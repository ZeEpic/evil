package me.zeepic.evil.tasks;

import me.zeepic.evil.Main;
import me.zeepic.evil.models.SoundData;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;

import java.util.List;
import java.util.Random;

public class SoundTask implements Runnable {

    private static final List<SoundData> ambiance = List.of(
            new SoundData(Sound.AMBIENT_WARPED_FOREST_MOOD, SoundCategory.AMBIENT, 0.5f, 2),
            new SoundData(Sound.AMBIENT_CAVE, SoundCategory.AMBIENT, 1, 1),
            new SoundData(Sound.AMBIENT_BASALT_DELTAS_MOOD, SoundCategory.AMBIENT, 1, 0.1f),
            new SoundData(Sound.AMBIENT_CRIMSON_FOREST_MOOD, SoundCategory.AMBIENT, 0.5f, 2),
            new SoundData(Sound.AMBIENT_NETHER_WASTES_MOOD, SoundCategory.AMBIENT, 1, 0.1f),
            new SoundData(Sound.AMBIENT_NETHER_WASTES_MOOD, SoundCategory.AMBIENT, 0.5f, 1),
            new SoundData(Sound.AMBIENT_NETHER_WASTES_MOOD, SoundCategory.AMBIENT, 1, 2),
            new SoundData(Sound.AMBIENT_UNDERWATER_LOOP_ADDITIONS_ULTRA_RARE, SoundCategory.AMBIENT, 1, 0.1f),
            new SoundData(Sound.AMBIENT_UNDERWATER_LOOP_ADDITIONS_ULTRA_RARE, SoundCategory.AMBIENT, 1, 1),
            new SoundData(Sound.AMBIENT_UNDERWATER_LOOP_ADDITIONS_RARE, SoundCategory.AMBIENT, 0.25f, 0.1f),
            new SoundData(Sound.ENTITY_ENDERMAN_STARE, SoundCategory.AMBIENT, 0.5f, 0.1f),
            new SoundData(Sound.ENTITY_EVOKER_PREPARE_ATTACK, SoundCategory.AMBIENT, 0.25f, 2),
            new SoundData(Sound.ENTITY_EVOKER_PREPARE_ATTACK, SoundCategory.AMBIENT, 0.25f, 1),
            new SoundData(Sound.ENTITY_EVOKER_PREPARE_ATTACK, SoundCategory.AMBIENT, 0.25f, 0.1f),
            new SoundData(Sound.ENTITY_EVOKER_PREPARE_SUMMON, SoundCategory.AMBIENT, 0.25f, 0.1f),
            new SoundData(Sound.ENTITY_ENDERMAN_SCREAM, SoundCategory.AMBIENT, 1.5f, 0.1f),
            new SoundData(Sound.PARTICLE_SOUL_ESCAPE, SoundCategory.AMBIENT, 1.5f, 0.1f)
    );

    private final Random random = new Random();

    @Override
    public void run() {
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(player -> !player.getWorld().getName().equals("world"))
                .forEach(player ->
                        Bukkit.getScheduler().runTaskLater(
                                Main.getInstance(),
                                () -> ambiance.get(random.nextInt(ambiance.size())).play(player),
                                random.nextInt(100)
                        )
        );
    }

}
