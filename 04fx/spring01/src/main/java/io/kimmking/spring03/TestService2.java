package io.kimmking.spring03;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
public class TestService2 {

    @Autowired
    private TestService1 service1;

    public void test2() {
        System.out.println("test2");
    }

}
