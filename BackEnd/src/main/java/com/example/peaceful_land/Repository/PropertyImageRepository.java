package com.example.peaceful_land.Repository;

import com.example.peaceful_land.Entity.PropertyImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyImageRepository extends JpaRepository<PropertyImage, Long> {
    boolean existsByFileUrl(String fileUrl);
}
