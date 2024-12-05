package com.example.peaceful_land.Repository;

import com.example.peaceful_land.Entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<Property, Long> {
}
