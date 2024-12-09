package com.example.peaceful_land.Repository;

import com.example.peaceful_land.Entity.Property;
import com.example.peaceful_land.Entity.RequestContact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface RequestContactRepository extends JpaRepository<RequestContact, Long> {
    boolean existsByPropertyAndDateBeginBefore(Property property, LocalDateTime dateTime);
}
