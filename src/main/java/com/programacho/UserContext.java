package com.programacho;

import io.micrometer.observation.Observation;

public class UserContext extends Observation.Context {

    private final String id;

    private final String username;

    public UserContext(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
