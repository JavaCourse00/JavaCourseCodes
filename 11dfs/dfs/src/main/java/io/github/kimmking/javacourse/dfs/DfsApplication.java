package io.github.kimmking.javacourse.dfs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("io.github.kimmking.javacourse.dfs.mapper")
@SpringBootApplication
public class DfsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DfsApplication.class, args);
	}

}
