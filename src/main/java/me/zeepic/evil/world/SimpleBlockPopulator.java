package me.zeepic.evil.world;

import me.zeepic.evil.utils.WorldUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class SimpleBlockPopulator extends BlockPopulator {

    @Override
    public void populate(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull LimitedRegion limitedRegion) {

        for (int x = -8; x < 8; x++) {
            for (int z = -8; z < 8; z++) {
                int highestY = WorldUtil.highestY(chunkX * 16 + x, chunkZ * 16 + z, limitedRegion);
                Location highest = new Location(null, chunkX * 16 + x, highestY, chunkZ * 16 + z);
                if (highestY <= SimpleChunkGenerator.WATER_LEVEL)
                    continue;
                Material belowMat = limitedRegion.getType(highest.clone().subtract(0, 1, 0));
                if (highestY == SimpleChunkGenerator.WATER_LEVEL + 1 && random.nextInt(90) == 1 && belowMat.equals(Material.WATER)) {
                    limitedRegion.setType(highest, Material.LILY_PAD);
                    continue;
                }
                if (limitedRegion.isInRegion(highest) && limitedRegion.getType(highest).isAir() && highestY != SimpleChunkGenerator.WATER_LEVEL + 1 && SimpleChunkGenerator.getGroundMaterials().contains(belowMat)) {
                    Material material;
                    int rand = random.nextInt(5);
                    if (rand == 0) {
                        material = Material.GRASS;
                    } else if (rand == 1) {
                        if (random.nextBoolean())
                            material = Material.FERN;
                        else
                            material = Material.MOSS_CARPET;
                    } else
                        material = Material.AIR;
                    limitedRegion.setType(highest, material);
                }
            }
        }
        int amountOfTrees = random.nextInt(30) + 15;
        for (int i = 1; i < amountOfTrees; i++) {
            int x = chunkX * 16 + (random.nextInt(15) - 7);
            int z = chunkZ * 16 + (random.nextInt(15) - 7);
            int highestY = WorldUtil.highestY(x, z, limitedRegion);
            Location highest = new Location(null, x, highestY, z);
            if (highestY <= SimpleChunkGenerator.WATER_LEVEL + 1) // + 1 for the beaches
                continue;
            if (limitedRegion.isInRegion(highest)) {
                boolean success = limitedRegion.generateTree(highest, random, TreeType.BIG_TREE);
                if (!success) continue;
                limitedRegion.setType(highest.clone().subtract(0, 1, 0), Material.ROOTED_DIRT);
                for (int y = highestY; y < highestY + 6; y++) {
                    if (random.nextInt(5) == 1)
                        setVine(highest.getBlockX() + 1, y, highest.getBlockZ(), BlockFace.WEST, limitedRegion);
                    if (random.nextInt(5) == 1)
                        setVine(highest.getBlockX() - 1, y, highest.getBlockZ(), BlockFace.EAST, limitedRegion);
                    if (random.nextInt(5) == 1)
                        setVine(highest.getBlockX(), y, highest.getBlockZ() + 1, BlockFace.NORTH, limitedRegion);
                    if (random.nextInt(5) == 1)
                        setVine(highest.getBlockX(), y, highest.getBlockZ() - 1, BlockFace.SOUTH, limitedRegion);
                }
            }
        }

    }

    private void setVine(int x, int y, int z, BlockFace face, LimitedRegion limitedRegion) {
        if (!limitedRegion.getType(x, y, z).isAir())
            return;
        limitedRegion.setType(x, y, z, Material.VINE);
        if (limitedRegion.getBlockData(x, y, z) instanceof MultipleFacing facing) {
            facing.setFace(face, true);
            limitedRegion.setBlockData(x, y, z, facing);
        }
    }
}
