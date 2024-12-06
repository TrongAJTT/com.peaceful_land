package com.example.peaceful_land.Repository;

import com.example.peaceful_land.Entity.Account;
import com.example.peaceful_land.Entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    long countByUserEquals(Account account);
    long countByDateBeginBetweenAndUserEquals(LocalDateTime dateBegin, LocalDateTime dateEnd, Account account);
}
