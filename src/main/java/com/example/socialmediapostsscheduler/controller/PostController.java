package com.example.socialmediapostsscheduler.controller;

import com.example.socialmediapostsscheduler.entity.Post;
import com.example.socialmediapostsscheduler.entity.PostStatus;
import com.example.socialmediapostsscheduler.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PostController {
    private final PostService postService;
    @Autowired
    PostController(PostService postService) {
        this.postService = postService;
    }
    @PostMapping("/post")
    public Post createPost(@RequestBody Post post) {
        return postService.savePost(post);
    }

    @GetMapping("/post/{:state}")
    public List<Post> getPostsByState(@PathVariable int state) {
        return postService.getPostsByState(state);
    }

    @GetMapping("/post")
    public List<Post> getPendingPostsBeforeNow() {
        return postService.getPendingPostBeforeNow(PostStatus.IN_PROCESS.getId());
    }

}
