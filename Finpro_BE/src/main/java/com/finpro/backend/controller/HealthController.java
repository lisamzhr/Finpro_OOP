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

    @GetMapping("/health")
    public Map<String, Object> healthCheck() {
        Map<String, Object> healthStatus = new HashMap<>();
        healthStatus.put("status", "UP");
        healthStatus.put("message", "Fashion Game Backend is running!");
        healthStatus.put("timestamp", System.currentTimeMillis());
        return healthStatus;
    }

    @GetMapping("/info")
    public Map<String, Object> info() {
        Map<String, Object> response = new HashMap<>();

        response.put("application", "Fashion_Game_Backend");
        response.put("version", "1.0");
        response.put("description", "Backend untuk Fashion Dating Game - Mengelola data player, level, dan fashion coin");

        // Daftar endpoint
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("basePath", "/api");
        endpoints.put("health", "/api/health");
        endpoints.put("info", "/api/info");
        endpoints.put("register", "/api/player/register");
        endpoints.put("login", "/api/player/login");
        endpoints.put("updateLevel", "/api/player/update-level");
        endpoints.put("updateFashionCoin", "/api/player/update-fashion-coin");
        endpoints.put("updateSelectedSkin", "/api/player/update-selected-skin");

        response.put("endpoints", endpoints);

        return response;
    }
}