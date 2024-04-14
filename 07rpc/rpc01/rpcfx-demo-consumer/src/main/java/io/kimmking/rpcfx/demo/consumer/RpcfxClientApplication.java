package io.kimmking.rpcfx.demo.consumer;

import com.alibaba.fastjson.JSON;
import io.kimmking.rpcfx.annotation.RpcfxReference;
import io.kimmking.rpcfx.demo.api.OrderService;
import io.kimmking.rpcfx.demo.api.UserService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"io.kimmking.rpcfx.consumer","io.kimmking.rpcfx.demo.consumer"})
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

	@RpcfxReference
	UserService userService;

	@RpcfxReference
	OrderService orderService;

	@Bean
	public ApplicationRunner runUserService() {
		System.out.println(JSON.toJSONString(userService.hashCode()));
		return x -> System.out.println(JSON.toJSONString(userService.find(500)));
	}

//	@Bean
//	public ApplicationRunner runOrderService() {
//		return x -> System.out.println(JSON.toJSONString(orderService.findOrderById(11)));
//	}

}



