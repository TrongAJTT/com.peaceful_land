package com.example.peaceful_land;

import com.example.peaceful_land.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class Application {

	@Autowired
	private EmailService emailDemo;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@EventListener(org.springframework.boot.context.event.ApplicationReadyEvent.class)
	public void sendMail(){
//		emailDemo.sendEmailConfirmation("abc@example.com", "123456");
	}

}
