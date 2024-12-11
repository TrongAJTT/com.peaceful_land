package com.example.peaceful_land.Config;

import com.example.peaceful_land.Service.AccountService;
import com.example.peaceful_land.Service.PropertyService;
import com.example.peaceful_land.Service.UserRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component @RequiredArgsConstructor
public class ScheduledTasks {

    private final AccountService accountService;
    private final UserRequestService userRequestService;
    private final PropertyService propertyService;

    @Scheduled(cron = "0 1 0 * * ?", zone = "Asia/Ho_Chi_Minh")
    public void processDataAtStartOfDay() {
        System.out.println("BACK UP SERVER DATA");
        accountService.SYSTEM_scanAndResetRoleIfExpired();
        userRequestService.SYSTEM_scanForExpiredPostRequests();
    }

    @Scheduled(cron = "5 1 0 ? * 2,4,6", zone = "Asia/Ho_Chi_Minh")
    public void deleteUnusedImages() {
        accountService.SYSTEM_scanAndDeleteUnusedAvatar();
        propertyService.SYSTEM_scanAndDeleteUnusedImages();
    }

}
