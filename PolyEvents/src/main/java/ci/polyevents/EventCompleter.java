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
        if (args.length == 1 && sender.isOp()) return List.of("list", "join", "leave", "create", "delete");
        if (args.length == 1) return List.of("list", "join", "leave");

        List<String> autoComplete = new ArrayList<>();

        try {
            ResultSet events = null;

            if (args[0].equals("join") || args[0].equals("delete"))
                events = EventHandler.getEvents();
            else if (args[0].equals("leave"))
                events = EventHandler.getPlayerByName(sender.getName());

            if (events == null)
                return autoComplete;

            while (events.next())
                autoComplete.add(events.getString("event_id"));
            events.close();
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.WARNING, "Error while autocompleting command " + command.getName());
        }

        return autoComplete;
    }
}
