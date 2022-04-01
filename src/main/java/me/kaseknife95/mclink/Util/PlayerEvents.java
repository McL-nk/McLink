package me.kaseknife95.mclink.Util;

import me.kaseknife95.mclink.Websocket_Handler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.json.JSONException;
import org.json.JSONObject;

public class PlayerEvents implements Listener {

    //Player join event
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws JSONException {
        Player player  =  event.getPlayer();
        JSONObject json = new JSONObject();
        json.put("Player_Name",player.getDisplayName());
        json.put("Player_UUID", player.getUniqueId());
        Websocket_Handler.EmitObject("Player_Join", json);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) throws JSONException {
        Player player  =  event.getPlayer();
        JSONObject json = new JSONObject();
        json.put("Player_Name",player.getDisplayName());
        json.put("Player_UUID", player.getUniqueId());
        Websocket_Handler.EmitObject("Player_Leave", json);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) throws JSONException {
        Player player  =  event.getPlayer();
        JSONObject json = new JSONObject();
        json.put("Player_Name",player.getDisplayName());
        json.put("Player_UUID", player.getUniqueId());
        json.put("Message", event.getMessage());
        Websocket_Handler.EmitObject("Player_Chat", json);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) throws JSONException {
        Player player  =  event.getEntity().getPlayer();
        JSONObject json = new JSONObject();
        json.put("Player_Name",player.getDisplayName());
        json.put("Player_UUID", player.getUniqueId());
        json.put("Death_Message", event.getDeathMessage());
        Websocket_Handler.EmitObject("Player_Death", json);
    }

    @EventHandler
    public void onPlayerAchievement(PlayerAdvancementDoneEvent event) throws JSONException {
        Player player  =  event.getPlayer();
        JSONObject json = new JSONObject();
        json.put("Player_Name",player.getDisplayName());
        json.put("Player_UUID", player.getUniqueId());
        json.put("Achievement_Message", event.getAdvancement());
        Websocket_Handler.EmitObject("Player_Acheivement", json);
    }
}
