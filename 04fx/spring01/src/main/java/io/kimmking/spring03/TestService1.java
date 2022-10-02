package io.kimmking.spring03;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@Data
@EnableAsync
public class TestService1 { // TODO rename this class to TestService6 and it works well.

    @Autowired
    private TestService2 service2;

    @Async
    public void test1() {
        System.out.println("test1");
        System.out.println(this.getClass().getCanonicalName());
        System.out.println(Thread.currentThread().getName());
    }

}
