package ci.polyevents;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class PolyEvents extends JavaPlugin {
    @Override
    public void onEnable() {
        PluginCommand eventsCommand = getCommand("events");
        PluginCommand rezoCommand = getCommand("rezo");

        if (eventsCommand == null || rezoCommand == null) {
            System.out.println("Plugin commands not found, check plugin.yml");
            return;
        }

        eventsCommand.setExecutor(new EventExecutor());
        eventsCommand.setTabCompleter(new EventCompleter());

        rezoCommand.setExecutor(new RezoExecutor());
        rezoCommand.setTabCompleter(new RezoCompleter());

        EventHandler.initDB();
    }

    @Override
    public void onDisable() {
        EventHandler.closeDB();
    }
}
