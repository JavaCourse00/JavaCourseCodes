package io.kimmking.rpcfx.consumer;


import com.alibaba.fastjson.parser.ParserConfig;
import io.kimmking.rpcfx.api.*;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public final class RpcfxInvoker {

    static {
        ParserConfig.getGlobalInstance().addAccept("io.kimmking");
    }
    CuratorFramework client;
    String zkUrl = null;

    public RpcfxInvoker(String zkUrl) {
        this.zkUrl = zkUrl; //"localhost:2181"
        this.start();
    }

    public void start() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.builder().connectString(this.zkUrl).namespace("rpcfx").retryPolicy(retryPolicy).build();
        client.start();
    }

    public void stop() {
        client.close();
    }

    public <T> T createFromRegistry(final Class<T> serviceClass, Router router, LoadBalancer loadBalance, Filter filter) {

        String service = serviceClass.getCanonicalName();//"io.kimking.rpcfx.demo.api.UserService";
        System.out.println("====> "+service);
        List<String> invokers = new ArrayList<>();

        try {

            if ( null == client.checkExists().forPath("/" + service)) {
                return null;
            }

            fetchInvokers(client, service, invokers);

            final TreeCache treeCache = TreeCache.newBuilder(client, "/" + service).setCacheData(true).setMaxDepth(2).build();
            treeCache.getListenable().addListener(new TreeCacheListener() {
                public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {
                    System.out.println("treeCacheEvent: "+treeCacheEvent);
                    fetchInvokers(client, service, invokers);
                }
            });
            treeCache.start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return (T) create(serviceClass, invokers, router, loadBalance, filter);

    }



    private void fetchInvokers(CuratorFramework client, String service, List<String> invokers) throws Exception {
        List<String> services = client.getChildren().forPath("/" + service);
        invokers.clear();
        for (String svc : services) {
            System.out.println(svc);
            String url = svc.replace("_", ":");
            invokers.add("http://" + url);
        }
    }

    private <T> T create(Class<T> serviceClass, List<String> invokers, Router router, LoadBalancer loadBalance, Filter... filters) {
        RpcfxInvocationHandler invocationHandler
                = new RpcfxInvocationHandler(serviceClass, invokers, router, loadBalance, filters);
        return (T) Proxy.newProxyInstance(RpcfxInvoker.class.getClassLoader(),
                new Class[]{serviceClass}, invocationHandler);
    }

}
