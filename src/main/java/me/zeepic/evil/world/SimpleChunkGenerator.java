package me.zeepic.evil.world;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.noise.SimplexOctaveGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class SimpleChunkGenerator extends ChunkGenerator {

    public static final int WATER_LEVEL = 74;
    public static final int OCTAVES = 6;
    public static final double SCALE = 0.008;
    @Getter private static final List<Material> groundMaterials = List.of(Material.GRASS_BLOCK, Material.COARSE_DIRT, Material.DIRT);

    public static Material getRandomGroundMaterial(Random random, boolean useGrass) {
        Material material;
        int rand = random.nextInt(10);
        if (rand == 0)
            material = Material.COARSE_DIRT;
        else if (rand == 1)
            material = Material.DIRT;
        else {
            if (useGrass)
                material = Material.GRASS_BLOCK;
            else
                material = Material.ROOTED_DIRT;
        }
        return material;
    }

    @Override
    public void generateNoise(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkGenerator.ChunkData chunkData) {

        SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(worldInfo.getSeed()), OCTAVES);
        generator.setScale(SCALE);

        int worldX = chunkX * 16;
        int worldZ = chunkZ * 16;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                double noise = generator.noise(worldX + x, worldZ + z, 1, 1, true);
                int height = (int) (noise * 40);
                height += 84;
                if (height > chunkData.getMaxHeight()) {
                    height = chunkData.getMaxHeight();
                }
                Material material = getRandomGroundMaterial(random, true);
                if (height < WATER_LEVEL) {
                    for (int y = WATER_LEVEL; y > height; y--) {
                        if (y == height + 1) {
                            if (random.nextInt(5) == 1) {
                                if (random.nextBoolean())
                                    chunkData.setBlock(x, y, z, Material.SEAGRASS);
                                else
                                    chunkData.setBlock(x, y, z, Material.KELP_PLANT);
                            } else
                                chunkData.setBlock(x, y, z, Material.WATER);
                        } else
                            chunkData.setBlock(x, y, z, Material.WATER);
                    }
                    material = Material.SAND;
                } else if (height == WATER_LEVEL) {
                    material = Material.SANDSTONE;
                }
                chunkData.setBlock(x, height - 1, z, Material.BARRIER);
                chunkData.setBlock(x, height, z, material);
            }
        }
    }

    @Override
    public boolean shouldGenerateSurface() {
        return false;
    }

    @Override
    public boolean shouldGenerateDecorations() {
        return false;
    }

    @Override
    public boolean shouldGenerateStructures() {
        return false;
    }

    @Override
    public @Nullable BiomeProvider getDefaultBiomeProvider(@NotNull WorldInfo worldInfo) {
        return new SimpleBiomeProvider();
    }

}