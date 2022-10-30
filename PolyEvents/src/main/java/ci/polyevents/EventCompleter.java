package ci.polyevents;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class EventCompleter implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1 && sender.isOp())
            return matchPartial(args[0], List.of("list", "join", "leave", "create", "delete"));

        if (args.length == 1)
            return matchPartial(args[0], List.of("list", "join", "leave"));

        try {
            List<String> autoComplete = new ArrayList<>();

            ResultSet entries = switch (args[0]) {
                case "join", "delete" -> DBHandler.getEvents();
                case "leave" -> DBHandler.getPlayerByName(sender.getName());
                default -> null;
            };

            if (entries == null)
                return autoComplete;

            while (entries.next())
                autoComplete.add(entries.getString("id"));

            return matchPartial(args[1], autoComplete);
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.WARNING, "Error while autocompleting command " + command.getName());
        }

        return Collections.emptyList();
    }

    private List<String> matchPartial(String arg, List<String> completions) {
        List<String> res = new ArrayList<>();
        StringUtil.copyPartialMatches(arg, completions, res);
        return res;
    }
}
