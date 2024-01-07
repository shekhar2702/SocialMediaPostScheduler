package com.example.socialmediapostsscheduler.publisher;

import com.example.socialmediapostsscheduler.entity.Post;
import com.example.socialmediapostsscheduler.entity.PostStatus;
import com.example.socialmediapostsscheduler.service.PostService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;

@Component
public class PostsScheduler {

    private final PostService postService;
    private final RabbitTemplate rabbitTemplate;
    @Value("${publisher.pool.count}")
    private int poolCount;
    @Value("${publisher.pool.alive.timeout}")
    private int poolAliveTime;
    @Value("${publisher.pool.queue.size}")
    private int poolQueueSize;

    @Autowired
    PostsScheduler(PostService postService, RabbitTemplate rabbitTemplate) {
        this.postService = postService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(fixedDelay = 60000L)
    @SuppressWarnings("all")
    private void publishPendingPostsToQueue() {

        List<Post> postsToPublish = this.postService.getPendingPostBeforeNow(PostStatus.IN_PROCESS.getId());
        System.out.println("Number of pending posts are: " + postsToPublish.size());
        ExecutorService executorService = new ThreadPoolExecutor(poolCount - 2, poolCount, poolAliveTime
                , TimeUnit.SECONDS, new ArrayBlockingQueue<>(poolQueueSize),
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

        postsToPublish.forEach(p -> {
                executorService.submit(()->{
                    System.out.println("Publishing post to queue with postId: " + p.getId());
                    try {
                        p.setStatus(PostStatus.IN_FLIGHT.getId());
                        rabbitTemplate.convertAndSend("","q.postsQueue",p);
                        if(postService.updatePostStatus(p)) {
                            System.out.println("Posts updated sucessfully to in_flight,postId: " + p.getId());
                        }
                        else {
                            System.out.println("Failed updating posts to in_flight status with postId: " + p.getId());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        });
    }
}
