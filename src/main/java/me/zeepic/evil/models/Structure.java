package me.zeepic.evil.models;

import com.github.shynixn.structureblocklib.api.enumeration.StructureRotation;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.nio.file.Path;

public record Structure(StructureRotation rotation,
                        Location          location,
                        Vector            size,
                        Path              path) { }
