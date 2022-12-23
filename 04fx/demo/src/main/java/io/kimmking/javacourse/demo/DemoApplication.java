package io.kimmking.javacourse.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

@SpringBootApplication
public class DemoApplication implements EnvironmentAware, ApplicationRunner {

	Environment environment;

	@Resource(name="a1")
	DemoConfig a1;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Autowired
	ApplicationContext context;

	@Bean
	//@ConditionalOnMissingBean
	public ConsConfig defaultConsConfig() {
		ConsConfig cf = new ConsConfig();
		cf.setDemoName("defaultName");
		cf.setDemoDesc("defaultDesc");
		return cf;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println(environment.getProperty("a"));
		System.out.println(a1.getDemoName());

		DemoConfig a4 = (DemoConfig) context.getBean("a4");
		System.out.println(a4.getDemoName());

		System.out.println(context.getBean(ConsConfigs.class));

	}

	@EnableDemoConfigBindings(prefix = "demo.config", type = DemoConfig.class)
	@PropertySource("application.properties")
	public static class TestDemoConfig {

	}

	@Bean("a4")
	@ConditionalOnClass(name="A")
	DemoConfig createA4() {
		DemoConfig config = new DemoConfig();
		config.setDemoName("a4");
		config.setDemoDesc("this is a4");
		return config;
	}
}
