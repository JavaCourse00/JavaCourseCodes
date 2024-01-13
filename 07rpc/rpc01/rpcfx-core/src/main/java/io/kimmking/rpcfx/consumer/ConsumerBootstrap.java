package io.kimmking.rpcfx.consumer;

import io.kimmking.rpcfx.annotation.RpcfxReference;
import io.kimmking.rpcfx.api.RpcContext;
import io.kimmking.rpcfx.stub.StubSkeletonHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;
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
public class ConsumerBootstrap implements Closeable, InstantiationAwareBeanPostProcessor {

    private RpcContext rpcContext = new RpcContext();

    private String scanPackage = "io.kimmking";

    @Override
    public void close() throws IOException {

    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        if (bean.getClass().getPackage().getName().startsWith(scanPackage)) {
            Field[] declaredFields = bean.getClass().getDeclaredFields();
            List<Field> consumers = Arrays.stream(declaredFields).filter(field -> field.isAnnotationPresent(RpcfxReference.class)).collect(Collectors.toList());

            consumers.stream().forEach(consumer -> {
                Object consumer1 = createConsumer(consumer.getType());
                try {
                    consumer.setAccessible(true);
                    consumer.set(bean, consumer1);
                } catch (IllegalAccessException e) {
                    log.error(e.getMessage(), e);
                }
            });
        }
        return null;
    }

    private <T> T createConsumer(Class<T> clazz) {
        return StubSkeletonHelper.createConsumer(clazz, rpcContext);
    }
}
