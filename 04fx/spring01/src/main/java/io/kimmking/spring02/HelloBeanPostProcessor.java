package io.kimmking.spring02;

import io.kimmking.spring01.Student;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class HelloBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println(" ====> postProcessBeforeInitialization " + beanName  +":"+ bean);
        // 可以加点额外处理
        // 例如
        if (bean instanceof Student) {
            Student student = (Student) bean;
            student.setName(student.getName() + "-" + System.currentTimeMillis());
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println(" ====> postProcessAfterInitialization " + beanName +":"+ bean);
        return bean;
    }
}
