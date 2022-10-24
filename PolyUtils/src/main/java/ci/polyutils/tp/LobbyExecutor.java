package ci.polyutils.tp;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LobbyExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        World lobby = sender.getServer().getWorld("lobby");

        if (lobby == null) {
            sender.sendMessage(ChatColor.RED + "Le monde \"lobby\" est introuvable");
            return true;
        }

        Location spawn = lobby.getSpawnLocation();
        Player player = (Player) sender;

        player.teleport(spawn);
        return true;
    }
}
