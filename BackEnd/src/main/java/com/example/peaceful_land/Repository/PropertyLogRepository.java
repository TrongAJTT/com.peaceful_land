package com.example.peaceful_land.Repository;

import com.example.peaceful_land.Entity.Property;
import com.example.peaceful_land.Entity.PropertyLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PropertyLogRepository extends JpaRepository<PropertyLog, Long> {
    List<PropertyLog> findAllByPropertyEqualsOrderByDateBeginDesc(Property property);
    Long countByPropertyAndDateBeginAfter(Property property, LocalDateTime date);
}