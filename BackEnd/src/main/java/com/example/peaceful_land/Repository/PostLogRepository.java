package com.example.peaceful_land.Repository;

import com.example.peaceful_land.Entity.Post;
import com.example.peaceful_land.Entity.PostLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostLogRepository extends JpaRepository<PostLog, Long> {
    List<PostLog> findByThumbnUrl(String thumbnUrl);
    PostLog findTopByPostEqualsOrderByDateBeginDesc(Post post);
    boolean existsByThumbnUrl(String thumbnUrl);
    List<PostLog> findAllByPostEqualsOrderByDateBeginDesc(Post post);

    @Query("SELECT p.thumbnUrl FROM PostLog p")
    List<String> findAllThumbnails();

}
