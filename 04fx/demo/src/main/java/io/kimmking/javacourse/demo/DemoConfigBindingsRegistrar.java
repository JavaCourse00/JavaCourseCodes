package io.kimmking.javacourse.demo;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.*;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

import static io.kimmking.javacourse.demo.PropertySourcesUtils.getSubProperties;
import static org.springframework.beans.factory.support.BeanDefinitionBuilder.rootBeanDefinition;

/**
 * This class is cloned and modified from https://github.com/apache/dubbo/blob/58d1259cb9d89528594e43db5d8667179005dcfc/dubbo-config/dubbo-config-spring/src/main/java/com/alibaba/dubbo/config/spring/context/annotation/DubboConfigBindingsRegistrar.java.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2022/11/29 16:09
 */
public class DemoConfigBindingsRegistrar  implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private ConfigurableEnvironment environment;

    @Override
    public void setEnvironment(Environment _environment) {
        this.environment = (ConfigurableEnvironment) _environment;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {
        registerBeanDefinitions(importingClassMetadata, registry);
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        AnnotationAttributes attributes = AnnotationAttributes.fromMap(
                importingClassMetadata.getAnnotationAttributes(EnableDemoConfigBindings.class.getName()));
        String prefix = environment.resolvePlaceholders(attributes.getString("prefix"));
        Class<? extends DemoConfig> configClass = attributes.getClass("type");
        boolean multiple = attributes.getBoolean("multiple");
        registerDubboConfigBeans(prefix, configClass, multiple, registry);
    }

    private void registerDubboConfigBeans(String prefix,
                                          Class<? extends DemoConfig> configClass,
                                          boolean multiple,
                                          BeanDefinitionRegistry registry) {
        Map<String, Object> properties = getSubProperties(environment.getPropertySources(), prefix);
        if (CollectionUtils.isEmpty(properties)) {
            System.out.println("There is no property for binding to demo config class [" + configClass.getName()
                        + "] within prefix [" + prefix + "]");
            return;
        }
        Set<String> beanNames = multiple ? resolveMultipleBeanNames(properties) :
                Collections.singleton(resolveSingleBeanName(properties, configClass, registry));
        Map<String, Map<String, Object>> groupProperties = getGroupProperties(properties, beanNames);
        for (String beanName : beanNames) {
            registerDemoConfigBean(beanName, configClass, registry, groupProperties.get(beanName));
            //registerDubboConfigBindingBeanPostProcessor(prefix, beanName, multiple, registry);
        }
        //registerDubboConfigBeanCustomizers(registry);
    }

    private Map<String, Map<String, Object>> getGroupProperties(Map<String, Object> properties, Set<String> beanNames) {
        Map<String, Map<String, Object>> map = new HashMap<>();
        for (String propertyName : properties.keySet()) {
            int index = propertyName.indexOf(".");
            if (index > 0) {
                String beanName = propertyName.substring(0, index);
                String beanPropertyName = propertyName.substring(index + 1);
                if (beanNames.contains(beanName)) {
                    Map<String, Object> group = map.get(beanName);
                    if (group == null) {
                        group = new HashMap<>();
                        map.put(beanName, group);
                    }
                    group.put(beanPropertyName, properties.get(propertyName));
                }
            }
        }
        return map;
    }

    private void registerDemoConfigBean(String beanName, Class<? extends DemoConfig> configClass,
                                        BeanDefinitionRegistry registry, Map<String, Object> properties) {
        BeanDefinitionBuilder builder = rootBeanDefinition(configClass);
        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
        // Convert Map to MutablePropertyValues
        MutablePropertyValues propertyValues = new MutablePropertyValues(properties);
        beanDefinition.setPropertyValues(propertyValues);
        registry.registerBeanDefinition(beanName, beanDefinition);
        System.out.println("The demo config bean definition [name : " + beanName + ", class : " + configClass.getName() +
                    "] has been registered.");
    }

    private Set<String> resolveMultipleBeanNames(Map<String, Object> properties) {
        Set<String> beanNames = new LinkedHashSet<String>();
        for (String propertyName : properties.keySet()) {
            int index = propertyName.indexOf(".");
            if (index > 0) {
                String beanName = propertyName.substring(0, index);
                beanNames.add(beanName);
            }
        }
        return beanNames;
    }

    private String resolveSingleBeanName(Map<String, Object> properties, Class<? extends DemoConfig> configClass,
                                         BeanDefinitionRegistry registry) {
        String beanName = (String) properties.get("demoName");
        if (!StringUtils.hasText(beanName)) {
            BeanDefinitionBuilder builder = rootBeanDefinition(configClass);
            beanName = BeanDefinitionReaderUtils.generateBeanName(builder.getRawBeanDefinition(), registry);
        }
        return beanName;
    }

}
