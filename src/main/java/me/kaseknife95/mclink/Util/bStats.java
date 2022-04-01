package me.kaseknife95.mclink.Util;

import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Callable;

public class bStats {
    JavaPlugin plugin;

    public bStats(JavaPlugin plugin) {
        this.plugin = plugin;
        int pluginId = 14540;

        Metrics metrics = new Metrics(this.plugin, pluginId);
        Logger.d("Connected to bStats");

        metrics.addCustomChart(new SingleLineChart("players", new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return Bukkit.getOnlinePlayers().size();
            }
        }));
            }
}
