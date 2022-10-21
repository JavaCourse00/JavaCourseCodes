package io.kimmking.cache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.*;

@SpringBootApplication(scanBasePackages = "io.kimmking.cache")
@MapperScan("io.kimmking.cache.mapper")
@EnableCaching
public class CacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(CacheApplication.class, args);
	}

	@Bean
	@Autowired
	public ApplicationRunner runner(DataSource dataSource) {
		return (x) -> {
			// testInsert(dataSource);
			// 测试 insert into on duplicate key update时如果没有执行更新条件，则回填id为空
		};
	}

	private void testInsert(DataSource dataSource) throws SQLException {
		Connection connection = dataSource.getConnection();

		System.out.println(" =====> test Insert ...");
		PreparedStatement statement = connection.prepareStatement("insert into user(name,age) values(?,20)", Statement.RETURN_GENERATED_KEYS);
		//boolean b = statement.execute("insert into user(name,age) values('K8',20)");
		//System.out.println("insert:" +b);
		// You need to specify Statement.RETURN_GENERATED_KEYS to Statement.executeUpdate(), Statement.executeLargeUpdate() or Connection.prepareStatement().
		statement.setString(1, "K8");
		boolean b = statement.execute();
//		System.out.println("insert:" +b);
		ResultSet rs = statement.getGeneratedKeys();
		System.out.println("insert getGeneratedKeys => " + (rs.next() ? rs.getInt(1): "NULL"));
		rs.close();
		statement.close();


		System.out.println(" =====> test Duplicate ...");
		statement = connection.prepareStatement("insert into user(name,age) values(?,20) on duplicate key update age=age+1", Statement.RETURN_GENERATED_KEYS);
		statement.setString(1, "K8");
		b = statement.execute();
//		System.out.println("insert:" +b);
		rs = statement.getGeneratedKeys();
		System.out.println("insert getGeneratedKeys => " + (rs.next() ? rs.getInt(1): "NULL"));
		rs.close();
		//statement.execute("delete from user where name='K8'");
		statement.close();


		System.out.println(" =====> test Duplicate No Update ...");
		statement = connection.prepareStatement("insert into user(name,age) values(?,20) on duplicate key update age=age", Statement.RETURN_GENERATED_KEYS);
		statement.setString(1, "K8");
		b = statement.execute();
//		System.out.println("insert:" +b);
		rs = statement.getGeneratedKeys();
		System.out.println("insert getGeneratedKeys => " + (rs.next() ? rs.getInt(1): "NULL"));
		rs.close();
		//statement.execute("delete from user where name='K8'");
		statement.close();


		connection.close();
	}


}
