package me.zeepic.evil.listeners;

import io.papermc.paper.event.packet.PlayerChunkLoadEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onFood(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (!player.getWorld().getName().equals("world"))
                return;
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (!player.getWorld().getName().equals("world"))
                return;
            if (event.getCause().equals(EntityDamageEvent.DamageCause.VOID))
                player.teleport(new Location(Bukkit.getWorld("world"), -8, 71, -2));
            event.setCancelled(true);
        }
    }

}
