package com.landowner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;

@SpringBootApplication
public class LandownerApplication {

	public static void main(String[] args) {
		//SpringApplication.run(LandownerApplication.class, args);
		SpringApplication application = new SpringApplication(LandownerApplication.class);
		application.addListeners((ApplicationListener<ApplicationStartingEvent>) event ->{
			System.out.println("=============================ApplicationStartingEvent=============================");
		});
		application.addListeners((ApplicationListener<ApplicationPreparedEvent>) event ->{
			System.out.println("=============================ApplicationPreparedEvent=============================");
		});
		application.addListeners((ApplicationListener<ApplicationReadyEvent>) event ->{
			System.out.println("=============================ApplicationReadyEvent=============================");
		});
		application.run(args);
	}
}
