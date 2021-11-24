package me.zeepic.evil.utils;

import me.zeepic.evil.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class ZombieLoot implements LootTable {

    private static final NamespacedKey key = new NamespacedKey(Main.getInstance(), "zombie_loot");

    @Override
    public @NotNull Collection<ItemStack> populateLoot(@NotNull Random random, @NotNull LootContext context) {
        List<ItemStack> loot = new ArrayList<>();
        Entity entity = context.getLootedEntity();
        if (entity == null)
            return loot;
        if (!entity.getType().equals(EntityType.ZOMBIE))
            return loot;

        if (random.nextInt(10) != 1)
            loot.add(new Item(Material.ROTTEN_FLESH, "Flesh"));
        if (random.nextInt(5) == 1)
            loot.add(new Item(Material.RED_DYE, "Blood"));
        if (random.nextInt(5) == 1)
            loot.add(new Item(Material.POTATO, "Zombie Brain"));
        else {
            if (random.nextBoolean())
                loot.add(new Item(Material.RABBIT_FOOT, "Zombie Foot").withAmount(2));
        }
        if (random.nextInt(20) == 1)
            loot.add(new Item(Material.WOODEN_SWORD, "Zombie Club"));
        if (random.nextBoolean())
            loot.add(new Item(Material.GREEN_DYE, "Zombie Organ"));

        return loot;
    }

    @Override
    public void fillInventory(@NotNull Inventory inventory, @NotNull Random random, @NotNull LootContext context) {
        inventory.addItem(populateLoot(random, context).toArray(new ItemStack[0]));
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return key;
    }

}
