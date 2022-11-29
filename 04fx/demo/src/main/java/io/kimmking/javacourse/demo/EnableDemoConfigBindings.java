package io.kimmking.javacourse.demo;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2022/11/29 16:10
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(DemoConfigBindingsRegistrar.class)
public @interface EnableDemoConfigBindings {
    /**
     * The name prefix of the properties that are valid to bind.
     *
     * @return the name prefix of the properties to bind
     */
    String prefix();

    /**
     * @return The binding type.
     */
    Class<? extends DemoConfig> type();

    /**
     * It indicates whether {@link #prefix()} binding to multiple Spring Beans.
     *
     * @return the default value is <code>true</code>
     */
    boolean multiple() default true;
}
