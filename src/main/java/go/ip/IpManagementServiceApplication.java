package go.ip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.sql.SQLException;
import java.util.logging.Logger;


@SpringBootApplication
public class IpManagementServiceApplication {

	public static final Logger logger = Logger.getLogger(String.valueOf(IpManagementServiceApplication.class));

	public static void main(String[] args) {
		SpringApplication.run(IpManagementServiceApplication.class, args);
	}

	@Bean(name = "dataSource")
	public DriverManagerDataSource dataSource() throws SQLException {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setUrl("jdbc:h2:mem:ipDB;");
		dataSource.setUsername("sa");
		dataSource.setPassword("");

		Resource initSchema = new ClassPathResource("scripts/IP_POOL.sql");
		Resource initData = new ClassPathResource("scripts/IP_POOL_DATA.sql");
		DatabasePopulator databasePopulator = new ResourceDatabasePopulator(initSchema, initData);
		DatabasePopulatorUtils.execute(databasePopulator, dataSource);
		logger.info("IP_POOL table has been initilized");
		return dataSource;
	}
}
