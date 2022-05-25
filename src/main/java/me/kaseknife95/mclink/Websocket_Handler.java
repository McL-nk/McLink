package me.kaseknife95.mclink;


import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import me.kaseknife95.mclink.Util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.json.JSONException;
import org.json.JSONObject;
import org.bukkit.plugin.java.JavaPlugin;


import java.io.File;
import java.net.URI;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;


import static org.bukkit.Bukkit.getServer;

public class Websocket_Handler {

    private static final Socket socket = IO.socket(URI.create("http://localhost:3000"));
    static JavaPlugin plugin1;
    static String id;
    public Websocket_Handler(JavaPlugin plugin) {

     plugin1 = plugin;
         File file = new File(plugin1.getDataFolder()+File.separator+"Config.yml");
         FileConfiguration config = YamlConfiguration.loadConfiguration(file);
         this.id = config.getString("Server_UUID");
        socket.connect();
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Emit("Connect", "");
                Logger.d("Connected to WS server");
            }

        });

        //Execute commands on server from discord or whatever other 3rd party service sends the request
        socket.on("command", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String command = args[0].toString();
                ConsoleCommandSender console = getServer().getConsoleSender();
                Logger.d("Command executed from Socket");
                try {
                    boolean success = Bukkit.getScheduler().callSyncMethod( plugin1, new Callable<Boolean>() {
                        @Override
                        public Boolean call() {
                            return Bukkit.dispatchCommand( console, command );
                        }
                    } ).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // emit events to the websocket server from this plugin or other plugins using the built-in api
    public static void Emit(String event, String message){



        JSONObject json = new JSONObject();
        try {
            json.put("UUID", id);
            json.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        socket.emit(event, json);
    }

    public static void EmitObject(String event, JSONObject message){


        JSONObject json = new JSONObject();
        try {
            json.put("UUID", id);
            json.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        socket.emit(event, json);
    }

}
