package io.kimmking.spring03;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Spring03Main {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Spring03Main.class.getPackage().getName());
        TestService1 testService1 = context.getBean(TestService1.class);
        testService1.test1();
    }

}
