package com.finpro.frontend.models;

public class Player {
    private String id;
    private String username;

    public Player(String id, String username) {
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
