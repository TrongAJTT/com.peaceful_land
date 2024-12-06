package com.example.peaceful_land.Repository;

import com.example.peaceful_land.Entity.Post;
import com.example.peaceful_land.Entity.RequestPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestPostRepository extends JpaRepository<RequestPost, Long> {
    List<RequestPost> findAllByApprovedEquals(Boolean approved);
    RequestPost findByPostEquals(Post post);
}