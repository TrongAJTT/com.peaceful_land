package com.example.peaceful_land;

import com.example.peaceful_land.Entity.Account;
import com.example.peaceful_land.Service.AccountService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
//		SpringApplication.run(Application.class, args);
		ApplicationContext applicationContext = SpringApplication.run(Application.class, args);
		AccountService accountService = applicationContext.getBean(AccountService.class);
		accountService.encodeOldPassword();
	}
}
