package me.zeepic.evil.utils;

import com.onarandombox.MultiverseCore.api.MVWorldManager;
import me.zeepic.evil.Main;
import me.zeepic.evil.commands.SimpleCommand;
import me.zeepic.evil.listeners.EntitySpawnListener;
import me.zeepic.evil.models.GunState;
import me.zeepic.evil.world.SimpleChunkGenerator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class CommandManager {


    private static final int BORDER_SIZE = 900;

    public static void addCommands() {

        addCommand("start", new SimpleCommand(Main.getInstance(), player -> {
            MVWorldManager worldManager = Main.getMultiverse().getMVWorldManager();
            Random random = new Random();
            String gameName = "game" + random.nextInt();
            boolean success = worldManager.addWorld(
                    gameName,
                    World.Environment.NORMAL,
                    null,
                    WorldType.NORMAL,
                    false,
                    "Evil" // >:)
            );
            if (!success) {
                PlayerUtil.message(player, "Couldn't make a world for your game. Try again later.");
                return;
            }
            Main.setGameWorld(Bukkit.getWorld(gameName));
            WorldBorder border = Main.getGameWorld().getWorldBorder();
            border.setCenter(0, 0);
            border.setDamageAmount(10);
            border.setSize(BORDER_SIZE);
            Bukkit.getOnlinePlayers().forEach(CommandManager::setupPlayer);
            WorldUtil.setGameRules(Main.getGameWorld());
            PlayerUtil.spread(Main.getGameWorld(), SimpleChunkGenerator.WATER_LEVEL + 1, BORDER_SIZE / 3);
            // they will always spawn on beaches
            EntitySpawnListener.setSpawnEntities(true);
        }));
        addCommand("temp", new SimpleCommand(Main.getInstance(), player -> {
            player.getInventory()
                    .addItem(new Item(
                            Material.IRON_HOE,
                            "Gun",
                            "Click to fire. Wait " + GunState.GUN_COOLDOWN + " seconds after firing and after reloading."
                    ));
            player.getInventory()
                    .addItem(new Item(
                            Material.IRON_NUGGET,
                            "Bullet"
                    ).withAmount(100));
        }));
        addCommand("discord", new SimpleCommand(Main.getInstance(), player -> {
            PlayerUtil.message(player, " ");
            PlayerUtil.message(player, " ");
            PlayerUtil.message(player, "&8-");
            player.sendMessage(Component.text("Click here to join our Discord server:").color(NamedTextColor.GRAY));
            player.sendMessage(Component.text("https://discord.gg/bmm9UfdgY5")
                    .decorate(TextDecoration.UNDERLINED)
                    .color(NamedTextColor.BLUE)
                    .clickEvent(ClickEvent.openUrl("https://discord.gg/bmm9UfdgY5")));
            PlayerUtil.message(player, "&8-");
            PlayerUtil.message(player, " ");
            PlayerUtil.message(player, " ");
        }));

    }

    private static void addCommand(String name, CommandExecutor executor) {

        PluginCommand command = Main.getInstance().getCommand(name);
        if (command == null) {
            Bukkit.getLogger().info("Command " + name + " failed to be added, maybe it isn't in the plugin.yml?");
            return;
        }
        command.setExecutor(executor);
        Bukkit.getLogger().info("\""
                + command.getUsage().replace("<command>", name)
                + "\" command was registered.");

    }

    private static void setupPlayer(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100000, 10, false, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100000, 10, false, false, false));
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().clear();
        ItemStack item = new ItemStack(Material.IRON_SWORD);
        Damageable meta = (Damageable) item.getItemMeta();
        item.setItemMeta(configureMeta(meta, "Dagger", true, false, 200));
        player.getInventory().setItem(0, item);
    }

    public static Damageable configureMeta(Damageable meta, String name, boolean clean, boolean rusty, int damage) {
        meta.setDamage(damage);
        Component component = Component.text("");
        if (clean)
            component = component.append(Component.text("§fClean "));
        else
            component = component.append(Component.text("§fBloody "));
        if (rusty)
            component = component.append(Component.text("§fRusty "));
        component = component.append(Component.text(name));

        meta.displayName(component);
        meta.getPersistentDataContainer().set(new NamespacedKey(Main.getInstance(), "clean"), PersistentDataType.BYTE, clean ? (byte) 1 : (byte) 0);
        meta.getPersistentDataContainer().set(new NamespacedKey(Main.getInstance(), "rusty"), PersistentDataType.BYTE, rusty ? (byte) 1 : (byte) 0);
        meta.getPersistentDataContainer().set(new NamespacedKey(Main.getInstance(), "damaged"), PersistentDataType.BYTE, damage == 0 ? (byte) 0 : (byte) 1);

        return meta;
    }

}
