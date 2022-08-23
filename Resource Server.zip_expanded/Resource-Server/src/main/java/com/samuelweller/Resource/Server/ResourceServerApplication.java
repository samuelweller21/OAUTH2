package com.samuelweller.Resource.Server;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

//docker run --detach --env MYSQL_ROOT_PASSWORD=sweller --env MYSQL_USER=sweller --env MYSQL_PASSWORD=sweller --env MYSQL_DATABASE=test --name mysql --publish 3306:3306 mysql:5.7
//GRANT ALL PRIVILEGES ON *.* TO 'sweller'@'localhost';
//FLUSH PRIVILEGES;

//@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@SpringBootApplication()
public class ResourceServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResourceServerApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner demo(DocumentRepository docRepo) {
		return (args) -> {
			
			docRepo.save(new Document(10001L,"HR Manual","01/01/2019","OFFICIAL","HR"));
	    	docRepo.save(new Document(10002L,"Financial Report","01/01/2020","OFFICIAL SENSITIVE","Finance"));
	    	docRepo.save(new Document(10003L,"Board Minutes","01/01/2021","SECRET","Chairman's Office"));
	    	docRepo.save(new Document(10004L,"Competitor profile","27/07/2022","TOP SECRET","Dave Davidson"));
	    	
	    	docRepo.findAll().forEach(doc -> System.out.println(doc.toString()));
		  };
	}

}
