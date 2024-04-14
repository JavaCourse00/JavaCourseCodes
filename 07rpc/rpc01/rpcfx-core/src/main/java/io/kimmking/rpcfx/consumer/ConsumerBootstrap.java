package io.kimmking.rpcfx.consumer;

import io.kimmking.rpcfx.annotation.RpcfxReference;
import io.kimmking.rpcfx.api.RpcContext;
import io.kimmking.rpcfx.meta.ServiceMeta;
import io.kimmking.rpcfx.registry.RegistryCenter;
import io.kimmking.rpcfx.registry.RegistryConfiguration;
import io.kimmking.rpcfx.stub.StubSkeletonHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/1/13 23:26
 */
@Slf4j
@Component
@Import({RegistryConfiguration.class})
public class ConsumerBootstrap implements Closeable, InstantiationAwareBeanPostProcessor {

    private RpcContext context = new RpcContext();

    private String scanPackage = "io.kimmking";

    @Value("${app.id:app1}")
    public String app;
    @Value("${app.namespace:public}")
    public String ns;
    @Value("${app.env:dev}")
    public String env;
    @Value("${app.mock:false}")
    public boolean mock;
    @Value("${app.cache:false}")
    public boolean cache;
    @Value("${app.retry:1}")
    public int retry;

    @Autowired
    RegistryCenter rc;

    @PostConstruct
    public void init() {
        this.context.getParameters().put("app.id", app);
        this.context.getParameters().put("app.namespace", ns);
        this.context.getParameters().put("app.env", env);
        this.context.getParameters().put("app.mock", String.valueOf(mock));
        this.context.getParameters().put("app.cache", String.valueOf(cache));
        this.context.getParameters().put("app.retry", String.valueOf(retry));
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        if (bean.getClass().getPackage().getName().startsWith(scanPackage)) {
            Field[] declaredFields = resolveAllField(bean.getClass()); // 解决父类里的注解扫描不到的问题

            List<Field> consumers = Arrays.stream(declaredFields)
                    .filter(field -> field.isAnnotationPresent(RpcfxReference.class))
                    .collect(Collectors.toList());

            consumers.stream().forEach(field -> {
                Object consumer = createConsumer(field.getType());
                try {
                    field.setAccessible(true);
                    field.set(bean, consumer);
                } catch (IllegalAccessException e) {
                    log.error(e.getMessage(), e);
                }
            });
        }
        return null;
    }

    private Field[] resolveAllField(Class<?> aClass) {
        List<Field> res = new ArrayList<>(20);
        while ( !Object.class.equals(aClass) ) {
            Field[] fields = aClass.getDeclaredFields();
            res.addAll(Arrays.asList(fields));
            aClass = aClass.getSuperclass();
        }
        return res.toArray(new Field[0]);
    }

    private <T> T createConsumer(Class<T> clazz) {
        ServiceMeta sm = ServiceMeta.builder().name(clazz.getCanonicalName())
                .app(app).namespace(ns).env(env).build();
        return (T) StubSkeletonHelper.createConsumer(sm, context, rc);
    }
}
