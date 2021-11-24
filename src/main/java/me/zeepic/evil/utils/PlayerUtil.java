package me.zeepic.evil.utils;

import me.zeepic.evil.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class PlayerUtil {

    public static void message(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void spread(World world, int maxY, int borderSize) {
        Random random = new Random();
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(player -> player.getWorld().getName().equals("world"))
                .forEach(player -> {
                    int x = 0;
                    int z = 0;
                    int y = 100;
//                    while (y != maxY) {
//                       x = random.nextInt(borderSize * 2) - borderSize;
//                       z = random.nextInt(borderSize * 2) - borderSize;
//                       if (world.isChunkLoaded(x / 16, z / 16))
//                           y = world.getHighestBlockYAt(x, z);
//                    }
                    player.teleport(new Location(world, x, y + 1, z));
                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () ->
                            player.removePotionEffect(PotionEffectType.SLOW),
                            20);
                });
    }

}
