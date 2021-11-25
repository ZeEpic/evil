package me.zeepic.evil.listeners;

import me.zeepic.evil.Main;
import me.zeepic.evil.models.GunState;
import me.zeepic.evil.utils.PlayerUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        event.joinMessage(Component.text(" + ").color(NamedTextColor.DARK_GREEN).append(Component.text(player.getName()).color(NamedTextColor.GRAY)));
        PlayerUtil.message(player, "&a&oWelcome to &c&oEvil >:)");
        PlayerUtil.message(player, "&3Join the Discord server with &b/discord");
        if (player.isOp())
            PlayerUtil.message(player, "&a&oDo &6&o/start &a&oto start a new game of zombie survival.");
        else
            PlayerUtil.message(player, "&a&oWait for an admin to do &6&o/start &a&oto start a new game of zombie survival.");
        setAttackSpeed(player);
        player.teleport(Main.SPAWN_LOCATION);
        player.setHealth(20);
        player.setFoodLevel(20);
        Main.removeEffects(player);
        if (!player.getGameMode().equals(GameMode.CREATIVE))
            player.getInventory().clear();
        player.setGameMode(GameMode.ADVENTURE);
        Main.getGunStates().put(player.getUniqueId(), GunState.CAN_FIRE);

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.quitMessage(Component.text(" - ").color(NamedTextColor.RED).append(Component.text(event.getPlayer().getName()).color(NamedTextColor.GRAY)));
        Main.getGunStates().remove(event.getPlayer().getUniqueId());
    }

    private void setAttackSpeed(Player player){
        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
        if (attribute == null) return;
        attribute.setBaseValue(16);
        player.saveData();
    }

}
