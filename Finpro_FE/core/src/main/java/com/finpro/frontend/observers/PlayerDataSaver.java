package com.finpro.frontend.observers;

import com.finpro.frontend.models.Player;
import com.finpro.frontend.services.BackendService;

public class PlayerDataSaver implements PlayerListener {

    private final BackendService backendService;
    private boolean isSaving = false;

    public PlayerDataSaver() {
        this.backendService = new BackendService();
    }

    @Override
    public void onPlayerUpdated(Player player, String eventType) {
        if (isSaving) {
            System.out.println("Save already in progress, skipping: " + eventType);
            return;
        }

        switch(eventType) {
            case "LEVEL_CHANGED":
                saveLevel(player);
                break;

            case "COIN_CHANGED":
                saveFashionCoin(player);
                break;

            case "SKIN_CHANGED":
                saveSelectedSkin(player);
                break;

            default:
                System.out.println("Unknown event type: " + eventType);
        }
    }

    private void saveLevel(Player player) {
        isSaving = true;
        System.out.println("Auto-saving level for player: " + player.getUsername());

        backendService.updateLevel(
            player.getUsername(),
            player.getLevel(),
            new BackendService.RequestCallback() {
                @Override
                public void onSuccess(String response) {
                    System.out.println("✓ Level auto-saved successfully!");
                    System.out.println("Response: " + response);
                    isSaving = false;
                }

                @Override
                public void onError(String error) {
                    System.err.println("✗ Failed to auto-save level: " + error);
                    isSaving = false;
                }
            }
        );
    }

    private void saveFashionCoin(Player player) {
        isSaving = true;
        System.out.println("Auto-saving fashion coin for player: " + player.getUsername());

        backendService.updateFashionCoin(
            player.getUsername(),
            player.getFashionCoin(),
            new BackendService.RequestCallback() {
                @Override
                public void onSuccess(String response) {
                    System.out.println("✓ Fashion coin auto-saved successfully!");
                    System.out.println("Response: " + response);
                    isSaving = false;
                }

                @Override
                public void onError(String error) {
                    System.err.println("✗ Failed to auto-save fashion coin: " + error);
                    isSaving = false;
                }
            }
        );
    }

    private void saveSelectedSkin(Player player) {
        isSaving = true;
        System.out.println("Auto-saving selected skin for player: " + player.getUsername());

        backendService.updateSelectedSkin(
            player.getUsername(),
            player.getSelectedSkinId(),
            new BackendService.RequestCallback() {
                @Override
                public void onSuccess(String response) {
                    System.out.println("✓ Selected skin auto-saved successfully!");
                    System.out.println("Response: " + response);
                    isSaving = false;
                }

                @Override
                public void onError(String error) {
                    System.err.println("✗ Failed to auto-save selected skin: " + error);
                    isSaving = false;
                }
            }
        );
    }
}
