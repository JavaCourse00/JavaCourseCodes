package io.kimmking.rpcfx.stub;

import io.kimmking.rpcfx.utils.MockUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/2/11 02:57
 */
public class MockHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> type = method.getReturnType();
        System.out.println("invoke by mock handler...");
        return MockUtils.mock(type, null);
    }

    public static <T> T createMock(Class<T> serviceClass) {
        //final ServiceMeta sm, Router router, LoadBalancer loadBalance, Filter filter) {
        return (T) Proxy.newProxyInstance(MockHandler.class.getClassLoader(),
                new Class[]{serviceClass}, new MockHandler());

    }
}
