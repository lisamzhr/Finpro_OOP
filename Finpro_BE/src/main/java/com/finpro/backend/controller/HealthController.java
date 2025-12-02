package com.finpro.backend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class HealthController {
    //method
    @GetMapping("/health")
    public Map<String, Object> healthCheck() {
        Map<String, Object> healthStatus = new HashMap<>();
        healthStatus.put("status", "UP");
        healthStatus.put("message", "Jetpack Joyride Backend is running!");
        healthStatus.put("timestamp", System.currentTimeMillis());
        return healthStatus;
    }

    @GetMapping("/info")
    public Map<String, Object> info() {
        Map<String, Object> response = new HashMap<>();

        response.put("application", "CS6_JetpackJoyride_Backend");
        response.put("version", "1.0");
        response.put("description", "Backend untuk game Jetpack Joyride - Mengelola data pemain dan skor");

        //daftar endpoint
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("basePath", "/api");
        endpoints.put("players", "/api/players");
        endpoints.put("scores", "/api/scores");
        endpoints.put("leaderboard", "/api/scores/leaderboard");
        endpoints.put("health", "/api/health");

        response.put("endpoints", endpoints);

        return response;
    }
}