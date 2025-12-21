package com.finpro.frontend.observers;

import com.finpro.frontend.models.Player;

public interface PlayerListener {
    void onPlayerUpdated(Player player, String eventType);
}
