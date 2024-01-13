package io.kimmking.rpcfx.stub;

import io.kimmking.rpcfx.api.*;
import io.kimmking.rpcfx.consumer.RpcfxInvoker;
import io.kimmking.rpcfx.meta.ProviderMeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

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
        String methodSign = method.getName();//MethodUtils.methodSign(method);
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

    public static <T> T createConsumer(Class<T> clazz, RpcContext rpcContext) {
        String clazzName = clazz.getName();
        T proxyHandler = (T) rpcContext.getConsumerHolder().get(clazzName);
        if (proxyHandler == null) { // TODO configuration
            proxyHandler = new RpcfxInvoker("localhost:2181")
                            .createFromRegistry(clazz, new TagRouter(),
                                new RandomLoadBalancer(), new CuicuiFilter());
            rpcContext.getConsumerHolder().put(clazzName, proxyHandler);
        }
        return (T) proxyHandler;
    }


    private static class TagRouter implements Router {
        @Override
        public List<String> route(List<String> urls) {
            return urls;
        }
    }

    private static class RandomLoadBalancer implements LoadBalancer {
        private final Random random = new Random();
        @Override
        public String select(List<String> urls) {
            if(urls.isEmpty()) return null;
            return urls.get(random.nextInt(urls.size()));
        }
    }

    @Slf4j
    private static class CuicuiFilter implements Filter {
        @Override
        public boolean filter(RpcfxRequest request) {
            //log.info("filter {} -> {}", this.getClass().getName(), request.toString());
            //System.out.printf("filter %s -> %s%n", this.getClass().getName(), request.toString());
            return true;
        }
    }

}
