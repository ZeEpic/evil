package me.zeepic.evil.listeners;

import com.destroystokyo.paper.event.entity.ProjectileCollideEvent;
import me.zeepic.evil.models.SoundData;
import me.zeepic.evil.utils.Item;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ProjectileListener implements Listener {

    @EventHandler
    public void onHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if (projectile.getMetadata("bullet").isEmpty())
            return;
        if (event.getHitEntity() != null)
            return;

        projectile.getWorld().dropItemNaturally(projectile.getLocation(), new Item(Material.IRON_NUGGET, "Bullet"));
        projectile.remove();

    }

    @EventHandler
    public void onCollide(ProjectileCollideEvent event) {
        Projectile arrow = event.getEntity();
        if (arrow.getMetadata("bullet").isEmpty()
                || !event.getCollidedWith().getType().equals(EntityType.ZOMBIE)
                || event.getCollidedWith().equals(arrow.getShooter())) {
            return;
        }
        arrow.getWorld().dropItemNaturally(arrow.getLocation(), new Item(Material.STONE_BUTTON, "Used Bullet"));
        EntityHitListener.putBlood(event.getCollidedWith());
        DeathListener.dropZombieLoot((LivingEntity) event.getCollidedWith());
        event.getCollidedWith().remove();
        new SoundData(Sound.ENTITY_GENERIC_DEATH, SoundCategory.HOSTILE, 1, 0.8f);
        new SoundData(Sound.ENTITY_GENERIC_HURT, SoundCategory.HOSTILE, 1, 0.8f);
        arrow.getWorld().spawnParticle(Particle.ASH, event.getCollidedWith().getLocation(), 50, 0.5, 2, 1, 5);
        arrow.remove();
    }

}
