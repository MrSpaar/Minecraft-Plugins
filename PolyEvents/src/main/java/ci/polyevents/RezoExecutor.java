package ci.polyevents;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

public class RezoExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0)
            return false;

        Player player = (Player) sender;
        Scoreboard scoreboard = player.getScoreboard();

        if (scoreboard.getPlayerTeam(player) != null) {
            player.sendMessage(ChatColor.RED + "Tu fais déjà partie d'une ville, si c'est la mauvaise, contacte un modérateur ;)");
            return true;
        }

        Team team = scoreboard.getTeam(args[0]);

        if (team == null) {
            player.sendMessage(ChatColor.RED + "La ville \"" + args[0] + "\" est introuvable");
            return true;
        }

        team.addPlayer(player);
        player.sendMessage(ChatColor.GREEN + "Tu as rejoint la ville \"" + args[0] + "\"");

        return true;
    }
}
