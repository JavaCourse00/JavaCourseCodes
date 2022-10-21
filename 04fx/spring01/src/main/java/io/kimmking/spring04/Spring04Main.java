package io.kimmking.spring04;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Spring04Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Spring04Main.class.getPackage().getName());
        ABCStartup abc = context.getBean(ABCStartup.class);
        abc.call();

        context.publishEvent(new ApplicationEvent("myEvent") {});

        context.close();
        // context.destroy();
        // context.stop(); // 这3个有什么区别？

    }

}
