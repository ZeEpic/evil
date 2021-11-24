package me.zeepic.evil.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        if (event.getPlayer().getGameMode().equals(GameMode.SPECTATOR))
            event.setCancelled(true);
    }

}
