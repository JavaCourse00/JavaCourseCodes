package io.kimmking.rpcfx.stub;

import io.kimmking.rpcfx.api.*;
import io.kimmking.rpcfx.consumer.RpcfxConsumerInvoker;
import io.kimmking.rpcfx.meta.InstanceMeta;
import io.kimmking.rpcfx.meta.ProviderMeta;
import io.kimmking.rpcfx.meta.ServiceMeta;
import io.kimmking.rpcfx.registry.RegistryCenter;
import io.kimmking.rpcfx.utils.MethodUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lirui
 */
public class StubSkeletonHelper {

    public static void createProvider(Class<?> clazz, Object serviceImpl, RpcContext rpcContext) {
        String clazzName = clazz.getName();
        Class<?> callClass = serviceImpl.getClass();

        Method[] methodList = callClass.getMethods();
        for (Method method : methodList) {
            if (!checkRpcMethod(method)) {
                continue;
            }
            ProviderMeta providerMeta = buildProviderMeta(method, serviceImpl);

            MultiValueMap<String, ProviderMeta> providerHolder = rpcContext.getProviderHolder();
            providerHolder.add(clazzName, providerMeta);
        }
    }

    private static ProviderMeta buildProviderMeta(Method method, Object serviceImpl) {
        String methodSign = MethodUtils.methodSign(method);
        ProviderMeta providerMeta = new ProviderMeta();
        providerMeta.setMethod(method);
        providerMeta.setServiceImpl(serviceImpl);
        providerMeta.setMethodSign(methodSign);
        return providerMeta;
    }

    public static boolean checkRpcMethod(final Method method) {
        //本地方法不代理
        if ("toString".equals(method.getName()) ||
                "hashCode".equals(method.getName()) ||
                "notifyAll".equals(method.getName()) ||
                "equals".equals(method.getName()) ||
                "wait".equals(method.getName()) ||
                "getClass".equals(method.getName()) ||
                "notify".equals(method.getName())) {
            return false;
        }
        return true;
    }

    public static <T> T createConsumer(ServiceMeta sm, RpcContext ctx, RegistryCenter rc) {
        String clazzName = sm.getName();
        Class<?> serviceClass = null;
        try {
            serviceClass = Class.forName(clazzName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        T proxyHandler = (T) ctx.getConsumerHolder().get(clazzName);
        if (proxyHandler == null) { // TODO configuration

            ctx.setRouter(new TagRouter());
            ctx.setLoadBalancer(new RoundRibbonLoadBalancer());
            ctx.setFilters(createFilters(ctx));

            T mockHandler = createMockHandler(ctx, serviceClass);
            if(mockHandler != null) {
                return mockHandler;
            }

            RpcfxConsumerInvoker consumerInvoker = new RpcfxConsumerInvoker(ctx, rc);
            consumerInvoker.start();
            proxyHandler = consumerInvoker.createFromRegistry(sm, ctx);
            ctx.getConsumerHolder().put(clazzName, proxyHandler);
        }
        return proxyHandler;
    }

    private static Filter[] createFilters(RpcContext ctx) {
        String cache = ctx.getParameters().getOrDefault("app.cache", "false");
        Filter[] filters = null;
        if("true".equalsIgnoreCase(cache)) {
            filters = new Filter[]{new CuicuiFilter(), new CacheFilter()};
        } else {
            filters = new Filter[]{new CuicuiFilter()};
        }
        return filters;
    }

    private static <T> T createMockHandler(RpcContext ctx, Class<?> serviceClass) {
        String mock = ctx.getParameters().getOrDefault("app.mock", "false");
        if("true".equalsIgnoreCase(mock)) {
            return (T) MockHandler.createMock(serviceClass);
        }
        return null;
    }


    private static class TagRouter implements Router {
        @Override
        public List<InstanceMeta> route(List<InstanceMeta> instances) {
            return instances;
        }
    }

    private static class RoundRibbonLoadBalancer implements LoadBalancer {
        private final AtomicInteger count = new AtomicInteger(0);
        @Override
        public InstanceMeta select(List<InstanceMeta> instances) {
            if(instances.isEmpty()) return null;
            return instances.get((count.getAndIncrement() & Integer.MAX_VALUE) % instances.size());
        }
    }

    private static class RandomLoadBalancer implements LoadBalancer {
        private final Random random = new Random();
        @Override
        public InstanceMeta select(List<InstanceMeta> instances) {
            if(instances.isEmpty()) return null;
            return instances.get(random.nextInt(instances.size()));
        }
    }

    @Slf4j
    private static class CuicuiFilter implements Filter {
        @Override
        public RpcfxResponse prefilter(RpcfxRequest request) {
            //log.info("filter {} -> {}", this.getClass().getName(), request.toString());
            //System.out.printf("filter %s -> %s%n", this.getClass().getName(), request.toString());
            return null;
        }

        @Override
        public RpcfxResponse postfilter(RpcfxRequest request, RpcfxResponse response) {
            return response;
        }

    }

    private static class CacheFilter implements Filter {

        static Map<String, RpcfxResponse> CACHE = new HashMap<>();

        @Override
        public RpcfxResponse prefilter(RpcfxRequest request) {
            RpcfxResponse response = CACHE.get(genKey(request));
            if(response != null) {
                System.out.println("CacheFilter.prefilter hit! => request: \n" + request + "\n =>response: \n" + response);
            }
            return response;
            //log.info("filter {} -> {}", this.getClass().getName(), request.toString());
            //System.out.printf("filter %s -> %s%n", this.getClass().getName(), request.toString());
        }

        @Override
        public RpcfxResponse postfilter(RpcfxRequest request, RpcfxResponse response) {
            String key = genKey(request);
            if(!CACHE.containsKey(key)) {
                CACHE.put(key, response);
            }
            return response;
        }

    }

    public static String genKey(RpcfxRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(request.getServiceClass());
        sb.append("@");
        sb.append(request.getMethodSign());
        //sb.append("");
        Arrays.stream(request.getParams()).forEach(x -> sb.append("_"+x.toString()));
        return sb.toString();
    }

}
