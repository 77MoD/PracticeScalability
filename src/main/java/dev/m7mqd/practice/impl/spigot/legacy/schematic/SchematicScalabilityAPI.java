package dev.m7mqd.practice.impl.spigot.legacy.schematic;

import dev.m7mqd.practice.api.base.Arena;
import dev.m7mqd.practice.api.base.ScalabilityAPI;
import dev.m7mqd.practice.api.base.ScalabilityType;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.util.concurrent.CompletableFuture;

public class SchematicScalabilityAPI implements ScalabilityAPI {
    @Override
    public CompletableFuture<Arena> createEntry() {
        return null;
    }

    @Override
    public CompletableFuture<Arena> create(@NonNull Arena entry) {
        return null;
    }

    @Override
    public CompletableFuture<Arena> delete(@NonNull Arena entry) {
        return null;
    }

    @Override
    public ScalabilityType getType() {
        return ScalabilityType.SCHEMATICS;
    }

    @Override
    public String getImplementation() {
        return "FAWESchematics";
    }

    @Override
    public void load() {
        Bukkit.getLogger().info("Creating FAWEAreas world...");
        World world = new WorldCreator("FAWEAreas").createWorld();
        Bukkit.getLogger().info("Successfully created FAWEAreas world.");
    }
}
