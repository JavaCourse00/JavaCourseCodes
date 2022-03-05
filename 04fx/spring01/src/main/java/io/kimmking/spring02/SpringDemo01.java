package io.kimmking.spring02;

import io.kimmking.aop.ISchool;
import io.kimmking.spring01.Student;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.Method;

public class SpringDemo01 {
    
    public static void main(String[] args) {

        long s =  System.currentTimeMillis();
        
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
//        Student student123 = context.getBean(Student.class);
        
        Student student123 = (Student) context.getBean("student123");
        System.out.println(student123.toString());

        student123.print();
        
        Student student100 = (Student) context.getBean("student100");
        System.out.println(student100.toString());

        student100.print();


        Klass class1 = context.getBean(Klass.class);
        System.out.println(class1);
        System.out.println("Klass对象AOP代理后的实际类型："+class1.getClass());
        System.out.println("Klass对象AOP代理后的实际类型是否是Klass子类："+(class1 instanceof Klass));

        s =  System.currentTimeMillis();
        class1.dong();
        System.out.println("   *****====> class1.dong() " + (System.currentTimeMillis() - s) + " ms");

        ISchool school = context.getBean(ISchool.class);
        System.out.println(school);
        System.out.println("ISchool接口的对象AOP代理后的实际类型："+school.getClass());




        s =  System.currentTimeMillis();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Demo.class);
        enhancer.setCallback(new MI());
        enhancer.setUseCache(true);
        Demo demo = (Demo) enhancer.create();
        for (int i = 0; i < 1; i++) {
            demo.a1(1);
            demo.a2("hello");
        }
        System.out.println("   *****====> enhancer proxy " + (System.currentTimeMillis() - s) + " ms");




        ISchool school1 = context.getBean(ISchool.class);
        System.out.println(school1);
        System.out.println("ISchool接口的对象AOP代理后的实际类型："+school1.getClass());

        school1.ding();

        class1.dong();
    
        System.out.println("   context.getBeanDefinitionNames() ===>> "+ String.join(",", context.getBeanDefinitionNames()));

        Student s101 = (Student) context.getBean("s101");
        if (s101 != null) {
            System.out.println(s101);
        }
        Student s102 = (Student) context.getBean("s102");
        if (s102 != null) {
            System.out.println(s102);
        }
    }

    static class MI implements MethodInterceptor {
        @Override
        public Object intercept(Object obj, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            long s =  System.currentTimeMillis();
            System.out.println("   *****====> " + s + " " +"Before:"+method.getName());
            Object result= methodProxy.invokeSuper(obj, objects);
            System.out.println("   *****====> " + (System.currentTimeMillis() - s) + " ms  After:"+method.getName());
            return result;
        }
    }

    static class Demo {
        public void a1(int i) {
            System.out.println("   *****====> Demo a1, " + i);
        }

        public void a2(String str) {
            System.out.println("   *****====> Demo a2," + str);
        }
    }

}
