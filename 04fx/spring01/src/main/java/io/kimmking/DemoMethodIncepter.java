package io.kimmking;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class DemoMethodIncepter implements MethodInterceptor {

    public Object invoke(MethodInvocation invocation) throws Throwable {

        long s =  System.currentTimeMillis();
        System.out.println("   *****====> " + s + " " + invocation.getMethod().getName());
        Object result = invocation.proceed();
        System.out.println("   *****====> " + (System.currentTimeMillis() - s) + " ms");
        return result;
    }

}
