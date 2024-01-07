package com.example.socialmediapostsscheduler.service;

import com.example.socialmediapostsscheduler.dao.PostRepository;
import com.example.socialmediapostsscheduler.entity.Post;
import com.example.socialmediapostsscheduler.entity.PostStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    @Autowired
    PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @SuppressWarnings("all")
    public Post savePost(Post post) {
        try {
            post.setStatus(PostStatus.IN_PROCESS.getId());
            return postRepository.save(post);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Post> getPostsByState(int state) {
        return postRepository.findByStatus(state);
    }

    public List<Post> getPendingPostBeforeNow(int id) {
        return postRepository.findByStatusAndScheduledAtBefore(id,LocalDateTime.now());
    }

    @SuppressWarnings("all")
    public Boolean updatePostStatus(Post post) {
        try {
            postRepository.save(post);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
