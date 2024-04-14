package io.kimmking.rpcfx.consumer;

import com.alibaba.fastjson.JSON;
import io.kimmking.rpcfx.api.*;
import io.kimmking.rpcfx.meta.InstanceMeta;
import io.kimmking.rpcfx.stub.StubSkeletonHelper;
import io.kimmking.rpcfx.utils.MethodUtils;
import okhttp3.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RpcfxInvocationHandler implements InvocationHandler {

    public final Object target = new Object();

    public static final MediaType JSONTYPE = MediaType.get("application/json; charset=utf-8");

    private final Class<?> serviceClass;
    private final List<InstanceMeta> invokers;

    private final RpcContext context;

    public <T> RpcfxInvocationHandler(Class<T> serviceClass, List<InstanceMeta> invokers, RpcContext ctx) {
        this.serviceClass = serviceClass;
        this.invokers = invokers;
        this.context = ctx;
    }

    // 可以尝试，自己去写对象序列化，二进制还是文本的，，，rpcfx是xml自定义序列化、反序列化，json: code.google.com/p/rpcfx
    // int byte char float double long bool
    // [], data class

    @Override
    public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {

        long start = System.currentTimeMillis();

        if (!StubSkeletonHelper.checkRpcMethod(method)){
            return method.invoke(target, params);
        }


        int retry = 2;
        while (retry-- > 0) {
            System.out.println("retry:" + retry);
            try {

                // check mock, 挡板功能 TODO 3

                List<InstanceMeta> insts = context.getRouter().route(invokers);
//            System.out.println("router.route => ");
//            urls.forEach(System.out::println);
                InstanceMeta instance = context.getLoadBalancer().select(insts); // router, loadbalance
//            System.out.println("loadBalance.select => ");
//            System.out.println("final => " + url);

                if (instance == null) {
                    throw new RuntimeException("No available providers from registry center.");
                }


                RpcfxRequest request = new RpcfxRequest();
                request.setServiceClass(this.serviceClass.getName());
                request.setMethodSign(MethodUtils.methodSign(method));
                request.setParams(params);

                Filter[] filters = context.getFilters();

                if (null != filters) {
                    for (Filter filter : filters) {
                        RpcfxResponse response = filter.prefilter(request);
                        if (response != null) {
                            return JSON.parse(response.getResult().toString());
                        }
                    }
                }

                // 没有控制超时，可能会很久 TODO 2
                RpcfxResponse response = post(request, instance);

                if (null != filters) {
                    for (Filter filter : filters) {
                        RpcfxResponse postResponse = filter.postfilter(request, response);
                        if (postResponse!=null) {
                            response = postResponse;
                        }
                    }
                }

                System.out.println("Invoke spend " + (System.currentTimeMillis()-start) + " ms");

                // 加filter地方之三
                // Student.setTeacher("cuijing");

                // 这里判断response.status，处理异常
                // 考虑封装一个全局的RpcfxException

                return JSON.parse(response.getResult().toString());

            } catch (RuntimeException ex) {
                ex.printStackTrace();
                if(! (ex.getCause() instanceof SocketTimeoutException)) {
                    break;
                }
            }
        }
        return null;

    }

    OkHttpClient client = new OkHttpClient.Builder()
            .connectionPool(new ConnectionPool(128, 60, TimeUnit.SECONDS))
//            .dispatcher(dispatcher)
            .readTimeout(1, TimeUnit.SECONDS)
            .writeTimeout(1, TimeUnit.SECONDS)
            .connectTimeout(1, TimeUnit.SECONDS)
            .build();

    private RpcfxResponse post(RpcfxRequest req, InstanceMeta instance) throws Exception {
        String reqJson = JSON.toJSONString(req);
            System.out.println("req json: "+reqJson);

        final Request request = new Request.Builder()
                .url(instance.toString())
                .post(RequestBody.create(JSONTYPE, reqJson))
                .build();
        String respJson;
        try {
            respJson = client.newCall(request).execute().body().string();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        System.out.println("resp json: "+respJson);
        return JSON.parseObject(respJson, RpcfxResponse.class);
    }
}