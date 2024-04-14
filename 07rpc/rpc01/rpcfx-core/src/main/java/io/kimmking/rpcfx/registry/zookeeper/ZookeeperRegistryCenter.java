package io.kimmking.rpcfx.registry.zookeeper;

import io.kimmking.rpcfx.api.ServiceProviderDesc;
import io.kimmking.rpcfx.meta.InstanceMeta;
import io.kimmking.rpcfx.meta.ServiceMeta;
import io.kimmking.rpcfx.registry.ChangedListener;
import io.kimmking.rpcfx.registry.Event;
import io.kimmking.rpcfx.registry.RegistryCenter;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/1/13 20:16
 */
public class ZookeeperRegistryCenter implements RegistryCenter {

//    private final List<ChangedListener> listeners = new ArrayList<>();
//    public void addListener(ChangedListener listener) {
//        this.listeners.add(listener);
//    }

    CuratorFramework client = null;
    public void start() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.builder().connectString("localhost:2181").namespace("rpcfx").retryPolicy(retryPolicy).build();
        client.start();
    }

    public void stop(){
        client.close();
    }

    public void registerService(ServiceMeta service, InstanceMeta instance) throws Exception {
        ServiceProviderDesc userServiceSesc = ServiceProviderDesc.builder()
                .host(instance.getIp())
                .port(instance.getPort()).serviceClass(service.getName()).build();
        // String userServiceSescJson = JSON.toJSONString(userServiceSesc);

        try {
            if ( null == client.checkExists().forPath("/" + service)) {
                client.create().withMode(CreateMode.PERSISTENT).forPath("/" + service, "service".getBytes());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        client.create().withMode(CreateMode.EPHEMERAL).
                forPath( "/" + service + "/" + userServiceSesc.getHost() + "_" + userServiceSesc.getPort(), "provider".getBytes());
    }

    public void unregisterService(ServiceMeta service, InstanceMeta instance) throws Exception {

        if (null == client.checkExists().forPath("/" + service)) {
            return;
        }
        System.out.println("delete " + "/" + service + "/" + instance.getIp() + "_" + instance.getPort());
        client.delete().quietly().
                forPath( "/" + service + "/" + instance.getIp() + "_" + instance.getPort());
    }

    public List<InstanceMeta> fetchInstances(ServiceMeta service) throws Exception {
        List<String> services = client.getChildren().forPath("/" + service);
        List<InstanceMeta> instances = new ArrayList<>();
        for (String svc : services) {
            System.out.println(svc);
            String url = svc.replace("_", ":");
            instances.add(InstanceMeta.from("http://" + url));
        }
        return instances;
    }

    public void subscribe(ServiceMeta service, ChangedListener listener) {
        final TreeCache treeCache = TreeCache.newBuilder(client, "/" + service).setCacheData(true).setMaxDepth(2).build();
        treeCache.getListenable().addListener((curatorFramework, treeCacheEvent) -> {
            System.out.println("treeCacheEvent: "+treeCacheEvent);
            List<InstanceMeta> instances = fetchInstances(service);
            listener.fireEvent(Event.withData(instances));
        });
        try {
            treeCache.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void heartbeat(ServiceMeta service, InstanceMeta instance) {
        // do nothing
    }

}
