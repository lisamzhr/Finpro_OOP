package com.finpro.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @JsonProperty("playerId")
    private String playerId; // Ubah dari UUID ke String

    @JsonProperty("username")
    private String username;

    private Integer level = 1;

    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    public Player() {
        this.createdAt = LocalDateTime.now();
    }

    public Player(String username) {
        this.playerId = generateShortId(); // Generate ID sendiri
        this.username = username;
        this.level = 1;
        this.createdAt = LocalDateTime.now();
    }

    // Generate short ID (8 karakter alphanumeric)
    private String generateShortId() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(8);
        java.util.Random random = new java.util.Random();

        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        return sb.toString();
    }

    // Getters and Setters
    public String getPlayerId() { return playerId; }
    public void setPlayerId(String playerId) { this.playerId = playerId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Player{" +
                "playerId='" + playerId + '\'' +
                ", username='" + username + '\'' +
                ", level=" + level +
                '}';
    }
}