package me.zeepic.evil.utils;

import me.zeepic.evil.world.SimpleChunkGenerator;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.util.Vector;

import java.util.Random;

public class WorldUtil {

    public static final int MAX_STRUCTURE_TO_GROUND_PADDING_DISTANCE = 2;

    public static void setGameRules(World world) {

        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
        world.setGameRule(GameRule.MOB_GRIEFING, false);
        world.setGameRule(GameRule.KEEP_INVENTORY, true);
        world.setGameRule(GameRule.DO_PATROL_SPAWNING, false);
        world.setGameRule(GameRule.DO_TRADER_SPAWNING, false);
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(GameRule.DO_INSOMNIA, false);
        world.setGameRule(GameRule.DO_FIRE_TICK, false);
        world.setGameRule(GameRule.DO_TRADER_SPAWNING, false);
        world.setGameRule(GameRule.DISABLE_RAIDS, true);
        world.setGameRule(GameRule.DO_TILE_DROPS, false);
        world.setGameRule(GameRule.COMMAND_BLOCK_OUTPUT, false);
        world.setGameRule(GameRule.DO_ENTITY_DROPS, false);
        world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
        world.setGameRule(GameRule.NATURAL_REGENERATION, false);
        world.setGameRule(GameRule.REDUCED_DEBUG_INFO, true);
        world.setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, false);
        world.setTime(18000L);
        world.setDifficulty(Difficulty.HARD);

    }

    public static int highestY(int x, int z, LimitedRegion region) {
        int y;
        for (y = 0; isAir(x, y, z, region); y++);
        return y + 2; // 1 for the barrier, 1 for the grass layer
    }

    private static boolean isAir(int x, int y, int z, LimitedRegion region) {
        if (region.isInRegion(x, y, z))
            return region.getType(x, y, z).isAir();
        return true;
    }

    public static int highestY(int x, int z, World world) {
        int y;
        for (y = 0; world.getType(x, y, z).isAir(); y++);
        return y + 2; // 1 for the barrier, 1 for the grass layer
    }


    public static int fillAndFindHighestGrassInArea(int startX, int startZ, Vector size, LimitedRegion region, Random random) {
        if (!region.isInRegion(startX, 0, startZ))
            return 0; // search failed

        int high = 0;
        int low = Integer.MAX_VALUE;
        for (int x = startX; x < (size.getBlockX() + startX); x++) {
            for (int z = startZ; z < (size.getBlockZ() + startZ); z++) {
                int highestY = highestY(x, z, region) - 1;
                if (highestY > high)
                    high = highestY;
                if (highestY < low)
                    low = highestY;
            }
        }
        if ((high - low) > MAX_STRUCTURE_TO_GROUND_PADDING_DISTANCE)
            return 0; // search failed
        if (low < SimpleChunkGenerator.WATER_LEVEL + 1)
            return 0; // search failed

        for (int x = startX; x < (size.getBlockX() + startX); x++) {
            for (int z = startZ; z < (size.getBlockZ() + startZ); z++) {
                for (int y = SimpleChunkGenerator.WATER_LEVEL + 1; y < high; y++) {
                    if (region.isInRegion(x, y, z)) {
                        region.setType(x, y, z, SimpleChunkGenerator.getRandomGroundMaterial(random, false));
                    }
                }
            }
        }
        return high;
    }
}
