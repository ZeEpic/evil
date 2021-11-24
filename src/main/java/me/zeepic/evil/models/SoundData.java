package me.zeepic.evil.models;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public record SoundData(Sound sound, SoundCategory category, float volume, float pitch) {

    public void play(Player player) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

}
