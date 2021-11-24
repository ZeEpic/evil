package me.zeepic.evil.world;

import com.github.shynixn.structureblocklib.api.bukkit.StructureBlockLibApi;
import com.github.shynixn.structureblocklib.api.enumeration.StructureRotation;
import lombok.Getter;
import me.zeepic.evil.Main;
import me.zeepic.evil.utils.WorldUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;

public class StructurePopulator extends BlockPopulator {

    @Getter private static final Map<Path, Vector> structurePaths = Map.of(
            new File("plugins/Evil/test_rock.nbt").toPath(), new Vector(7, 8, 6)
    );

    @Override
    public void populate(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull LimitedRegion limitedRegion) {

        if (random.nextInt(5) == 1) {
            int x = chunkX * 16 + (random.nextInt(15) - 7);
            int z = chunkZ * 16 + (random.nextInt(15) - 7);

            int highest = WorldUtil.highestY(x, z, limitedRegion);
            if (highest > SimpleChunkGenerator.WATER_LEVEL + 1 && limitedRegion.isInRegion(x, highest, z)) { // + 1 for the beaches
                StructureRotation rotation = StructureRotation.values()[random.nextInt(StructureRotation.values().length)];
                Object[] paths = structurePaths.keySet().toArray();
                Path struct = (Path) paths[random.nextInt(paths.length)];
                Vector size = structurePaths.get(struct);
                highest = WorldUtil.fillAndFindHighestGrassInArea(x, z, size, limitedRegion, random);
                if (highest != 0) {
                    spawnRandomStructure(new Location(null, x, highest, z), struct, StructureRotation.NONE, worldInfo.getSeed());
                }
            }
        }
    }

    private Vector rotatedVector(Vector vector, StructureRotation rotation) {
        // 0 -> x, z
        // 90 -> -z, x
        // 180 -> -x, -z
        // 270 -> z, -x
        Vector rotated = null;
        switch (rotation) {
            case NONE -> rotated = vector;
            case ROTATION_90 -> rotated = new Vector(-vector.getZ(), vector.getY(), vector.getX());
            case ROTATION_180 -> rotated = new Vector(-vector.getX(), vector.getY(), -vector.getZ());
            case ROTATION_270 -> rotated = new Vector(vector.getZ(), vector.getY(), -vector.getX());
        }
        return rotated;
    }

    private void spawnRandomStructure(Location location, Path struct, StructureRotation rotation, long seed) {
        location.setWorld(Main.getGameWorld());
        Main plugin = Main.getInstance();
        StructureBlockLibApi.INSTANCE
                .loadStructure(plugin)
                .at(location)
                .rotation(rotation)
                //.integrity(0.9f)
                .seed(seed)
                .includeEntities(true)
                .loadFromPath(struct)
                .onException(e -> plugin.getLogger().log(Level.SEVERE, "Failed to load structure.", e))
                .onResult(result -> Bukkit.getLogger().log(Level.FINEST, "Structure was generated."));
    }
}
