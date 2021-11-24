package me.zeepic.evil.listeners;

import me.zeepic.evil.utils.Item;
import me.zeepic.evil.utils.PlayerUtil;
import me.zeepic.evil.utils.ZombieLoot;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        switch (event.getEntityType()) {
            case PLAYER -> {
                Player player = (Player) event.getEntity();
                player.getWorld().spawnParticle(Particle.ASH, player.getLocation(), 50, 0.5, 2, 1, 5);
                PlayerUtil.message(player, "&cYou died!");
                player.setGameMode(GameMode.SPECTATOR);
                player.removePotionEffect(PotionEffectType.BLINDNESS);
                Zombie zombie = (Zombie) player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE, false);
                zombie.getEquipment().setHelmet(new Item(Material.PLAYER_HEAD, player.getName()).customHead(player));
                player.getInventory().clear();
                event.setCancelled(true);
            }
            case ZOMBIE -> {
                event.getDrops().clear();
                dropZombieLoot(event.getEntity());
            }
            default -> {}
        }
    }

    public static void dropZombieLoot(LivingEntity entity) {
        LootTable loot = new ZombieLoot();
        loot.populateLoot(
                        new Random(),
                        new LootContext.Builder(entity.getLocation())
                                .killer(entity.getKiller())
                                .lootedEntity(entity)
                                .build()
                )
                .forEach(item -> entity.getWorld().dropItemNaturally(entity.getLocation(), item));
    }

}
