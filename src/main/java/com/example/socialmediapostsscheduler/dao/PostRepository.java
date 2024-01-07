package com.example.socialmediapostsscheduler.dao;

import com.example.socialmediapostsscheduler.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
//    List<Post> findByPendingTrue();
//    List<Post> findByPending(Boolean condition);
//    @Query("SELECT p FROM Post p WHERE p.pending = true AND p.scheduledAt < ?1")
//    List<Post> findPendingPostsBeforeDate(Date date);

    //alternative to above
//    List<Post> findByPendingTrueAndScheduledAtBefore(Date date);
    List<Post> findByStatusAndScheduledAtBefore(int id, LocalDateTime date);
    List<Post> findByStatus(int id);
}
