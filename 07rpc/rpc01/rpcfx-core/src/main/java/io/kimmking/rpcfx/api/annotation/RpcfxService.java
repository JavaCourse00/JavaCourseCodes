package io.kimmking.rpcfx.api.annotation;

import io.kimmking.rpcfx.api.Filter;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface RpcfxService {

    /**
     * Interface class name, default value is empty string
     */
    String interfaceName() default "";

    /**
     * Service version, default value is empty string
     */
    String version() default "";

    /**
     * Service group, default value is empty string
     */
    String group() default "";

    /**
     * Service mock name, use interface name + Mock if not set
     */
    String mock() default "";

    /**
     * Timeout value for service invocation, default value is 0
     */

    int timeout() default 0;

    /**
     * Filters for service invocation
     *
     * @see Filter
     */
    String[] filter() default {};

    /**
     * Service owner name
     */
    String owner() default "";

    /**
     * Application spring bean name
     */
    String application() default "";

    /**
     * Module spring bean name
     */
    String module() default "";

    /**
     * Service tag name
     */
    String tag() default "";
}
