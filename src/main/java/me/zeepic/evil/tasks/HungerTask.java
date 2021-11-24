package me.zeepic.evil.tasks;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;

public class HungerTask implements Runnable {

    @Override
    public void run() {
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(player -> player.getGameMode().equals(GameMode.ADVENTURE))
                .filter(player -> !player.getWorld().getName().equals("world"))
                .forEach(player -> {
                    player.setFoodLevel(player.getFoodLevel() - 1);
                    player.sendActionBar(Component.text("You're getting hungry..."));
                });
    }

}
