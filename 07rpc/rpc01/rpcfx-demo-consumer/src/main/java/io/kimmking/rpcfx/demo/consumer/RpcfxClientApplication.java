package io.kimmking.rpcfx.demo.consumer;

import io.kimmking.rpcfx.api.*;
import io.kimmking.rpcfx.client.Rpcfx;
import io.kimmking.rpcfx.demo.api.Order;
import io.kimmking.rpcfx.demo.api.OrderService;
import io.kimmking.rpcfx.demo.api.User;
import io.kimmking.rpcfx.demo.api.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
@RestController
//@ComponentScan("io.kimmking.rpcfx")
public class RpcfxClientApplication implements CommandLineRunner {

	// 二方库
	// 三方库 lib
	// nexus, userserivce -> userdao -> user
	//

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

	UserService userService2;// = Rpcfx.createFromRegistry(UserService.class, "localhost:2181", new TagRouter(), new RandomLoadBalancer(), new CuicuiFilter());

	@GetMapping("/api/hello")
	public User invoke() {
		return userService2.findById(100);
	}

	@Override
	public void run(String... args) throws Exception {
		userService2 = Rpcfx.createFromRegistry(UserService.class, "localhost:2181", new TagRouter(), new RandomLoadBalancer(), new CuicuiFilter());
		User user = userService2.findById(1);
		System.out.println(user.getName());
	}

	private static class TagRouter implements Router {
		@Override
		public List<String> route(List<String> urls) {
			return urls;
		}
	}

	private static class RandomLoadBalancer implements LoadBalancer {
		private final Random random = new Random();
		@Override
		public String select(List<String> urls) {
			if(urls.isEmpty()) return null;
			return urls.get(random.nextInt(urls.size()));
		}
	}

	@Slf4j
	private static class CuicuiFilter implements Filter {
		@Override
		public boolean filter(RpcfxRequest request) {
			//log.info("filter {} -> {}", this.getClass().getName(), request.toString());
			//System.out.printf("filter %s -> %s%n", this.getClass().getName(), request.toString());
			return true;
		}
	}
}



