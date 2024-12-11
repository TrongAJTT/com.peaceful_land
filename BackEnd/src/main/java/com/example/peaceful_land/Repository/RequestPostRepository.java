package com.example.peaceful_land.Repository;

import com.example.peaceful_land.Entity.Post;
import com.example.peaceful_land.Entity.RequestPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RequestPostRepository extends JpaRepository<RequestPost, Long> {
    List<RequestPost> findAllByOrderByIdDesc();
    List<RequestPost> findAllByExpirationBeforeAndApproved(LocalDate expiration, Boolean approved);
    List<RequestPost> findAllByApprovedEqualsOrderByExpirationDesc(Boolean approved);
    RequestPost findByPostEquals(Post post);
}