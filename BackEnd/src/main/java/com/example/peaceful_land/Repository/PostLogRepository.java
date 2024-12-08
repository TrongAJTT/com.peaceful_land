package com.example.peaceful_land.Repository;

import com.example.peaceful_land.Entity.Post;
import com.example.peaceful_land.Entity.PostLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface PostLogRepository extends JpaRepository<PostLog, Long> {
    PostLog findTopByPostEqualsOrderByDateBeginDesc(Post post);
    boolean existsByThumbnUrl(String thumbnUrl);
}
