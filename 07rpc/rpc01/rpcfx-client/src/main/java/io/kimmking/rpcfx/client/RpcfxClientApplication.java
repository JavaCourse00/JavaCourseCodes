package io.kimmking.rpcfx.client;

import com.alibaba.fastjson.parser.ParserConfig;
import io.kimmking.rpcfx.api.User;
import io.kimmking.rpcfx.api.UserService;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RpcfxClientApplication {

	static {
		ParserConfig.getGlobalInstance().addAccept("io.kimmking");
	}

	public static void main(String[] args) {

		UserService service = Rpcfx.create(UserService.class, "http://localhost:8080/");
		User user = service.findById(1);
		System.out.println("find user id=1 from server: " + user.getName());

//		SpringApplication.run(RpcfxClientApplication.class, args);
	}

}
