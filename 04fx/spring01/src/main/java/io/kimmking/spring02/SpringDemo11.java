package io.kimmking.spring02;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/1/22 18:01
 */
public class SpringDemo11 {

    public static void main(String[] args) {
        long s =  System.currentTimeMillis();
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{IAction.class});
        enhancer.setCallback(new MI());
        enhancer.setUseCache(true);
        IAction demo = (IAction) enhancer.create();
        for (int i = 0; i < 5; i++) {
            long ss =  System.currentTimeMillis();
            System.out.println(demo.action());
            System.out.println( i + "   *****====> invoke proxy " + (System.currentTimeMillis() - ss) + " ms");
        }
        System.out.println("   *****====> enhancer proxy " + (System.currentTimeMillis() - s) + " ms");

    }

    public interface IAction {
        Object action();
    }


    static class MI implements MethodInterceptor {
        @Override
        public Object intercept(Object obj, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            long s =  System.currentTimeMillis();
            System.out.println("   *****==MI==> " + s + " " +"Before:"+method.getName());
            Object result = "S-" + s;//methodProxy.invokeSuper(obj, objects);
            System.out.println("   *****==MI==> " + (System.currentTimeMillis() - s) + " ms  After:"+method.getName());
            return result;
        }
    }

}
