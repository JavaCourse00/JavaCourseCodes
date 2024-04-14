package io.kimmking.rpcfx.provider;

import io.kimmking.rpcfx.annotation.RpcfxService;
import io.kimmking.rpcfx.api.RpcContext;
import io.kimmking.rpcfx.meta.InstanceMeta;
import io.kimmking.rpcfx.meta.ServiceMeta;
import io.kimmking.rpcfx.registry.RegistryCenter;
import io.kimmking.rpcfx.registry.RegistryConfiguration;
import io.kimmking.rpcfx.stub.StubSkeletonHelper;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
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
@Import({RegistryConfiguration.class})
public class ProviderBootstrap {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    Environment environment;

    @Value("${app.id:app1}")
    public String app;
    @Value("${app.namespace:public}")
    public String ns;
    @Value("${app.env:dev}")
    public String env;

    private final RpcContext context = new RpcContext();

    @Getter
    private final RpcfxProviderInvoker invoker = new RpcfxProviderInvoker(context);;

    private static String SCHEME = "http";
    private static String ip;
    private static int port;

    @Autowired
    RegistryCenter registry;// = new KKRegistryCenter();

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
        StubSkeletonHelper.createProvider(clazz, bean, context); // 初始化了holder
    }

    @Order(Integer.MIN_VALUE)
    @Bean
    public ApplicationRunner run() throws Exception {
        return x -> registerServices();
    }

    private void registerServices() {

        registry.start();

        System.out.println("registry all services from RegistryCenter...");
        context.getProviderHolder().forEach( (x, y) ->
        {
            System.out.println(" register " + x);
            ServiceMeta sm = ServiceMeta.builder().name(x)
                    .app(app).namespace(ns).env(env).build();

            InstanceMeta im = InstanceMeta.builder()
                    .scheme(SCHEME).ip(ip).port(port).context("").build();
            try {
                registry.registerService(sm, im);
                registry.heartbeat(sm, im);
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
        System.out.println("unregistry all services from RegistryCenter...");
        context.getProviderHolder().forEach( (x, y) ->
                {
                    System.out.println(" unregister " + x);
                    ServiceMeta sm = ServiceMeta.builder().name(x)
                            .app(app).namespace(ns).env(env).build();
                    InstanceMeta im = InstanceMeta.builder()
                            .scheme(SCHEME).ip(ip).port(port).context("").build();
                    try {
                        registry.unregisterService(sm, im);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        registry.stop();

    }



}
