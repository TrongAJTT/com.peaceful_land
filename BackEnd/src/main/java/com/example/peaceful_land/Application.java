package com.example.peaceful_land;

import com.example.peaceful_land.Entity.Account;
import com.example.peaceful_land.Repository.AccountRepository;
import com.example.peaceful_land.Service.EmailService;
import com.example.peaceful_land.Utils.HibernateUtils;
import org.hibernate.Session;
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
		accountRepository.save(new Account(1L, (byte) 1, "axample@example.com", "123456", 10000, "Huy", null, "0123456", true, "null", true, "meta", false, 0, LocalDateTime.now())  );;

	}

}
