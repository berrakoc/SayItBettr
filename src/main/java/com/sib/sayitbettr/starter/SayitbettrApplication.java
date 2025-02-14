package com.sib.sayitbettr.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EntityScan(basePackages = {"com.sib"}) //it doesn't work if you don't write this
@ComponentScan(basePackages = {"com.sib"}) //restcontroller srvice gibi anatosyonalrın beanlerinin oluşması için tanımladık
@EnableJpaRepositories(basePackages = {"com.sib"} )
public class SayitbettrApplication {

	public static void main(String[] args) {
		SpringApplication.run(SayitbettrApplication.class, args);
	}

}
