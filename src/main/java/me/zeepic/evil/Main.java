package me.zeepic.evil;

import com.onarandombox.MultiverseCore.MultiverseCore;
import lombok.Getter;
import lombok.Setter;
import me.zeepic.evil.listeners.*;
import me.zeepic.evil.models.GunState;
import me.zeepic.evil.models.SoundData;
import me.zeepic.evil.tasks.HungerTask;
import me.zeepic.evil.tasks.SoundTask;
import me.zeepic.evil.utils.CommandManager;
import me.zeepic.evil.world.SimpleChunkGenerator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Main extends JavaPlugin {

    public static final Location SPAWN_LOCATION = new Location(Bukkit.getWorld("world"), -8, 71, -2);
    @Getter private final int soundTaskDelay = 50;
    @Getter private final int hungerTaskDelay = 30;

    @Getter private static MultiverseCore multiverse;
    @Getter @Setter private static World gameWorld;

    @Getter private static Main instance;

    @Getter private static final Map<UUID, GunState> gunStates = new HashMap<>();

    public static void endGame(@Nullable Player winner) {

        if (winner != null) {
            Bukkit.broadcast(Component.text(" "));
            Bukkit.broadcast(Component.text(" "));
            Bukkit.broadcast(Component.text(winner.getName() + " wins!").color(NamedTextColor.GREEN));
            Bukkit.broadcast(Component.text(" "));
            Bukkit.broadcast(Component.text(" "));
        }
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(p -> !p.getWorld().getName().equals("world"))
                .forEach(player -> {
                    player.teleport(SPAWN_LOCATION);
                    player.getInventory().clear();
                    removeEffects(player);
                    player.setHealth(20);
                    player.setFoodLevel(20);
                    Main.getGunStates().put(player.getUniqueId(), GunState.CAN_FIRE);
                    if (player.equals(winner)) {
                        player.showTitle(Title.title(
                                Component.text("You win!").color(NamedTextColor.GREEN),
                                Component.text("You were the last person alive.").color(NamedTextColor.GRAY)
                        ));
                    } else {
                        player.showTitle(Title.title(
                                Component.text("Game over!").color(NamedTextColor.RED),
                                Component.text("The game has ended.").color(NamedTextColor.GRAY)
                        ));
                        new SoundData(Sound.UI_TOAST_OUT, SoundCategory.MASTER, 2, 1).play(player);
                    }
                });
        if (winner != null) {
            new SoundData(Sound.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.MASTER, 1.5f, 1.2f).play(winner);
            new SoundData(Sound.PARTICLE_SOUL_ESCAPE, SoundCategory.MASTER, 1, 0.1f).play(winner);
        }

    }

    public static void removeEffects(Player player) {
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        instance = this;

        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new EntityHitListener(), this);
        manager.registerEvents(new EntitySpawnListener(), this);
        manager.registerEvents(new JoinListener(), this);
        manager.registerEvents(new WorldInitListener(), this);
        manager.registerEvents(new BlockClickListener(), this);
        manager.registerEvents(new DeathListener(), this);
        manager.registerEvents(new PlayerListener(), this);
        manager.registerEvents(new ChatListener(), this);
        manager.registerEvents(new WaterListener(), this);
        manager.registerEvents(new ProjectileListener(), this);
        CommandManager.addCommands();

        multiverse = (MultiverseCore) manager.getPlugin("Multiverse-Core");

        getServer().getScheduler().runTaskTimer(this, new HungerTask(), hungerTaskDelay * 20L, hungerTaskDelay * 20L);
        getServer().getScheduler().runTaskTimer(this, new SoundTask(), soundTaskDelay * 17L, soundTaskDelay * 19L);

        // for testing
        Bukkit.getOnlinePlayers().forEach(player -> gunStates.put(player.getUniqueId(), GunState.CAN_FIRE));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }

    @Override
    public @NotNull ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id) {
        return new SimpleChunkGenerator();
    }

}
