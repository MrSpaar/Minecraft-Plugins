package ci.polyutils.rezo;

import ci.polyutils.DBHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class RezoCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player))
            return Collections.emptyList();

        if (args.length == 1 && sender.isOp())
            return List.of("join", "leaderboard", "give", "sync", "reset");

        if (args.length == 1)
            return List.of("join", "leaderboard");

        if (args[0].equals("join"))
            return player.getScoreboard().getTeams().stream().map(Team::getName).toList();

        if (sender.isOp() && args[0].equals("give") && args.length == 2) {
            try {
                ResultSet entries = DBHandler.getTowns();
                ArrayList<String> towns = new ArrayList<>();

                if (entries == null)
                    return Collections.emptyList();

                while (entries.next())
                    towns.add(entries.getString("id"));

                return towns;
            } catch (SQLException e) {
                Bukkit.getLogger().log(Level.SEVERE, "Error in town autocomplete");
            }
        }

        return Collections.emptyList();
    }
}