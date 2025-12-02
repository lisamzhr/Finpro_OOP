package com.finpro.backend.service;

import com.finpro.backend.model.Player;
import com.finpro.backend.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    //REGISTER
    public Player register(String username) {
        // Cek apakah username sudah ada
        if (playerRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        Player p = new Player(username);
        return playerRepository.save(p);
    }

    //LOGIN
    public Player login(String username, UUID playerId) {
        return playerRepository.findById(playerId)
                .filter(p -> p.getUsername().equals(username))
                .orElse(null);
    }
}