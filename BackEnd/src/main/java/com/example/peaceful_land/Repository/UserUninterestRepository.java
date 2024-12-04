package com.example.peaceful_land.Repository;

import com.example.peaceful_land.Entity.UserId;
import com.example.peaceful_land.Entity.UserUninterest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserUninterestRepository extends JpaRepository<UserUninterest, Long> {
}
