package me.zeepic.evil.listeners;

import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.Comparator;

public class EntitySpawnListener implements Listener {

    public static final double MAX_ZOMBIE_DISTANCE = 50.0;

    @Setter private static boolean spawnEntities = false;

    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        if (!spawnEntities)
            return;
        EntityType type = event.getEntityType();
        if (type.equals(EntityType.DROPPED_ITEM) || type.equals(EntityType.ARROW) || type.equals(EntityType.SNOWBALL))
            return;
        event.setCancelled(true);
        if (!event.getEntityType().equals(EntityType.ZOMBIE))
            return;

        Zombie zombie = (Zombie) event.getEntity();

        Player target = Bukkit
                .getOnlinePlayers()
                .stream()
                .filter(player -> player.getWorld().getName().equals(zombie.getWorld().getName()))
                .min(Comparator.comparingDouble(player ->
                        player.getLocation().distance(zombie.getLocation())
                ))
                .orElse(null);
        if (target == null)
            return;
        else if (target.getLocation().distance(zombie.getLocation()) > MAX_ZOMBIE_DISTANCE)
            return;

        event.setCancelled(false);
        zombie.getEquipment().clear();
        zombie.setCanBreakDoors(true);
        zombie.setShouldBurnInDay(false);
        zombie.setConversionTime(100000);
        zombie.setRemoveWhenFarAway(true);
        zombie.setTarget(target);

    }

}
