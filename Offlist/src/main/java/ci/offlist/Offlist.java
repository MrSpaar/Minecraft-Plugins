package ci.offlist;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Offlist extends JavaPlugin {
    @Override
    public void onEnable() {
        PluginCommand command = getCommand("offlist");

        if (command == null) {
            System.out.println("Offlist command not found, check plugin.yml");
            return;
        }

        command.setExecutor(new OfflistExecutor());
        command.setTabCompleter(new OfflistCompleter());
    }
}
