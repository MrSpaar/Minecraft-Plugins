package ci.polyevents;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class EventCompleter implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1 && sender.isOp()) return List.of("list", "join", "leave", "create", "delete", "give");
        if (args.length == 1) return List.of("list", "join", "leave");

        List<String> autoComplete = new ArrayList<>();

        try {
            ResultSet entries = switch (args[0]) {
                case "join", "delete" -> DBHandler.getEvents();
                case "leave" -> DBHandler.getPlayerByName(sender.getName());
                default -> null;
            };

            if (entries == null)
                return autoComplete;

            while (entries.next())
                autoComplete.add(entries.getString("id"));
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.WARNING, "Error while autocompleting command " + command.getName());
        }

        return autoComplete;
    }
}
