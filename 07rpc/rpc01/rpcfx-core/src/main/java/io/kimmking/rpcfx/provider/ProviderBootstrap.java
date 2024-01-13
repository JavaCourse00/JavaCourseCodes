package io.kimmking.rpcfx.provider;

import io.kimmking.rpcfx.annotation.RpcfxService;
import io.kimmking.rpcfx.api.RpcContext;
import io.kimmking.rpcfx.registry.RegistryCenter;
import io.kimmking.rpcfx.stub.StubSkeletonHelper;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Objects;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/1/13 20:27
 */

@Component
public class ProviderBootstrap {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    Environment environment;

    private RpcContext rpcContext = new RpcContext();

    @Getter
    private RpcfxInvoker invoker;

    private static String PROTO = "http";
    private static String ip;
    private static int port;

    RegistryCenter registry = new RegistryCenter();

    @SneakyThrows
    @PostConstruct
    public void start(){
        System.out.println("build all services from annotation...");
        buildProvider();

        System.out.println("get IP and PORT...");
        ip = InetAddress.getLocalHost().getHostAddress();
        port  = Integer.parseInt(Objects.requireNonNull(environment.getProperty("server.port")));
    }

    private void buildProvider() {
        String[] beansName = applicationContext.getBeanDefinitionNames();
        for (int i = 0; i < beansName.length; i++) {
            String beanName = beansName[i];
            Object bean = applicationContext.getBean(beanName);
            RpcfxService provider = AnnotationUtils.findAnnotation(bean.getClass(), RpcfxService.class);
            if (provider == null) {
                continue;
            }
            Class<?>[] classes = bean.getClass().getInterfaces();
            if (classes == null || classes.length == 0) {
                continue;
            }
            Arrays.stream(classes).forEach(c -> this.createProvider(c, bean));
        }
    }

    private void createProvider(Class<?> clazz, Object bean) {
        StubSkeletonHelper.createProvider(clazz, bean, rpcContext); // 初始化了holder
        this.invoker = new RpcfxInvoker(rpcContext);
    }

    @Order(Integer.MIN_VALUE)
    @Bean
    public ApplicationRunner run() throws Exception {
        return x -> registerServices();
    }

    private void registerServices() {

        registry.start();

        System.out.println("registry all services from zk...");
        rpcContext.getProviderHolder().forEach( (x,y) ->
        {
            System.out.println(" register " + x);

            try {
                registry.registerService(x, ip, port);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        );
    }

    @PreDestroy
    public void stop() {
        unregisterServices();
    }

    private void unregisterServices() {
        System.out.println("unregistry all services from zk...");
        rpcContext.getProviderHolder().forEach( (x,y) ->
                {
                    System.out.println(" unregister " + x);
                    try {
                        registry.unregisterService(x, ip, port);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        registry.stop();

    }



}
