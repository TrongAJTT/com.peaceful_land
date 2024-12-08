package com.example.peaceful_land.Repository;

import com.example.peaceful_land.Entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    long countByExpirationAfterAndHideEquals(LocalDateTime expiration, Boolean hide);
}
