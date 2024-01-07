package com.example.socialmediapostsscheduler.utility;
import com.github.scribejava.core.builder.api.DefaultApi10a;

public class TwitterApi extends DefaultApi10a {

    protected TwitterApi() {
    }

    private static class InstanceHolder {
        private static final TwitterApi INSTANCE = new TwitterApi();
    }

    public static TwitterApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "https://api.twitter.com/oauth/request_token";
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.twitter.com/oauth/access_token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://api.twitter.com/oauth/authorize";
    }
}
