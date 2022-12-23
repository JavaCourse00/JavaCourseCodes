package io.kimmking.javacourse.demo;

import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;


/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2022/12/22 16:43
 */

@Configuration
//@EnableConfigurationProperties(ConsConfigs.class)
@Import(ConsConfigAutoConfiguration.ConsRegistrar.class)
public class ConsConfigAutoConfiguration {
    public static class ConsRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

        Environment environment;
//        ApplicationContext applicationContext;
//        @Override
//        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//            this.applicationContext = applicationContext;
//        }

        @Override
        public void setEnvironment(Environment environment) {
            this.environment = environment;
        }



        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            //ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(importingClassMetadata, registry);

            RootBeanDefinition def = new RootBeanDefinition();
            def.setBeanClass(ConsConfig.class);
            registry.registerBeanDefinition("cons", def);

            RootBeanDefinition definition = new RootBeanDefinition();
            definition.setBeanClass(ConsConfigs.class);
            definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
            definition.setDependencyCheck(AbstractBeanDefinition.DEPENDENCY_CHECK_NONE);
            //ConstructorArgumentValues argumentValues = new ConstructorArgumentValues();

            //ConsConfig config= applicationContext.getBean(ConsConfig.class);
//            String name = ConsConfig.class.getCanonicalName();
//            boolean exist = registry.containsBeanDefinition(name);
//            if(exist) {
//                argumentValues.addGenericArgumentValue(registry.getBeanDefinition(name));
//            } else {
//                ConsConfig cf = new ConsConfig();
//                cf.setDemoName("defaultName1");
//                cf.setDemoDesc("defaultDesc1");
//                argumentValues.addGenericArgumentValue(cf);
//            }
            //definition.setConstructorArgumentValues(argumentValues);
            registry.registerBeanDefinition("conss", definition);
        }
    }
}
