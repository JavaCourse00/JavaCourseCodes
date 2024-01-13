package io.kimmking.rpcfx.demo.consumer;

import com.alibaba.fastjson.JSON;
import io.kimmking.rpcfx.annotation.RpcfxReference;
import io.kimmking.rpcfx.api.Filter;
import io.kimmking.rpcfx.api.LoadBalancer;
import io.kimmking.rpcfx.api.Router;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.consumer.RpcfxInvoker;
import io.kimmking.rpcfx.demo.api.User;
import io.kimmking.rpcfx.demo.api.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

@SpringBootApplication
@ComponentScan("io.kimmking.rpcfx")
public class RpcfxClientApplication {

	public static void main(String[] args) {

		// UserService service = new xxx();
		// service.findById

//		UserService userService = Rpcfx.create(UserService.class, "http://localhost:8080/");
//		User user = userService.findById(1);
//		System.out.println("find user id=1 from server: " + user.getName());
//
//		OrderService orderService = Rpcfx.create(OrderService.class, "http://localhost:8080/");
//		Order order = orderService.findOrderById(1992129);
//		System.out.println(String.format("find order name=%s, amount=%f",order.getName(),order.getAmount()));

//		UserService userService2 = Rpcfx.createFromRegistry(UserService.class, "localhost:2181", new TagRouter(), new RandomLoadBalancer(), new CuicuiFilter());
//		User user = userService2.findById(1);
//		System.out.println(user.getName());

		SpringApplication.run(RpcfxClientApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		// userService2 = RpcfxInvoker.createFromRegistry(UserService.class, new TagRouter(), new RandomLoadBalancer(), new CuicuiFilter());
//		User user = userService2.findById(1);
//		System.out.println(JSON.toJSONString(user));
//	}

}



