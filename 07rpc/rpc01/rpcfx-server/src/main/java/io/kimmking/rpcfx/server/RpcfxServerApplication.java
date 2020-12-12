package io.kimmking.rpcfx.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.api.User;
import io.kimmking.rpcfx.api.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class RpcfxServerApplication implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public static void main(String[] args) {
		SpringApplication.run(RpcfxServerApplication.class, args);
	}

	@PostMapping("/")
	public RpcfxResponse call(@RequestBody RpcfxRequest request) {

		String serviceClass = request.getServiceClass();
		// 作业1：改成泛型和反射
		UserService service = (UserService) this.applicationContext.getBean(serviceClass);
		int id = Integer.parseInt(request.getParams()[0].toString());
		Object result = service.findById(id);
		RpcfxResponse response = new RpcfxResponse();
		response.setResult(JSON.toJSONString(result, SerializerFeature.WriteClassName));
		response.setStatus(true);
		return response;
	}

	@Bean(name = "io.kimmking.rpcfx.api.UserService")
	public UserService create(){
		return new UserServiceImpl();
	}

}
