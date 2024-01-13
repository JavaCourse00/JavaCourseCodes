package io.kimmking.rpcfx.registry;

import io.kimmking.rpcfx.api.ServiceProviderDesc;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/1/13 20:16
 */
public class RegistryCenter {

    CuratorFramework client = null;
    public void start() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.builder().connectString("localhost:2181").namespace("rpcfx").retryPolicy(retryPolicy).build();
        client.start();
    }

    public void stop(){
        client.close();
    }

    public void registerService(String service, String host, int port) throws Exception {
        ServiceProviderDesc userServiceSesc = ServiceProviderDesc.builder()
                .host(host)
                .port(port).serviceClass(service).build();
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

    public void unregisterService(String service, String host, int port) throws Exception {

        if (null == client.checkExists().forPath("/" + service)) {
            return;
        }
        System.out.println("delete " + "/" + service + "/" + host + "_" + port);
        client.delete().quietly().
                forPath( "/" + service + "/" + host + "_" + port);
    }

}
