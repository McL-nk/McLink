package me.kaseknife95.mclink;

import me.kaseknife95.mclink.Commands.Setup;
import me.kaseknife95.mclink.Util.Logger;
import me.kaseknife95.mclink.Util.PlayerEvents;
import me.kaseknife95.mclink.Util.bStats;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static me.kaseknife95.mclink.Util.Logger.d;
import static me.kaseknife95.mclink.Websocket_Handler.Emit;

public final class McLink extends JavaPlugin {
    Websocket_Handler WShandler;
    File file = new File(this.getDataFolder()+File.separator+"Config.yml");
    public static Logger logger;

    @Override
    public void onEnable() {

        //Setup event listeners
        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);

        //Setup commands
        this.getCommand("Setup").setExecutor(new Setup(this));

        //Setup bStats
        new bStats(this);

        //Setup config file
        if(!this.getDataFolder().exists()){
            this.getDataFolder().mkdir();
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
                UUID id = UUID.randomUUID();
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                config.set("Server_UUID", String.valueOf(id));
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    //Setup Websocket Handler
    this.WShandler = new Websocket_Handler(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
