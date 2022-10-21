package io.kimmking.spring04;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ABCStartupListener implements ApplicationListener<ApplicationEvent> {

    @Autowired
    private List<ABCPlugin> services;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {

        System.out.println(event);

        // 如果用spring boot可以怎么改进
        if (event instanceof ContextStartedEvent || event instanceof ContextRefreshedEvent) {
            services.forEach(x -> {
                try {
                    x.startup();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        if (event instanceof ContextClosedEvent | event instanceof ContextStoppedEvent) {
            services.forEach(x -> {
                try {
                    x.shutdown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
