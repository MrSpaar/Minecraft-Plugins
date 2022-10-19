package ci.polyevents;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class PolyEvents extends JavaPlugin {
    @Override
    public void onEnable() {
        PluginCommand command = getCommand("events");

        if (command == null) {
            System.out.println("Event command not found, check plugin.yml");
            return;
        }

        command.setExecutor(new EventExecutor());
        command.setTabCompleter(new EventCompleter());

        EventHandler.initDB();
    }

    @Override
    public void onDisable() {
        EventHandler.closeDB();
    }
}
