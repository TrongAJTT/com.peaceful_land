package com.example.peaceful_land.Repository;

import com.example.peaceful_land.Entity.Post;
import com.example.peaceful_land.Entity.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    boolean existsByProperty(Property property);
    Page<Post> findAllByHideEquals(boolean hide, PageRequest pageRequest);
    Post findByProperty(Property property);
}
