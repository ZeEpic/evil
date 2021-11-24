package me.zeepic.evil;

import com.onarandombox.MultiverseCore.MultiverseCore;
import lombok.Getter;
import lombok.Setter;
import me.zeepic.evil.listeners.*;
import me.zeepic.evil.models.GunState;
import me.zeepic.evil.tasks.HungerTask;
import me.zeepic.evil.tasks.SoundTask;
import me.zeepic.evil.utils.CommandManager;
import me.zeepic.evil.world.SimpleChunkGenerator;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Main extends JavaPlugin {

    @Getter private final int soundTaskDelay = 50;
    @Getter private final int hungerTaskDelay = 30;

    @Getter private static MultiverseCore multiverse;
    @Getter @Setter private static World gameWorld;

    @Getter private static Main instance;

    @Getter private static final Map<UUID, GunState> gunStates = new HashMap<>();

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
