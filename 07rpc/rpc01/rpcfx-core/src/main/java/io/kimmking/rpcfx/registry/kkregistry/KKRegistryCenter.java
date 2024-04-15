package io.kimmking.rpcfx.registry.kkregistry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import io.kimmking.rpcfx.meta.InstanceMeta;
import io.kimmking.rpcfx.meta.ServerMeta;
import io.kimmking.rpcfx.meta.ServiceMeta;
import io.kimmking.rpcfx.registry.ChangedListener;
import io.kimmking.rpcfx.registry.Event;
import io.kimmking.rpcfx.registry.RegistryCenter;
import lombok.SneakyThrows;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static io.kimmking.rpcfx.consumer.RpcfxInvocationHandler.JSONTYPE;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/2/8 15:25
 */
public class KKRegistryCenter implements RegistryCenter {

    public String RC_Server = "http://localhost:8485";
    private ServerMeta leader;
    private List<ServerMeta> servers;
    private Map<String, Long> TV = new HashMap<>();

    OkHttpClient client;
    @SneakyThrows
    @Override
    public void start() {
        client = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(128, 60, TimeUnit.SECONDS))
//            .dispatcher(dispatcher)
            .readTimeout(65, TimeUnit.SECONDS)
            .writeTimeout(65, TimeUnit.SECONDS)
            .connectTimeout(3, TimeUnit.SECONDS)
                .build();

        String url = RC_Server + "/cluster";
        boolean init = false;
        while(!init) {
            System.out.println("===============>> cluster info from :" + url);
            List<ServerMeta> new_servers = null;
            ServerMeta new_leader = null;
            try {
                String respJson = get(url);
                new_servers = JSON.parseObject(respJson, new TypeReference<List<ServerMeta>>() {
                });
                new_leader = new_servers.stream().filter(ServerMeta::isStatus)
                        .filter(ServerMeta::isLeader).findFirst().orElse(null);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            if(new_leader == null) {
                System.out.println("===============>> no leader, 500ms later and retry.");
                Thread.sleep(500);
                Random random = new Random();
                if(new_servers !=null && new_servers.size() > 1) {
                    url = new_servers.get(random.nextInt(new_servers.size())).getUrl() + "/cluster";
                } else if((new_servers ==null || new_servers.isEmpty()) && !servers.isEmpty()) {
                    url = servers.get(random.nextInt(servers.size())).getUrl() + "/cluster";
                }
            } else {
                this.servers = new_servers;
                this.leader = new_leader;
                init = true;
                System.out.println("===============>> init ok, new_leader  = " + new_leader);
                System.out.println("===============>> init ok, new_servers = " + new_servers);
            }
        }
    }

    @Override
    public void stop() {
        this.checker.stop();
    }

    @Override
    public void registerService(ServiceMeta service, InstanceMeta instance) throws Exception {
        instance.setStatus(true);
        String reqJson = JSON.toJSONString(instance);
        String url = leader.getUrl() + "/reg?service=" + service;
        post(url, reqJson);
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

    private String post(String url, String reqJson) throws IOException {
        System.out.println(" ====> request: " + url);
        final Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSONTYPE, reqJson))
                .build();
        String respJson = client.newCall(request).execute().body().string();
        System.out.println(" ====> response: " + respJson);
        return respJson;
    }

    private String get(String url) throws IOException {
        System.out.println(" ====> request: " + url);
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        String respJson = client.newCall(request).execute().body().string();
        System.out.println(" ====> response: " + respJson);
        return respJson;
    }

    @Override
    public void unregisterService(ServiceMeta service, InstanceMeta instance) throws Exception {
        String reqJson = "{\n" +
                "  \"scheme\": \"http\",\n" +
                "  \"host\": \"" + instance.getHost() + "\",\n" +
                "  \"port\": \"" + instance.getPort() + "\",\n" +
                "  \"context\": \"\"\n" +
                "}";
        String url = leader.getUrl() + "/unreg?service=" + service;
        post(url, reqJson);
    }

    public List<InstanceMeta> fetchInstances(ServiceMeta service) throws Exception {
        String url = RC_Server + "/findAll?service=" + service;
        String respJson = get(url);
        List<InstanceMeta> instances = JSON.parseObject(respJson, new TypeReference<List<InstanceMeta>>() {
        });
        return instances;
    }

    KKHeathChecker checker = new KKHeathChecker();

    // for Consumer
    public void subscribe(ServiceMeta service, final ChangedListener<List<InstanceMeta>> listener) {
        checker.check( () -> {
            if(hb(service)) {
                List<InstanceMeta> instances = fetchInstances(service);
                Event<List<InstanceMeta>>  e = Event.withData(instances);
                listener.fireEvent(e);
            }
        });

        // 定时器轮询
        // 保存上一次的TV
        // 如果有差异就fire
    }

    private boolean hb(ServiceMeta service) throws Exception {
        String svc = service.toString();
        String url = RC_Server + "/version?service=" + svc;
        String respJson = get(url);
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
        String url = leader.getUrl() + "/renew?service=" + service;
        String respJson = post(url, reqJson);
        return Long.valueOf(respJson);
    }

}
