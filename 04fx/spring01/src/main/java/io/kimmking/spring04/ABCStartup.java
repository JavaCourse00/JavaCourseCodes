package io.kimmking.spring04;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.List;

@Component
public class ABCStartup {

    @Autowired
    private List<ABCPlugin> services;

    public void call () {
        services.forEach(System.out::println);
    }

    @PreDestroy
    public void destroy() {

        System.out.println(Thread.currentThread().getName()+"-destroy:" + this.getClass().getSimpleName());

    }

}
