package me.kaseknife95.mclink;

import me.kaseknife95.mclink.Commands.Setup;
import me.kaseknife95.mclink.Util.appender;
import me.kaseknife95.mclink.Util.bStats;
import me.kaseknife95.mclink.events.PlayerEvents;
import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.UUID;

import static java.lang.Thread.sleep;


public final class McLink extends JavaPlugin {
    Websocket_Handler WShandler = new Websocket_Handler(this);
    private static final org.apache.logging.log4j.core.Logger logger = (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();

    private static final String mcVer = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

private Thread thread;
    File file = new File(this.getDataFolder()+File.separator+"Config.yml");


    @Override
    public void onEnable() {
        me.kaseknife95.mclink.Util.appender appender = new appender(WShandler);
        logger.addAppender(appender);


        //Setup event listeners
        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);

        //Setup commands
        Objects.requireNonNull(this.getCommand("Setup")).setExecutor(new Setup(this));

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

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Started thread");
                do {
                    try {

                        sleep(1000);
                        Runtime r = Runtime.getRuntime();

                        long free = r.freeMemory() / 1024 / 1024;
                        long RAM_Max = r.maxMemory() / 1024 / 1024;
                        long RAM_Used = r.totalMemory() / 1024 / 1024 - free;

                        int maxPlayers = Bukkit.getMaxPlayers();
                        int OnlinePlayers = Bukkit.getOnlinePlayers().size();

                        double tps = getTps()[0];

                        double usage = getProcessCpuLoad();

                        JSONObject json = new JSONObject();
                        json.put("ram_max", RAM_Max);
                        json.put("ram_used", RAM_Used);
                        json.put("max_players", maxPlayers);
                        json.put("online_players", OnlinePlayers);

                        json.put("cpu_usage", usage);

                        WShandler.EmitObject("stats", json);

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                } while (true);
            }
        });
        thread.start();
    //Setup Websocket Handler

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public double[] getTps() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        try {
            Class<?> minecraftServerClass = Class.forName("net.minecraft.server." + mcVer + ".MinecraftServer");
            Method getServerMethod = minecraftServerClass.getDeclaredMethod("getServer");
            Object serverInstance = getServerMethod.invoke(null);
            Field recentTpsField = serverInstance.getClass().getField("recentTps");
            double[] recentTps = (double[]) recentTpsField.get(serverInstance);
            for (int i = 0; i < recentTps.length; i++) {
                recentTps[i] = Math.round(recentTps[i]);
            }
            return recentTps;
        } catch (Exception e) {
            //If an uncaught exception is thrown, maybe it is because this method of getting TPS does not work in the MV version currently running..
            return new double[] { 0 };
        }
    }

    public double getProcessCpuLoad() throws Exception {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = ObjectName.getInstance("java.lang:type=OperatingSystem");
        AttributeList list = mbs.getAttributes(name, new String[] {"SystemCpuLoad"});

        if (list.isEmpty())
            return Double.NaN;

        Attribute att = (Attribute) list.get(0);
        Double value = (Double) att.getValue();

        // usually takes a couple of seconds before we get real values
        if (value == -1.0)
            return Double.NaN;
        // returns a percentage value with 1 decimal point precision
        return ((int) (value * 1000) / 10.0);
    }
}
