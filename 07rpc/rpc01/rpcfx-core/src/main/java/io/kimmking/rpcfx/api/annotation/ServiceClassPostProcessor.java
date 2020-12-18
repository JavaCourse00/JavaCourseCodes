package io.kimmking.rpcfx.api.annotation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.*;
import org.springframework.beans.factory.support.*;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.*;

import static java.util.Arrays.asList;
import static org.springframework.beans.factory.support.BeanDefinitionBuilder.rootBeanDefinition;
import static org.springframework.context.annotation.AnnotationConfigUtils.CONFIGURATION_BEAN_NAME_GENERATOR;
import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;
import static org.springframework.core.annotation.AnnotationUtils.getAnnotationAttributes;
import static org.springframework.util.ClassUtils.resolveClassName;

@Slf4j
public class ServiceClassPostProcessor implements BeanDefinitionRegistryPostProcessor, EnvironmentAware,
        ResourceLoaderAware, BeanClassLoaderAware {

    private final static List<Class<? extends Annotation>> serviceAnnotationTypes = asList(
            RpcfxService.class
    );

    protected final Set<String> packagesToScan;
    private Environment environment;
    private ResourceLoader resourceLoader;
    private ClassLoader classLoader;

    public ServiceClassPostProcessor(String... packagesToScan) {
        this(asList(packagesToScan));
    }

    public ServiceClassPostProcessor(Collection<String> packagesToScan) {
        this(new LinkedHashSet<>(packagesToScan));
    }

    public ServiceClassPostProcessor(Set<String> packagesToScan) {
        this.packagesToScan = packagesToScan;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
//        registerInfrastructureBean(registry, DubboBootstrapApplicationListener.BEAN_NAME, DubboBootstrapApplicationListener.class);
        Set<String> resolvedPackagesToScan = resolvePackagesToScan(packagesToScan);
        if (!CollectionUtils.isEmpty(resolvedPackagesToScan)) {
            registerServiceBeans(resolvedPackagesToScan, registry);
        } else {
            if (log.isWarnEnabled()) {
                log.warn("packagesToScan is empty , ServiceBean registry will be ignored!");
            }
        }
    }

    private void registerServiceBeans(Set<String> packagesToScan, BeanDefinitionRegistry registry) {
        RpcfxClassPathBeanDefinitionScanner scanner =
                new RpcfxClassPathBeanDefinitionScanner(registry, environment, resourceLoader);
        BeanNameGenerator beanNameGenerator = resolveBeanNameGenerator(registry);
        scanner.setBeanNameGenerator(beanNameGenerator);
        serviceAnnotationTypes.forEach(annotationType -> {
            scanner.addIncludeFilter(new AnnotationTypeFilter(annotationType));
        });
        for (String packageToScan : packagesToScan) {
            // Registers @Service Bean first
            scanner.scan(packageToScan);
            // Finds all BeanDefinitionHolders of @Service whether @ComponentScan scans or not.
            Set<BeanDefinitionHolder> beanDefinitionHolders =
                    findServiceBeanDefinitionHolders(scanner, packageToScan, registry, beanNameGenerator);
            if (!CollectionUtils.isEmpty(beanDefinitionHolders)) {
                for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {
                    registerServiceBean(beanDefinitionHolder, registry, scanner);
                }
                if (log.isInfoEnabled()) {
                    log.info(beanDefinitionHolders.size() + " annotated Dubbo's @Service Components { " +
                            beanDefinitionHolders +
                            " } were scanned under package[" + packageToScan + "]");
                }
            } else {
                if (log.isWarnEnabled()) {
                    log.warn("No Spring Bean annotating Dubbo's @Service was found under package["
                            + packageToScan + "]");
                }
            }
        }
    }

    private BeanNameGenerator resolveBeanNameGenerator(BeanDefinitionRegistry registry) {
        BeanNameGenerator beanNameGenerator = null;
        if (registry instanceof SingletonBeanRegistry) {
            SingletonBeanRegistry singletonBeanRegistry = SingletonBeanRegistry.class.cast(registry);
            beanNameGenerator = (BeanNameGenerator) singletonBeanRegistry.getSingleton(CONFIGURATION_BEAN_NAME_GENERATOR);
        }
        if (beanNameGenerator == null) {
            if (log.isInfoEnabled()) {
                log.info("BeanNameGenerator bean can't be found in BeanFactory with name ["
                        + CONFIGURATION_BEAN_NAME_GENERATOR + "]");
                log.info("BeanNameGenerator will be a instance of " +
                        AnnotationBeanNameGenerator.class.getName() +
                        " , it maybe a potential problem on bean name generation.");
            }
            beanNameGenerator = new AnnotationBeanNameGenerator();
        }
        return beanNameGenerator;
    }

    private Set<BeanDefinitionHolder> findServiceBeanDefinitionHolders(
            ClassPathBeanDefinitionScanner scanner, String packageToScan, BeanDefinitionRegistry registry,
            BeanNameGenerator beanNameGenerator) {
        Set<BeanDefinition> beanDefinitions = scanner.findCandidateComponents(packageToScan);
        Set<BeanDefinitionHolder> beanDefinitionHolders = new LinkedHashSet<>(beanDefinitions.size());
        for (BeanDefinition beanDefinition : beanDefinitions) {
            String beanName = beanNameGenerator.generateBeanName(beanDefinition, registry);
            BeanDefinitionHolder beanDefinitionHolder = new BeanDefinitionHolder(beanDefinition, beanName);
            beanDefinitionHolders.add(beanDefinitionHolder);
        }
        return beanDefinitionHolders;
    }

    private void registerServiceBean(BeanDefinitionHolder beanDefinitionHolder, BeanDefinitionRegistry registry,
                                     RpcfxClassPathBeanDefinitionScanner scanner) {
        Class<?> beanClass = resolveClass(beanDefinitionHolder);
        Annotation service = findServiceAnnotation(beanClass);
        AnnotationAttributes serviceAnnotationAttributes = getAnnotationAttributes(service, false, false);
        Class<?> interfaceClass = serviceAnnotationAttributes.getClass("interfaceName"); //resolveServiceInterfaceClass(serviceAnnotationAttributes, beanClass);
        String annotatedServiceBeanName = beanDefinitionHolder.getBeanName();
        AbstractBeanDefinition serviceBeanDefinition =
                buildServiceBeanDefinition(service, serviceAnnotationAttributes, interfaceClass, annotatedServiceBeanName);

        // ServiceBean Bean name
        String beanName = interfaceClass.getName(); //generateServiceBeanName(serviceAnnotationAttributes, interfaceClass);
        if (scanner.checkCandidate(beanName, serviceBeanDefinition)) { // check duplicated candidate bean
            registry.registerBeanDefinition(beanName, serviceBeanDefinition);
            if (log.isInfoEnabled()) {
                log.info("The BeanDefinition[" + serviceBeanDefinition +
                        "] of ServiceBean has been registered with name : " + beanName);
            }
        } else {
            if (log.isWarnEnabled()) {
                log.warn("The Duplicated BeanDefinition[" + serviceBeanDefinition +
                        "] of ServiceBean[ bean name : " + beanName +
                        "] was be found , Did @DubboComponentScan scan to same package in many times?");
            }
        }
    }

    private Annotation findServiceAnnotation(Class<?> beanClass) {
        return serviceAnnotationTypes
                .stream()
                .map(annotationType -> findMergedAnnotation(beanClass, annotationType))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

//    private String generateServiceBeanName(AnnotationAttributes serviceAnnotationAttributes, Class<?> interfaceClass) {
////        ServiceBeanNameBuilder builder = create(interfaceClass, environment)
////                .group(serviceAnnotationAttributes.getString("group"))
////                .version(serviceAnnotationAttributes.getString("version"));
////        return builder.build();
//        return interfaceClass.getName();
//    }

    private Class<?> resolveClass(BeanDefinitionHolder beanDefinitionHolder) {
        BeanDefinition beanDefinition = beanDefinitionHolder.getBeanDefinition();
        return resolveClass(beanDefinition);
    }

    private Class<?> resolveClass(BeanDefinition beanDefinition) {
        String beanClassName = beanDefinition.getBeanClassName();
        return resolveClassName(beanClassName, classLoader);
    }

    private Set<String> resolvePackagesToScan(Set<String> packagesToScan) {
        Set<String> resolvedPackagesToScan = new LinkedHashSet<String>(packagesToScan.size());
        for (String packageToScan : packagesToScan) {
            if (StringUtils.hasText(packageToScan)) {
                String resolvedPackageToScan = environment.resolvePlaceholders(packageToScan.trim());
                resolvedPackagesToScan.add(resolvedPackageToScan);
            }
        }
        return resolvedPackagesToScan;
    }

    private AbstractBeanDefinition buildServiceBeanDefinition(Annotation serviceAnnotation,
                                                              AnnotationAttributes serviceAnnotationAttributes,
                                                              Class<?> interfaceClass,
                                                              String annotatedServiceBeanName) {
        BeanDefinitionBuilder builder = rootBeanDefinition(ServiceBean.class);
        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
        MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
        String[] ignoreAttributeNames = of("interfaceName");
        propertyValues.addPropertyValues(new AnnotationPropertyValuesAdapter(serviceAnnotation, environment, ignoreAttributeNames));
        // Set interface
        builder.addPropertyValue("interface", interfaceClass.getName());

//        String moduleConfigBeanName = serviceAnnotationAttributes.getString("module");
//        if (StringUtils.hasText(moduleConfigBeanName)) {
//            addPropertyReference(builder, "module", moduleConfigBeanName);
//        }
//
//        String[] registryConfigBeanNames = serviceAnnotationAttributes.getStringArray("registry");
//        List<RuntimeBeanReference> registryRuntimeBeanReferences = toRuntimeBeanReferences(registryConfigBeanNames);
//        if (!registryRuntimeBeanReferences.isEmpty()) {
//            builder.addPropertyValue("registries", registryRuntimeBeanReferences);
//        }
        return builder.getBeanDefinition();
    }

//    private ManagedList<RuntimeBeanReference> toRuntimeBeanReferences(String... beanNames) {
//        ManagedList<RuntimeBeanReference> runtimeBeanReferences = new ManagedList<>();
//        if (!ObjectUtils.isEmpty(beanNames)) {
//            for (String beanName : beanNames) {
//                String resolvedBeanName = environment.resolvePlaceholders(beanName);
//                runtimeBeanReferences.add(new RuntimeBeanReference(resolvedBeanName));
//            }
//        }
//        return runtimeBeanReferences;
//    }
//    private void addPropertyReference(BeanDefinitionBuilder builder, String propertyName, String beanName) {
//        String resolvedBeanName = environment.resolvePlaceholders(beanName);
//        builder.addPropertyReference(propertyName, resolvedBeanName);
//    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public static Class<?> resolveClassName(String className, @Nullable ClassLoader classLoader) throws IllegalArgumentException {
        try {
            return Class.forName(className, false, classLoader);
        } catch (IllegalAccessError var3) {
            throw new IllegalStateException("Readability mismatch in inheritance hierarchy of class [" + className + "]: " + var3.getMessage(), var3);
        } catch (LinkageError var4) {
            throw new IllegalArgumentException("Unresolvable class definition for class [" + className + "]", var4);
        } catch (ClassNotFoundException var5) {
            throw new IllegalArgumentException("Could not find class [" + className + "]", var5);
        }
    }

    public static <T> T[] of(T... values) {
        return values;
    }
}