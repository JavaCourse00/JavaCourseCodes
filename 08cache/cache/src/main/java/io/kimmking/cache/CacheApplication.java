package io.kimmking.cache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(scanBasePackages = "io.kimmking.cache")
@MapperScan("io.kimmking.cache.mapper")
@EnableCaching
public class CacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(CacheApplication.class, args);
	}

}
