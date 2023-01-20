package dev.m7mqd.practice.impl.spigot.legacy.schematic.commands;

import com.google.common.base.Joiner;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import dev.m7mqd.practice.impl.spigot.legacy.schematic.utils.SchematicUtil;
import dev.m7mqd.practice.impl.spigot.legacy.utils.TextHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class CreateCommand implements CommandExecutor {
    private final Joiner joiner = Joiner.on('-');
    private final File dataFolder;
    private final WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
    public CreateCommand(Plugin plugin){
        this.dataFolder= plugin.getDataFolder();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command  cmd, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(TextHelper.format("&cYou're not allowed to use this command"));
            return true;
        }
        Player player = (Player)sender;
        if(args.length == 0){
            player.sendMessage(TextHelper.format("&cPlease provide a schematic name. Usage: /CreateSchematic <schematic name>"));
            return true;
        }
        String schematicName = joiner.join(args);
            Selection selection = worldEdit.getSelection(player);
            if(!(selection instanceof CuboidSelection)){
                player.sendMessage(TextHelper.format("&cPlease select an cuboid area using WorldEdit/FAWE."));
                return true;
            }
            player.sendMessage(TextHelper.format("&eCreating the &6" + schematicName + " &eschematic..."));
            Location max = selection.getMaximumPoint();
            Location min = selection.getMinimumPoint();
            SchematicUtil.save(dataFolder, schematicName, new Vector(min.getX(), min.getY(), min.getZ()), new Vector(max.getX(), max.getY(), max.getZ()), player.getWorld()).whenComplete((completed, err) -> {
                if(err != null){
                    player.sendMessage(TextHelper.format("&cCould not save the schematic, Error: " + err.getMessage()));
                    return;
                }
                player.sendMessage(TextHelper.format("&aSuccessfully created the " + schematicName + "&a schematic."));
            });

        return true;
    }
}
