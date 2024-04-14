package io.kimmking.rpcfx.demo.provider;

import com.alibaba.fastjson.JSON;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.provider.ProviderBootstrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
@ComponentScan({"io.kimmking.rpcfx.provider", "io.kimmking.rpcfx.demo.provider"})
public class RpcfxServerApplication implements CommandLineRunner {

	@Autowired
	ProviderBootstrap bootstrap;

	public static void main(String[] args) {
		SpringApplication.run(RpcfxServerApplication.class, args);
	}


	@PostMapping("/")
	public RpcfxResponse invoke(@RequestBody RpcfxRequest request) {
		return bootstrap.getInvoker().invoke(request);
	}

	@GetMapping("/api/hello")
	public RpcfxResponse invoke() {
		RpcfxRequest request = new RpcfxRequest();
		request.setServiceClass("io.kimmking.rpcfx.demo.api.UserService");
		request.setParams(new Object[]{1});
		request.setMethodSign("findById@1_int,");
		return bootstrap.getInvoker().invoke(request);
	}

	@Override
	public void run(String... args) {
		RpcfxResponse response = invoke();
		System.out.println(JSON.toJSONString(response));
	}

}
