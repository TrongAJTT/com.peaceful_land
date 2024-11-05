package com.example.peaceful_land;

import com.example.peaceful_land.Model.Account;
import com.example.peaceful_land.Repository.AccountRepository;
import com.example.peaceful_land.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;

import java.time.LocalDateTime;

@SpringBootApplication
public class Application {

	@Autowired
	private EmailService emailDemo;
	@Autowired
	AccountRepository accountRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@EventListener(org.springframework.boot.context.event.ApplicationReadyEvent.class)
	public void sendMail(){
//		emailDemo.sendEmailConfirmation("abc@example.com", "123456");


	}

}
