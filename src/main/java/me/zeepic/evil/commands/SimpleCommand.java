package me.zeepic.evil.commands;

import me.zeepic.evil.Main;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public final class SimpleCommand extends CustomCommand {

    private final Consumer<Player> effect;

    public SimpleCommand(Main plugin, Consumer<Player> effect) {
        super(plugin, 0);
        this.effect = effect;
    }

    @Override
    protected boolean executeCommand(Player sender, String[] args) {
        effect.accept(sender);
        return true;
    }

}
