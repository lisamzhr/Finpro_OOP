package com.finpro.backend.controller;

import com.finpro.backend.model.Player;
import com.finpro.backend.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/player")
@CrossOrigin(origins = "*") // Allow all origins (untuk testing)
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        try {
            String username = body.get("username");

            if (username == null || username.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Username cannot be empty"));
            }

            Player p = playerService.register(username);

            // Return Map dengan format yang jelas
            Map<String, String> response = new HashMap<>();
            response.put("playerId", p.getPlayerId()); // Sudah String, tidak perlu toString()
            response.put("username", p.getUsername());

            System.out.println("Register success: " + response);
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            System.err.println("Register error: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            System.err.println("Register unexpected error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            String username = body.get("username");
            String playerId = body.get("playerId");

            if (username == null || playerId == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Username and Player ID required"));
            }

            System.out.println("Login attempt - Username: " + username + ", PlayerId: " + playerId);

            Player p = playerService.login(username, playerId);

            if (p == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Username or Player ID invalid"));
            }

            // Return Map dengan format yang jelas
            Map<String, String> response = new HashMap<>();
            response.put("playerId", p.getPlayerId()); // Sudah String
            response.put("username", p.getUsername());
            response.put("level", String.valueOf(p.getLevel()));

            System.out.println("Login success: " + response);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Login unexpected error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error"));
        }
    }
}