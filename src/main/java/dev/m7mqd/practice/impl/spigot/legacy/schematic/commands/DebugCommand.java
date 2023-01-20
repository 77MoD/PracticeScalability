package dev.m7mqd.practice.impl.spigot.legacy.schematic.commands;

import com.boydti.fawe.object.schematic.Schematic;
import dev.m7mqd.practice.api.utils.Position;
import dev.m7mqd.practice.impl.spigot.legacy.schematic.utils.SchematicUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Optional;

public class DebugCommand implements CommandExecutor {
    private final File dataFolder;
    public DebugCommand(Plugin plugin){
        this.dataFolder = plugin.getDataFolder();
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Optional<Schematic> schematic = SchematicUtil.load(dataFolder, "testSchem");
        if(schematic.isPresent()) {
            SchematicUtil.paste(schematic.get(), ((Player) commandSender).getWorld(), new Position(5, 5, 5));
        }else{
            commandSender.sendMessage("The schematic doesn't exist!");
        }
        return true;
    }
}
