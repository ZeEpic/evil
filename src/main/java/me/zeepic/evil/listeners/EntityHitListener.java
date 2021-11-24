package me.zeepic.evil.listeners;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

public class EntityHitListener implements Listener {

    @EventHandler
    public void onHit(EntityDamageEvent event) {
        World world = event.getEntity().getWorld();
        LivingEntity entity = (LivingEntity) event.getEntity();
        if (world.getName().equals("world"))
            return;

        switch (event.getCause()) {
            case ENTITY_ATTACK, ENTITY_SWEEP_ATTACK -> {
                putBlood(entity);
                Entity attacker = ((EntityDamageByEntityEvent) event).getDamager();
                if (attacker instanceof Player damager) {
                    ItemStack holding = damager.getInventory().getItemInMainHand();
                    if (holding.getItemMeta() instanceof Damageable damageable) {
                        //CommandManager.configureMeta(damageable, damageable.displayName().toString(), false, damageable.getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), "rusty"), PersistentDataType.BYTE) == 1,1);
                    }
                }
            }
        }

    }

    public static void putBlood(Entity entity) {
        Block block = entity.getLocation().getBlock();
        if (block.getType().isAir() && !block.getRelative(BlockFace.DOWN).getType().isAir())
            block.setType(Material.REDSTONE_WIRE);
        entity.getWorld().spawnParticle(Particle.FALLING_LAVA,
                entity.getLocation(),
                25,
                0.2,
                1,
                0.2
        );
    }

}
