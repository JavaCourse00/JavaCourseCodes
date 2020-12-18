package io.kimmking.rpcfx.api.annotation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

import java.util.Set;

import static org.springframework.context.annotation.AnnotationConfigUtils.registerAnnotationConfigProcessors;

public class RpcfxClassPathBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {


    public RpcfxClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters, Environment environment,
                                               ResourceLoader resourceLoader) {

        super(registry, useDefaultFilters);

        setEnvironment(environment);

        setResourceLoader(resourceLoader);

        registerAnnotationConfigProcessors(registry);

    }

    public RpcfxClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry, Environment environment,
                                               ResourceLoader resourceLoader) {

        this(registry, false, environment, resourceLoader);

    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        return super.doScan(basePackages);
    }

    @Override
    public boolean checkCandidate(String beanName, BeanDefinition beanDefinition) throws IllegalStateException {
        return super.checkCandidate(beanName, beanDefinition);
    }

}