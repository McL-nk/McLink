package me.kaseknife95.mclink.Commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class Setup implements CommandExecutor {
    private Plugin plugin;
    public Setup(Plugin plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "[McLink]: " + ChatColor.WHITE + "You cannot execute this command from the console.");
            return false;
        }

        File file = new File(this.plugin.getDataFolder()+File.separator+"Config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        String id = config.getString("Server_UUID");


        TextComponent message = new TextComponent("Click me");
        message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, id));
        message.setBold(true);
        message.setColor(net.md_5.bungee.api.ChatColor.AQUA);

        TextComponent message1 = new TextComponent("[McLink]: ");
        message1.setColor(net.md_5.bungee.api.ChatColor.AQUA);

        TextComponent message2 = new TextComponent("Here is your servers unique UUID, use the /setup command in your discord with our bot and this id to link your servers together! ");
        message2.setColor(net.md_5.bungee.api.ChatColor.WHITE);

        message1.addExtra(message2);
        message1.addExtra(message);

        sender.spigot().sendMessage(message1);
        return true;
    }
}
