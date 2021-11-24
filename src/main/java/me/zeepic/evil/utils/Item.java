package me.zeepic.evil.utils;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Item extends ItemStack {

    @Getter private String name;
    private List<Component> lore;


    /**
     * To create an item from an already existing item stack.
     * @param itemStack The item stack which we want to convert.
     */
    public Item(ItemStack itemStack) {

        setType(itemStack.getType());
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        if (meta.hasDisplayName())
            setName(Objects.requireNonNull(meta.displayName()).toString());
        if (meta.hasLore())
            this.lore(meta.lore());
        setAmount(itemStack.getAmount());

    }

    /**
     * This is the basic item constructor.
     * @param material The item's type.
     * @param name The name of the item. Note that it will be colored white.
     */
    public Item(Material material, String name) {

        setType(material);
        setName(ChatColor.WHITE + name);
        setAmount(1);

    }

    /**
     * This is for when you want an item with lore text.
     * @param material The item's type.
     * @param name The name of the item. Note that it will be colored white.
     * @param lore The lore text, in a list. Each item in the list will be on its own line, and colored gray.
     */
    public Item(Material material, String name, List<Component> lore) {

        setType(material);
        setName(ChatColor.WHITE + name);
        lore(lore);
        setAmount(1);

    }

    /**
     * This is for when you want an item with lore text.
     * @param material The item's type.
     * @param name The name of the item. Note that it will be colored white.
     * @param lore The lore text, in a string. It will be one line, regardless of \n characters used. Note that it will be colored gray.
     */
    public Item(Material material, String name, String lore) {

        setType(material);
        setName(ChatColor.WHITE + name);
        lore(Collections.singletonList(Component.text(lore)));
        setAmount(1);

    }

    public List<Component> lore() {
        return lore;
    }

    /**
     * Sets the meta name of this item.
     * @param name The new name to use.
     */
    public void setName(String name) {
        this.name = name;
        ItemMeta meta = getItemMeta();
        assert meta != null;
        meta.displayName(Component.text(this.name));
        setItemMeta(meta);
    }

    /**
     * Sets the lore of this item.
     * @param lore The list of lore to use. Each item in the list is its own line in the lore.
     */
    public void lore(@Nullable List<Component> lore) {
        if (lore == null || lore.isEmpty())
            return;
        List<Component> newLore = lore.stream().map(line -> line.color(NamedTextColor.GRAY)).collect(Collectors.toList());
        this.lore = newLore;

        ItemMeta meta = getItemMeta();
        assert meta != null;
        meta.lore(newLore);
        setItemMeta(meta);
    }

    /**
     * To get this item but with a different material.
     * @param type The new type to use.
     * @return The modified version of this item.
     */
    public Item withType(Material type) {
        setType(type);
        return this;
    }

    /**
     * To get this item but with a different amount.
     * @param amount The new amount to use.
     * @return The modified version of this item.
     */
    public Item withAmount(int amount) {
        setAmount(amount);
        return this;
    }

    public Item customHead(Player player) {
        if (this instanceof SkullMeta skull) {
            if (skull.hasOwner())
                skull.setOwningPlayer(player);
            setItemMeta(skull);
        }
        return this;
    }
}
