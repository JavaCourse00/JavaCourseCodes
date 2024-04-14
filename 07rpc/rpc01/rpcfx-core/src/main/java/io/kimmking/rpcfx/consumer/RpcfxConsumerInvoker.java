package io.kimmking.rpcfx.consumer;


import com.alibaba.fastjson.parser.ParserConfig;
import io.kimmking.rpcfx.api.*;
import io.kimmking.rpcfx.meta.InstanceMeta;
import io.kimmking.rpcfx.meta.ServiceMeta;
import io.kimmking.rpcfx.registry.RegistryCenter;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public final class RpcfxConsumerInvoker {

    static {
        ParserConfig.getGlobalInstance().addAccept("io.kimmking");
    }

    RpcContext ctx;

    RegistryCenter rc;

    public RpcfxConsumerInvoker(RpcContext ctx, RegistryCenter rc) {
        this.ctx = ctx;
        this.rc = rc; //"localhost:2181"
    }

    public void start() {
        this.rc.start();
    }

    public void stop() {
        this.rc.stop();
    }

    public <T> T createFromRegistry(final ServiceMeta sm, RpcContext ctx) {

        String service = sm.getName();//"io.kimking.rpcfx.demo.api.UserService";
        System.out.println("====> "+service);
        List<InstanceMeta> invokers = new ArrayList<>();
        Class<?> serviceClass = null;
        try {

            serviceClass = Class.forName(service);

            List<InstanceMeta> insts = rc.fetchInstances(sm);
            if(insts != null && insts.size()>0) invokers.addAll(insts);
            rc.subscribe(sm, e -> {
                invokers.clear();
                invokers.addAll((List<InstanceMeta>)e.getData());
            });

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

        return (T) create(serviceClass, invokers, ctx);

    }

    private <T> T create(Class<T> serviceClass, List<InstanceMeta> invokers, RpcContext ctx) {
        RpcfxInvocationHandler invocationHandler
                = new RpcfxInvocationHandler(serviceClass, invokers, ctx);
        return (T) Proxy.newProxyInstance(RpcfxConsumerInvoker.class.getClassLoader(),
                new Class[]{serviceClass}, invocationHandler);
    }

}
