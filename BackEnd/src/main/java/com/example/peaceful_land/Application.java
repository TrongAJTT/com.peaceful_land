package com.example.peaceful_land;

import com.example.peaceful_land.Config.ScheduledTasks;
import com.example.peaceful_land.Service.AccountService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Application {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(Application.class, args);

		AccountService accountService = applicationContext.getBean(AccountService.class);
		accountService.encodeOldPassword();

		ScheduledTasks scheduledTasks = applicationContext.getBean(ScheduledTasks.class);
		scheduledTasks.processDataAtStartOfDay();
		scheduledTasks.deleteUnusedImages();
	}
}
