package io.kimmking.rpcfx.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.kimmking.rpcfx.api.*;
import io.kimmking.rpcfx.demo.api.OrderService;
import io.kimmking.rpcfx.demo.api.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

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

		RpcfxResponse response = new RpcfxResponse();
		String serviceClass = request.getServiceClass();

		// 作业1：改成泛型和反射
		Object service = this.applicationContext.getBean(serviceClass);

		try {
			Method method = resolveMethodFromClass(service.getClass(), request.getMethod());
			Object result = method.invoke(service, request.getParams()); // dubbo, fastjson,
			// 两次json序列化能否合并成一个
			response.setResult(JSON.toJSONString(result, SerializerFeature.WriteClassName));
			response.setStatus(true);
			return response;
		} catch ( IllegalAccessException | InvocationTargetException e) {

			// 3.Xstream

			// 2.封装一个统一的RpcfxException
			// 客户端也需要判断异常
			e.printStackTrace();
			response.setException(e);
			response.setStatus(false);
			return response;
		}
	}

	private Method resolveMethodFromClass(Class<?> klass, String methodName) {
		return Arrays.stream(klass.getMethods()).filter(m -> methodName.equals(m.getName())).findFirst().get();
	}

	// 能否去掉name
	//
	@Bean(name = "io.kimmking.rpcfx.api.UserService")
	public UserService createUserService(){
		return new UserServiceImpl();
	}

	@Bean(name = "io.kimmking.rpcfx.api.OrderService")
	public OrderService createOrderService(){
		return new OrderServiceImpl();
	}

}
