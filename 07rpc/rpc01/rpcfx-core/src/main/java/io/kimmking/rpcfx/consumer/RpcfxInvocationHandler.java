package io.kimmking.rpcfx.consumer;

import com.alibaba.fastjson.JSON;
import io.kimmking.rpcfx.api.*;
import io.kimmking.rpcfx.stub.StubSkeletonHelper;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RpcfxInvocationHandler implements InvocationHandler {

    public static final MediaType JSONTYPE = MediaType.get("application/json; charset=utf-8");

    private final Class<?> serviceClass;
    private final List<String> invokers;
    private final Router router;
    private final LoadBalancer loadBalance;
    private final Filter[] filters;

    public <T> RpcfxInvocationHandler(Class<T> serviceClass, List<String> invokers, Router router, LoadBalancer loadBalance, Filter... filters) {
        this.serviceClass = serviceClass;
        this.invokers = invokers;
        this.router = router;
        this.loadBalance = loadBalance;
        this.filters = filters;
    }

    // 可以尝试，自己去写对象序列化，二进制还是文本的，，，rpcfx是xml自定义序列化、反序列化，json: code.google.com/p/rpcfx
    // int byte char float double long bool
    // [], data class

    @Override
    public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {

        if (!StubSkeletonHelper.checkRpcMethod(method)){
            return null ;
        }

        List<String> urls = router.route(invokers);
//            System.out.println("router.route => ");
//            urls.forEach(System.out::println);
        String url = loadBalance.select(urls); // router, loadbalance
//            System.out.println("loadBalance.select => ");
//            System.out.println("final => " + url);

        if (url == null) {
            throw new RuntimeException("No available providers from registry center.");
        }

        // 加filter地方之二
        // mock == true, new Student("hubao");

        RpcfxRequest request = new RpcfxRequest();
        request.setServiceClass(this.serviceClass.getName());
        request.setMethod(method.getName());
        request.setParams(params);

        if (null!=filters) {
            for (Filter filter : filters) {
                if (!filter.filter(request)) {
                    return null;
                }
            }
        }

        RpcfxResponse response = post(request, url);

        // 加filter地方之三
        // Student.setTeacher("cuijing");

        // 这里判断response.status，处理异常
        // 考虑封装一个全局的RpcfxException

        return JSON.parse(response.getResult().toString());
    }

    OkHttpClient client = new OkHttpClient.Builder()
            .connectionPool(new ConnectionPool(128, 60, TimeUnit.SECONDS))
//            .dispatcher(dispatcher)
//            .readTimeout(httpClientConfig.getReadTimeout(), TimeUnit.SECONDS)
//            .writeTimeout(httpClientConfig.getWriteTimeout(), TimeUnit.SECONDS)
//            .connectTimeout(httpClientConfig.getConnectTimeout(), TimeUnit.SECONDS)
            .build();

    private RpcfxResponse post(RpcfxRequest req, String url) throws IOException {
        String reqJson = JSON.toJSONString(req);
//            System.out.println("req json: "+reqJson);

        final Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSONTYPE, reqJson))
                .build();
        String respJson = client.newCall(request).execute().body().string();
//            System.out.println("resp json: "+respJson);
        return JSON.parseObject(respJson, RpcfxResponse.class);
    }
}