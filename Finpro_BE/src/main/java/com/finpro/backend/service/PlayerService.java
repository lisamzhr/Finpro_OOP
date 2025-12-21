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
    public Player login(String username, String playerId) {
        return playerRepository.findById(playerId)
                .filter(p -> p.getUsername().equals(username))
                .orElse(null);
    }

    public Player updateLevel(String username, int newLevel) {
        Player player = playerRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        player.setLevel(newLevel);
        return playerRepository.save(player);
    }

    // UPDATE FASHION COIN
    public Player updateFashionCoin(String username, float fashionCoin) {
        Player player = playerRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        player.setFashionCoin(fashionCoin);
        return playerRepository.save(player);
    }

    // UPDATE SELECTED SKIN
    public Player updateSelectedSkin(String username, int selectedSkinId) {
        Player player = playerRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        player.setSelectedSkinId(selectedSkinId);
        return playerRepository.save(player);
    }
}