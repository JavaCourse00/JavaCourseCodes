package io.kimmking.dtx01;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(scanBasePackages = "io.kimmking.dtx01")
@MapperScan("io.kimmking.dtx01.mapper")
@EnableCaching
public class Dtx01Application {

	public static void main(String[] args) {
		SpringApplication.run(Dtx01Application.class, args);
	}

}
