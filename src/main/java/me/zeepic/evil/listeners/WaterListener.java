package me.zeepic.evil.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityAirChangeEvent;

public class WaterListener implements Listener {

    @EventHandler
    public void onAirChange(EntityAirChangeEvent event) {
        if (!(event.getEntity() instanceof Player player))
            return;
        player.setRemainingAir(player.getRemainingAir() - 20);
    }

}
