package com.programacho;

import io.micrometer.observation.Observation;

public class UserContext extends Observation.Context {

    private final String id;

    private final String name;

    public UserContext(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
