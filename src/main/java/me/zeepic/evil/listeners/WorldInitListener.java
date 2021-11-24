package me.zeepic.evil.listeners;

import me.zeepic.evil.world.SimpleBlockPopulator;
import me.zeepic.evil.world.StructurePopulator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;

public class WorldInitListener implements Listener {

    @EventHandler
    public void onInit(WorldInitEvent event) {
        event.getWorld().getPopulators().clear();
        event.getWorld().getPopulators().add(new SimpleBlockPopulator());
    }

    @EventHandler
    public void onLoad(WorldLoadEvent event) {
        event.getWorld().getPopulators().clear();
        event.getWorld().getPopulators().add(new StructurePopulator());
        event.getWorld().getPopulators().add(new SimpleBlockPopulator());
    }

}
