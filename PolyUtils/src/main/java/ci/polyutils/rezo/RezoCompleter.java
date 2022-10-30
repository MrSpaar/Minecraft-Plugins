package ci.polyutils.rezo;

import ci.polyutils.DBHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;

public class RezoCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player))
            return Collections.emptyList();

        if (args.length == 1 && sender.isOp())
            return matchPartial(args[0], List.of("join", "tp", "leaderboard", "give", "sync", "reset"));

        if (args.length == 1)
            return matchPartial(args[0], List.of("join", "tp", "leaderboard"));

        if ((args[0].equals("join") && args.length == 2) || args[0].equals("tp"))
            return matchPartial(args[1], player.getScoreboard().getTeams().stream().map(Team::getName).toList());

        if (sender.isOp() && args[0].equals("give") && args.length == 2) {
            try {
                ResultSet entries = DBHandler.getTowns();
                List<String> towns = new ArrayList<>();

                if (entries == null)
                    return Collections.emptyList();

                while (entries.next())
                    towns.add(entries.getString("id"));

                return matchPartial(args[1], towns);
            } catch (SQLException e) {
                Bukkit.getLogger().log(Level.SEVERE, "Error in town autocomplete");
            }
        }

        return Collections.emptyList();
    }

    private List<String> matchPartial(String arg, List<String> completions) {
        List<String> res = new ArrayList<>();
        StringUtil.copyPartialMatches(arg, completions, res);
        return res;
    }
}