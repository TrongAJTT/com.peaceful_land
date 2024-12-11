package com.example.peaceful_land.Repository;

import com.example.peaceful_land.Entity.Property;
import com.example.peaceful_land.Entity.PropertyImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PropertyImageRepository extends JpaRepository<PropertyImage, Long> {
    boolean existsByFileUrl(String fileUrl);
    List<PropertyImage> findAllByPropertyEquals(Property property);
    Optional<PropertyImage> findByFileUrl(String fileUrl);

    @Query("SELECT p.fileUrl FROM PropertyImage p")
    List<String> findAllThumbnails();
}
