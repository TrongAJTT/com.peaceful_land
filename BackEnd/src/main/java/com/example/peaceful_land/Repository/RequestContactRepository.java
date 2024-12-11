package com.example.peaceful_land.Repository;

import com.example.peaceful_land.Entity.Account;
import com.example.peaceful_land.Entity.Property;
import com.example.peaceful_land.Entity.RequestContact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RequestContactRepository extends JpaRepository<RequestContact, Long> {
    boolean existsByPropertyAndUserAndDateBeginAfter(Property property, Account account, LocalDateTime dateTime);
    List<RequestContact> findByPropertyEqualsOrderByIdDesc(Property property);
    Long countByPropertyAndDateBeginAfter(Property property, LocalDateTime dateTime);
}
