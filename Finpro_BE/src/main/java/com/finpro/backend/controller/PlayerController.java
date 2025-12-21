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
@CrossOrigin(origins = "*")
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

            Map<String, String> response = new HashMap<>();
            response.put("playerId", p.getPlayerId());
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

            Map<String, String> response = new HashMap<>();
            response.put("playerId", p.getPlayerId());
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
    @PostMapping("/update-level")
    public ResponseEntity<?> updateLevel(@RequestBody Player request) {
        Player player = playerService.updateLevel(request.getUsername(), request.getLevel());
        return ResponseEntity.ok(player);
    }

    @PostMapping("/update-fashion-coin")
    public ResponseEntity<?> updateFashionCoin(@RequestBody Map<String, Object> body) {
        String username = (String) body.get("username");
        Number fashionCoinNum = (Number) body.get("fashionCoin");

        if (username == null || fashionCoinNum == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid input"));
        }

        Player player = playerService.updateFashionCoin(username, fashionCoinNum.floatValue());
        Map<String, String> response = new HashMap<>();
        response.put("playerId", player.getPlayerId());
        response.put("username", player.getUsername());
        response.put("fashionCoin", String.valueOf(player.getFashionCoin()));
        response.put("message", "Coin updated successfully");
        System.out.println("Update selected skin success: " + response);

        return ResponseEntity.ok(player);
    }

    @PostMapping("/update-selected-skin")
    public ResponseEntity<?> updateSelectedSkin(@RequestBody Map<String, Object> body) {
        String username = (String) body.get("username");
        Integer selectedSkinId = (Integer) body.get("selectedSkinId");

        if (username == null || selectedSkinId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid input"));
        }

        Player player = playerService.updateSelectedSkin(username, selectedSkinId);
        Map<String, String> response = new HashMap<>();
        response.put("playerId", player.getPlayerId());
        response.put("username", player.getUsername());
        response.put("selectedSkinId", String.valueOf(player.getSelectedSkinId()));
        response.put("message", "Selected skin updated successfully");
        System.out.println("Update selected skin success: " + response);
        return ResponseEntity.ok(player);
    }
}