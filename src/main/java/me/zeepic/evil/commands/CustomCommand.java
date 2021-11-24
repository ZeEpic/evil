package me.zeepic.evil.commands;

import lombok.Getter;
import me.zeepic.evil.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class CustomCommand implements CommandExecutor {

    @Getter
    private final Main plugin;
    @Getter private final int requiredArgs;

    public CustomCommand(Main plugin, int requiredArgs) {
        this.plugin = plugin;
        this.requiredArgs = requiredArgs;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!(sender instanceof Player))
            return true;
        if (args.length < requiredArgs)
            return false;

        return executeCommand((Player) sender, args);

    }

    protected abstract boolean executeCommand(Player sender, String[] args);

}
