package com.project.sns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages={"com.project.sns", "controller","service","model", "repository"})
@ComponentScan(basePackages = {"com.project.sns"})
@EnableJpaRepositories("com.project.sns.repository")
@EntityScan(basePackages = {"com.project.sns.*"})
public class SnsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SnsApplication.class, args);
	}

}
