package ci.worldspawn;

import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

public final class Worldspawn extends JavaPlugin {
    @Override
    public void onEnable() {
        PluginCommand lobbyCommand = getCommand("lobby");
        PluginCommand spawnCommand = getCommand("spawn");

        if (lobbyCommand == null || spawnCommand == null) {
            System.out.println("Commands not found, check plugin.yml");
            return;
        }

        TabCompleter completer = new Completer();

        lobbyCommand.setExecutor(new LobbyExecutor());
        lobbyCommand.setTabCompleter(completer);

        spawnCommand.setExecutor(new SpawnExecutor());
        spawnCommand.setTabCompleter(completer);
    }
}
