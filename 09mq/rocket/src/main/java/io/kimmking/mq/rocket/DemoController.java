package io.kimmking.mq.rocket;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/demo/send")
    public String send() {



        return "ok";
    }

}
