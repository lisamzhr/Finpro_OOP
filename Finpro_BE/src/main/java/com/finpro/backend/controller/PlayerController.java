package com.finpro.backend.controller;

import com.finpro.backend.model.Player;
import com.finpro.backend.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");

        if (username == null || username.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Username cannot be empty"));
        }

        Player p = playerService.register(username);
        return ResponseEntity.ok(p);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String idString = body.get("playerId");

        UUID playerId = UUID.fromString(idString); // throws IllegalArgumentException otomatis

        Player p = playerService.login(username, playerId);

        if (p == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Username or Player ID invalid"));
        }

        return ResponseEntity.ok(p);
    }
}