package io.kimmking.rpcfx.registry;

import io.kimmking.rpcfx.api.ServiceProviderDesc;
import io.kimmking.rpcfx.meta.InstanceMeta;
import io.kimmking.rpcfx.meta.ServiceMeta;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.List;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/2/8 15:23
 */
public interface RegistryCenter {

     void start();

     void stop();

     void registerService(ServiceMeta service, InstanceMeta instance) throws Exception;

     void unregisterService(ServiceMeta service, InstanceMeta instance) throws Exception;

     List<InstanceMeta> fetchInstances(ServiceMeta service) throws Exception;

     void subscribe(ServiceMeta service, ChangedListener<List<InstanceMeta>> listener);

     void heartbeat(ServiceMeta service, InstanceMeta instance);

}
