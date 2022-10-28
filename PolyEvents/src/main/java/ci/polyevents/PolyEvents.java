package ci.polyevents;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class PolyEvents extends JavaPlugin {
    @Override
    public void onEnable() {
        PluginCommand eventsCommand = getCommand("events");

        if (eventsCommand == null) {
            System.out.println("Commands not found, check plugin.yml");
            return;
        }

        eventsCommand.setExecutor(new EventExecutor());
        eventsCommand.setTabCompleter(new EventCompleter());

        DBHandler.initDB();
    }

    @Override
    public void onDisable() {
        DBHandler.closeDB();
    }

    public static boolean errorOut(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.RED + message);
        return true;
    }
}
