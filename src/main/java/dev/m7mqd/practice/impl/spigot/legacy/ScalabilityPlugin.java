package dev.m7mqd.practice.impl.spigot.legacy;

import dev.m7mqd.practice.api.base.ScalabilityAPI;
import dev.m7mqd.practice.api.base.ScalabilityType;
import dev.m7mqd.practice.impl.spigot.legacy.schematic.SchematicScalabilityAPI;
import dev.m7mqd.practice.impl.spigot.legacy.schematic.commands.CreateCommand;
import dev.m7mqd.practice.impl.spigot.legacy.schematic.commands.DebugCommand;
import dev.m7mqd.practice.impl.spigot.legacy.utils.TextHelper;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ScalabilityPlugin extends JavaPlugin implements Listener {
    private static ScalabilityAPI scalabilityAPI;
    private final static NotLoadedException NOT_LOADED_EXCEPTION = new NotLoadedException();
    private @Getter YamlConfiguration settings;
    @Override
    public void onEnable() {
        this.settings = reloadSettings();
        ScalabilityType scalabilityType = ScalabilityType.valueOf(settings.getString("scalability-type").toUpperCase());
        if(scalabilityType == ScalabilityType.SCHEMATICS){
            setScalabilityAPI(new SchematicScalabilityAPI());
        }
        if(ScalabilityPlugin.scalabilityAPI == null){
            log("&cYou're using a custom implementation of Scalability, but the implementation is not loaded yet!\nPlease use &aloadbefore: [PracticeScalability]&c in the custom implementation plugin's plugin.yml file.");
            return;
        }
        log("&a   _____           _       _     _ _ _ _         ");
        log("&a  / ____|         | |     | |   (_) (_) |        ");
        log("&a | (___   ___ __ _| | __ _| |__  _| |_| |_ _   _ ");
        log("&a  \\___ \\ / __/ _` | |/ _` | '_ \\| | | | __| | | |");
        log("&a  ____) | (_| (_| | | (_| | |_) | | | | |_| |_| |");
        log("&a |_____/ \\___\\__,_|_|\\__,_|_.__/|_|_|_|\\__|\\__, |");
        log("&a                                            __/ /");
        log("&a                                          |____/ ");
        log("&aRunning " + getScalabilityAPI().getImplementation() + " implementation of Scalability");
        this.getCommand("createschematic").setExecutor(new CreateCommand(this));
        this.getCommand("debugschematic").setExecutor(new DebugCommand(this));
        scalabilityAPI.load();
    }
    @Override
    public void onDisable() {
        getScalabilityAPI().unload();
    }
    private YamlConfiguration reloadSettings() {
        File settingsFile = new File(getDataFolder(), "settings.yml");
        if (!settingsFile.exists()) {
            settingsFile.getParentFile().mkdirs();
            saveResource("settings.yml", false);
        }
        return YamlConfiguration.loadConfiguration(settingsFile);
    }
    public static void log(String message){
        Bukkit.getConsoleSender().sendMessage(TextHelper.format(message));
    }
    public synchronized void setScalabilityAPI(ScalabilityAPI instance){
        ScalabilityPlugin.scalabilityAPI = instance;
    }
    public synchronized ScalabilityAPI getScalabilityAPI(){
        if(scalabilityAPI == null){
            throw NOT_LOADED_EXCEPTION;
        }
        return ScalabilityPlugin.scalabilityAPI;
    }
    private static final class NotLoadedException extends RuntimeException {
        private static final String MESSAGE = "The Scalability API instance isn't set/loaded yet!\nCommon cases that cause this problem: \n(1) The plugin in the stacktrace does not declare a dependency on Scalability \n(2) The plugin in the stacktrace is retrieving the API before the plugin 'enable' phase. \n(3) The server is using a custom Scalability type but it doesn't work. \n(call the #getScalabilityAPI method in onEnable, not the constructor!)\n";

        NotLoadedException() {
            super(NotLoadedException.MESSAGE);
        }
    }
}
