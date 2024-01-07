package com.example.socialmediapostsscheduler.entity;

import java.util.Optional;

public enum PostStatus {
    IN_PROCESS(1),
    IN_FLIGHT(2),
    COMPLETED(3);

    private int id;
    PostStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static Optional<PostStatus> valueOf(int id) {
        for(PostStatus status : PostStatus.values()) {
            if(status.getId() == id) {
                return Optional.of(status);
            }
        }
        return Optional.empty();
    }
}
