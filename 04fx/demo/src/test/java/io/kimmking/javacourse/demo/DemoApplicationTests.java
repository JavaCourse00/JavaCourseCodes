package io.kimmking.javacourse.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.PropertySource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DemoApplicationTests {
	@Test
	void testDemoConfig() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.register(TestConfig.class);
		context.refresh();

		System.out.println(Arrays.toString(context.getBeanNamesForType(DemoConfig.class)));
		assertEquals(3, context.getBeansOfType(DemoConfig.class).size());

		DemoConfig demoConfig = context.getBean("a1", DemoConfig.class);
		System.out.println("a1=" + demoConfig.toString());
		assertEquals("d1", demoConfig.getDemoName());
		assertEquals("demo1", demoConfig.getDemoDesc());

		demoConfig = context.getBean("a2", DemoConfig.class);
		System.out.println("a2=" + demoConfig.toString());
		assertEquals("d2", demoConfig.getDemoName());
		assertEquals("demo2", demoConfig.getDemoDesc());

		demoConfig = context.getBean("a3", DemoConfig.class);
		System.out.println("a3=" + demoConfig.toString());
		assertEquals("d3", demoConfig.getDemoName());
		assertEquals("demo3", demoConfig.getDemoDesc());

	}

	@EnableDemoConfigBindings(prefix = "demo.config", type = DemoConfig.class)
	@PropertySource("application.properties")
	private static class TestConfig {

	}

}
