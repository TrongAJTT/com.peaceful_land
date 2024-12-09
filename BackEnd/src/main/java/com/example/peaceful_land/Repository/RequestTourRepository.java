package com.example.peaceful_land.Repository;

import com.example.peaceful_land.Entity.Property;
import com.example.peaceful_land.Entity.RequestTour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface RequestTourRepository extends JpaRepository<RequestTour, Long> {
    boolean existsByPropertyAndDateBeginAfter(Property property, LocalDateTime date);
}
