package com.example.socialmediapostsscheduler.consumer;

import com.example.socialmediapostsscheduler.entity.Post;
import com.example.socialmediapostsscheduler.entity.PostStatus;
import com.example.socialmediapostsscheduler.entity.TwitterRequestPayload;
import com.example.socialmediapostsscheduler.service.PostService;
import com.example.socialmediapostsscheduler.utility.TwitterApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuth2Token;
import twitter4j.conf.ConfigurationBuilder;

import java.io.IOException;
import java.util.concurrent.ExecutionException;


@Service
public class TwitterPostTool implements SocialMediaPosterTool {
    @Value("${twitter4j.oauth.consumerKey}")
    private String consumerKey;

    @Value("${twitter4j.oauth.consumerSecret}")
    private String consumerSecret;

    @Value("${twitter4j.oauth.accessToken}")
    private String accessToken;

    @Value("${twitter4j.oauth.accessTokenSecret}")
    private String accessTokenSecret;

    @Value("${twitter.bearer.token}")
    private String bearerToken;

    private final PostService postService;

    private final RabbitTemplate rabbitTemplate;
    private final WebClient webClient;

    private final String baseUrl = "https://api.twitter.com/2";

    @Autowired
    TwitterPostTool(PostService postService, RabbitTemplate rabbitTemplate, WebClient.Builder webClientBuilder) {
        this.postService = postService;
        this.rabbitTemplate = rabbitTemplate;
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    @RabbitListener(queues = {"q.postsQueue"})
    @Override
    @SuppressWarnings("all")
    public void createPost(Post post) {
        try {
            System.out.println("Event received from queue: " + post);
            OAuth10aService service = new ServiceBuilder(consumerKey)
                    .apiSecret(consumerSecret)
                    .build(TwitterApi.instance());
            OAuth1AccessToken token = new OAuth1AccessToken(accessToken, accessTokenSecret);
            OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.twitter.com/2/tweets");
            request.addHeader("Content-Type", "application/json");
            TwitterRequestPayload tweet = new TwitterRequestPayload(post.getBody());
            ObjectMapper mapper = new ObjectMapper();
            String payload = mapper.writeValueAsString(tweet);
            request.setPayload(payload);
            service.signRequest(token, request);

            Response response = service.execute(request);
            System.out.println("posted tweet successfully for postId: " + post.getId() + " and response is: " + response.getBody());
            System.out.println("Updating status to completed for postId: " + post.getId());
            post.setStatus(PostStatus.COMPLETED.getId());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("failed to post,Updating status to in_process for postId: " + post.getId());
            post.setStatus(PostStatus.IN_PROCESS.getId());
        }
        postService.updatePostStatus(post);
    }
}