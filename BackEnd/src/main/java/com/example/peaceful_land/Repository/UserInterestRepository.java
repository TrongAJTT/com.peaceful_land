package com.example.peaceful_land.Repository;

import com.example.peaceful_land.Entity.Account;
import com.example.peaceful_land.Entity.Property;
import com.example.peaceful_land.Entity.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {
    Optional<UserInterest> findByUserEqualsAndPropertyEquals(Account account, Property property);
    List<UserInterest> findByPropertyEqualsAndNotificationEquals(Property property, Boolean notification);
}
