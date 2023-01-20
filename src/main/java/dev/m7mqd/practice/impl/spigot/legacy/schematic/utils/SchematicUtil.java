package dev.m7mqd.practice.impl.spigot.legacy.schematic.utils;

import com.boydti.fawe.object.schematic.Schematic;
import com.boydti.fawe.util.TaskManager;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.function.mask.ExistingBlockMask;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.registry.WorldData;
import dev.m7mqd.practice.api.utils.Position;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class SchematicUtil {
    public static Vector getDims(Schematic schematic){
        if(schematic == null) return null;
        return getDims(Objects.requireNonNull(schematic.getClipboard(), "Clipboard is null"));
    }
    public static Vector getDims(@NonNull Clipboard clipboard){
        return clipboard.getDimensions();
    }
    public static int getBlock(@NonNull Schematic schematic, Position holder, Position min){
        return getBlock(Objects.requireNonNull(schematic.getClipboard(), "Clipboard is null"), holder, min);
    }
    public static int getBlock(@NonNull Clipboard clipboard, Position holder, Position min){
        double x = holder.getX() - min.getX() ;
        double y = holder.getY() - min.getY();
        double z = holder.getZ() - min.getZ();
        return clipboard.getBlock(new Vector(x, y ,z)).getType();
    }
    public static boolean hasBlock(@NonNull Schematic schematic, Position holder, Position min){
        return hasBlock(schematic.getClipboard(), holder, min);
    }
    public static boolean hasBlock(Clipboard clipboard, Position holder, Position min){
        return getBlock(clipboard, holder, min) != 0;
    }
    public static EditSession paste(@NonNull Schematic schematic, @NonNull org.bukkit.World bukkitWorld, @NonNull Position loc) {
        return paste(schematic, new BukkitWorld(bukkitWorld), new Vector(loc.getX(), loc.getY(), loc.getZ()));
    }
    public static EditSession paste(@NonNull Schematic schematic, @NonNull World weWorld, @NonNull Vector loc) {
        Vector vec = new Vector(loc.getX(), loc.getY(), loc.getZ());
        // first boolean: allowUndo?
        // second boolean: pasteAir?
        // null is transforms which is rotate etc...
        return schematic.paste(weWorld, vec, true, false, null);
    }
    public static Optional<Schematic> load(File dataFolder, String schematicName) {
        File file = new File(dataFolder + File.separator + "schematics" + File.separator + schematicName + ".schematic");
        if (!file.exists()) {
            return Optional.empty();
        }
        try {
            ClipboardFormat clipboardFormat = ClipboardFormats.findByFile(file);
            if (clipboardFormat == null) {
                return Optional.empty();
            }
            return Optional.of(clipboardFormat.load(file));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }
    public static CompletableFuture<BlockArrayClipboard> save(File dataFolder, String schematicName, Vector firstVec, Vector lastVec, org.bukkit.World world) {
        CompletableFuture<BlockArrayClipboard> completableFuture = new CompletableFuture<>();
        TaskManager.IMP.async(() -> {//same to Bukkit.getScheduler().runTaskAsynchronously
            World weWorld = new BukkitWorld(world);
            WorldData worldData = weWorld.getWorldData();
            CuboidRegion cuboidRegion = new CuboidRegion(weWorld, firstVec, lastVec);
            File folder = new File(dataFolder, "schematics");
            folder.mkdirs();
            File file = new File(folder, schematicName + ".schematic");
            try {
                file.delete();
                file.createNewFile();
                BlockArrayClipboard clipboard = new BlockArrayClipboard(cuboidRegion);
                Extent source = WorldEdit.getInstance().getEditSessionFactory().getEditSession(weWorld, -1);
                ForwardExtentCopy copy = new ForwardExtentCopy(source, cuboidRegion, clipboard.getOrigin(), clipboard, firstVec);
                copy.setSourceMask(new ExistingBlockMask(source));
                Operations.completeLegacy(copy);
                completableFuture.complete(clipboard);
                ClipboardFormat.SCHEMATIC.getWriter(Files.newOutputStream(file.toPath())).write(clipboard, worldData);
            } catch (IOException | MaxChangedBlocksException ex) {
                completableFuture.completeExceptionally(ex);
            }
        });
        return completableFuture;
    }
    public static boolean exists(File dataFolder, String schematicName){
        File schematic = new File(dataFolder,  File.separator + "schematics" + File.separator + schematicName+ ".schematic");
        return schematic.exists();
    }
}
