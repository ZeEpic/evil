package me.zeepic.evil.listeners;

import me.zeepic.evil.Main;
import me.zeepic.evil.models.GunState;
import me.zeepic.evil.models.SoundData;
import me.zeepic.evil.utils.Item;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class BlockClickListener implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.PHYSICAL))
            return;
        assert event.getHand() != null;
        if (event.getHand().equals(EquipmentSlot.OFF_HAND))
            return;
        Component name = event.getPlayer().getInventory().getItemInMainHand().displayName();
        if (name.contains(Component.text("Gun"))) {
            fireGun(name, event.getPlayer());
            return;
        }
        Block block = event.getClickedBlock();
        if (block == null)
            return;
        if (block.getType().equals(Material.CHEST)) {
            lootChest(event.getPlayer(), block);
        }
    }

    private void lootChest(Player player, Block block) {
        if (!player.getGameMode().equals(GameMode.ADVENTURE))
            return;
        if (block.getState() instanceof Chest chest) {
            for (ItemStack item : chest.getBlockInventory().getStorageContents()) {
                if (item != null && !item.getType().isAir())
                    player.getInventory().addItem(item);
            }
            player.closeInventory();
        }
        block.getWorld().playSound(block.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1f, 0.5f);
        block.setType(Material.AIR);
    }

    private void fireGun(Component name, Player player) {
        GunState state = Main.getGunStates().get(player.getUniqueId());
        if (state.isAbleToFire() || player.getGameMode().equals(GameMode.CREATIVE)) {
            Item bullet = new Item(Material.IRON_NUGGET, "Bullet");
            if (!player.getInventory().containsAtLeast(bullet, 1))
                return;
            player.getInventory().removeItem(bullet);
            new SoundData(Sound.ENTITY_IRON_GOLEM_ATTACK, SoundCategory.PLAYERS, 1, 1).play(player);
            new SoundData(Sound.ENTITY_IRON_GOLEM_DAMAGE, SoundCategory.PLAYERS, 1, 2).play(player);
            new SoundData(Sound.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1, 2).play(player);
            new SoundData(Sound.ENTITY_SKELETON_SHOOT, SoundCategory.PLAYERS, 1, 2).play(player);
            new SoundData(Sound.ENTITY_GENERIC_HURT, SoundCategory.PLAYERS, 1, 0.1f).play(player);
            player.getWorld().spawnParticle(Particle.SMOKE_LARGE, player.getLocation(), 50, 1, 2, 1, 0.1);
            player.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, player.getLocation(), 1, 0, 0, 0, 0.1);
            Projectile projectile = player.launchProjectile(Arrow.class, player.getLocation().getDirection().multiply(8));
            projectile.setGravity(false);
            projectile.setBounce(false); // why doesn't this do anything :(
            projectile.setMetadata("bullet", new FixedMetadataValue(Main.getInstance(), true));
            Main.getGunStates().put(player.getUniqueId(), GunState.COOLDOWN);
            Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getInstance(), () ->
                Main.getGunStates().put(player.getUniqueId(), GunState.CAN_RELOAD),
                    GunState.GUN_COOLDOWN * 20L);
        } else if (state.equals(GunState.CAN_RELOAD)) {
            Main.getGunStates().put(player.getUniqueId(), GunState.RELOADING);
            player.sendActionBar(Component.text("Reloading...").color(NamedTextColor.GRAY).decorate(TextDecoration.ITALIC));
            new SoundData(Sound.ITEM_CROSSBOW_LOADING_START, SoundCategory.PLAYERS, 1, 2).play(player);
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> new SoundData(Sound.ITEM_CROSSBOW_LOADING_START, SoundCategory.PLAYERS, 1, 1.6f).play(player), 5);
            Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getInstance(), () -> {
                Main.getGunStates().put(player.getUniqueId(), GunState.CAN_FIRE);
                new SoundData(Sound.ENTITY_IRON_GOLEM_REPAIR, SoundCategory.PLAYERS, 1, 1.75f).play(player);
                new SoundData(Sound.ITEM_CROSSBOW_LOADING_END, SoundCategory.PLAYERS, 1, 1.75f).play(player);
            }, GunState.GUN_COOLDOWN * 20L);
        }
    }

}
