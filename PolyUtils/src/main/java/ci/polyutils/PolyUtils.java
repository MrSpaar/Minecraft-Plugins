package ci.polyutils;

import ci.polyutils.tp.*;
import ci.polyutils.rezo.*;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

public final class PolyUtils extends JavaPlugin {

    @Override
    public void onEnable() {
        PluginCommand lobbyCommand = getCommand("lobby");
        PluginCommand spawnCommand = getCommand("spawn");
        PluginCommand rezoCommand = getCommand("rezo");

        if (lobbyCommand == null || spawnCommand == null || rezoCommand == null) {
            System.out.println("Commands not found, check plugin.yml");
            return;
        }

        TabCompleter emptyCompleter = new EmptyCompleter();

        lobbyCommand.setExecutor(new LobbyExecutor());
        lobbyCommand.setTabCompleter(emptyCompleter);

        spawnCommand.setExecutor(new SpawnExecutor());
        spawnCommand.setTabCompleter(emptyCompleter);

        rezoCommand.setExecutor(new RezoExecutor());
        rezoCommand.setTabCompleter(new RezoCompleter());
    }
}
