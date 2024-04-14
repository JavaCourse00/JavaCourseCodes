package io.kimmking.rpcfx.registry.kkregistry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import io.kimmking.rpcfx.meta.InstanceMeta;
import io.kimmking.rpcfx.meta.ServiceMeta;
import io.kimmking.rpcfx.registry.ChangedListener;
import io.kimmking.rpcfx.registry.Event;
import io.kimmking.rpcfx.registry.RegistryCenter;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.kimmking.rpcfx.consumer.RpcfxInvocationHandler.JSONTYPE;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/2/8 15:25
 */
public class KKRegistryCenter implements RegistryCenter {

    private Map<String, Long> TV = new HashMap<>();

    OkHttpClient client;
    @Override
    public void start() {
        client = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(128, 60, TimeUnit.SECONDS))
//            .dispatcher(dispatcher)
            .readTimeout(65, TimeUnit.SECONDS)
            .writeTimeout(65, TimeUnit.SECONDS)
            .connectTimeout(3, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public void stop() {
        this.checker.stop();
    }

    @Override
    public void registerService(ServiceMeta service, InstanceMeta instance) throws Exception {
        instance.setStatus(true);
        String reqJson = JSON.toJSONString(instance);
        String url = "http://localhost:8484/reg?service=" + service;
        System.out.println(" ====> reg service: " + url);
        final Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSONTYPE, reqJson))
                .build();
        String respJson = client.newCall(request).execute().body().string();
        System.out.println(" ====> reg response: " + respJson);
//        String reqJson = "{\n" +
//                "  \"scheme\": \"http\",\n" +
//                "  \"ip\": \"" + instance.getIp() + "\",\n" +
//                "  \"port\": \"" + instance.getPort() + "\",\n" +
//                "  \"context\": \"\",\n" +
//                "  \"status\": \"online\",\n" +
//                "  \"metadata\": {\n" +
//                "    \"env\": \"dev\",\n" +
//                "    \"tag\": \"RED\"\n" +
//                "  }\n" +
//                "}";
//        final Request request = new Request.Builder()
//                .url("http://localhost:8484/reg?service=" + service)
//                .post(RequestBody.create(JSONTYPE, reqJson))
//                .build();
//        String respJson = client.newCall(request).execute().body().string();
//        System.out.println(respJson);
    }

    @Override
    public void unregisterService(ServiceMeta service, InstanceMeta instance) throws Exception {
        String reqJson = "{\n" +
                "  \"scheme\": \"http\",\n" +
                "  \"host\": \"" + instance.getHost() + "\",\n" +
                "  \"port\": \"" + instance.getPort() + "\",\n" +
                "  \"context\": \"\"\n" +
                "}";
        String url = "http://localhost:8484/unreg?service=" + service;
        System.out.println(" ====> unreg service: " + url);
        final Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSONTYPE, reqJson))
                .build();
        String respJson = client.newCall(request).execute().body().string();
        System.out.println(" ====> unreg response: " + respJson);
    }

    public List<InstanceMeta> fetchInstances(ServiceMeta service) throws Exception {
        String url = "http://localhost:8484/findAll?service=" + service;
        System.out.println(" ====> fetchInstances: " + url);
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        String respJson = client.newCall(request).execute().body().string();
        System.out.println(" ====> fetchInstances response: " + respJson);
        List<InstanceMeta> instances = JSON.parseObject(respJson, new TypeReference<List<InstanceMeta>>() {
        });
        return instances;
    }

    KKHeathChecker checker = new KKHeathChecker();

    // for Consumer
    public void subscribe(ServiceMeta service, final ChangedListener listener) {

        checker.check( () -> {
            if(hb(service, listener)) {
                List<InstanceMeta> instances = fetchInstances(service);
                Event e = Event.withData(instances);
                listener.fireEvent(e);
            }
        });

        // 定时器轮询
        // 保存上一次的TV
        // 如果有差异就fire
    }

    private boolean hb(ServiceMeta service, ChangedListener listener) throws Exception {
        String svc = service.toString();
        String url = "http://localhost:8484/version?service=" + svc;
        System.out.println(" ====> consumer version: " + url);
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        String respJson = client.newCall(request).execute().body().string();
        System.out.println(" ====> consumer version: "+respJson);
        Long v = Long.valueOf(respJson);
        Long o = TV.getOrDefault(svc, -1L);
        if ( v > o) {
            TV.put(svc, v);
            return o > -1L;
        }
        return false;
    }


     // for Provider
    public void heartbeat(ServiceMeta service, InstanceMeta instance) {
        checker.check( () -> {
            heart(service, instance);
        });
    }

    Long heart(ServiceMeta service, InstanceMeta instance) throws Exception {
        String reqJson = "{\n" +
                "  \"scheme\": \"http\",\n" +
                "  \"host\": \"" + instance.getHost() + "\",\n" +
                "  \"port\": \"" + instance.getPort() + "\",\n" +
                "  \"context\": \"\",\n" +
                "  \"status\": true\n" +
                "}";
        String url = "http://localhost:8484/renew?service=" + service;
        System.out.println(" ====> provider renew: " + url);
        final Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSONTYPE, reqJson))
                .build();
        String respJson = client.newCall(request).execute().body().string();
        System.out.println(" ====> provider renew: "+respJson);
        return Long.valueOf(respJson);
    }

}
