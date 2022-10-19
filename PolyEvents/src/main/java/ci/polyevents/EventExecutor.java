package ci.polyevents;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class EventExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) return false;

        if (args[0].equals("list")) {
            StringBuilder builder = new StringBuilder("Liste des évènements :\n");
            ResultSet events = EventHandler.getEvents();

            if (events == null) {
                sender.sendMessage(ChatColor.RED + "Aucun évènement en cours.");
                return true;
            }

            try {
                while (events.next()) {
                    builder.append(ChatColor.GREEN)
                            .append(ChatColor.BOLD)
                            .append(events.getString("event_id"))
                            .append(" : ")
                            .append(ChatColor.RESET)
                            .append(ChatColor.ITALIC)
                            .append(ChatColor.AQUA)
                            .append(events.getString("description"))
                            .append("\n");
                }

                if (builder.isEmpty())
                    sender.sendMessage(ChatColor.RED + "Aucun évènements en cours");
                else
                    sender.sendMessage(builder.toString());
            } catch (SQLException e) {
                Bukkit.getLogger().log(Level.SEVERE, "Error while listing events");
            }

            return true;
        }

        if (!(sender instanceof Player player) || args.length < 2)
            return false;

        if (args[0].equals("join")) {
            ResultSet event = EventHandler.getEvent(args[1]);

            if (event == null) {
                sender.sendMessage(ChatColor.RED + "L'évènement \"" + args[1] + "\" n'existe pas");
                return true;
            }

            try {
                String[] coords = event.getString("location").split(" ");
                Location loc = player.getLocation();

                EventHandler.updatePlayer(
                        player.getName(),
                        player.getWorld().getName(),
                        loc.getX()+" "+loc.getY()+" "+ loc.getZ(),
                        event.getString("event_id")
                );

                player.teleport(new Location(
                        player.getServer().getWorld(event.getString("world")),
                        Double.parseDouble(coords[0]),
                        Double.parseDouble(coords[1]),
                        Double.parseDouble(coords[2])
                ));

                player.sendMessage(ChatColor.GREEN + "Tu as rejoint l'évènement \"" + args[1] + "\"");
            } catch (SQLException e) {
                Bukkit.getLogger().log(Level.SEVERE, "Error while using join command");
            }

            return true;
        }

        if (args[0].equals("leave")) {
            ResultSet event = EventHandler.getPlayerByEvent(args[1], player.getName());

            if (event == null) {
                sender.sendMessage(ChatColor.RED + "Tu n'as pas rejoint l'évènement \"" + args[1] + "\"");
                return true;
            }

            try {
                String[] coords = event.getString("location").split(" ");

                player.teleport(new Location(
                        player.getServer().getWorld(event.getString("world")),
                        Double.parseDouble(coords[0]),
                        Double.parseDouble(coords[1]),
                        Double.parseDouble(coords[2])
                ));

                EventHandler.deletePlayer(args[1], player.getName());
                player.sendMessage(ChatColor.GREEN + "Tu as quitté l'évènement \"" + args[1] + "\"");
            } catch (SQLException e) {
                Bukkit.getLogger().log(Level.SEVERE, "Error while using leave command");
            }

            return true;
        }

        if (!player.isOp()) {
            player.sendMessage(ChatColor.RED + "Tu n'as pas la permission de faire ça !");
            return true;
        }

        if (args[0].equals("delete")) {
            EventHandler.deleteEvent(args[1]);
            player.sendMessage(ChatColor.GREEN + "L'évènement \"" + args[1] + "\" a été supprimé");
            return true;
        }

        if (args[0].equals("create")) {
            Location location = player.getLocation();
            StringBuilder description = new StringBuilder();

            for (int i=2; i<args.length; i++)
                description.append(args[i]).append(" ");

            EventHandler.createEvent(
                    args[1],
                    location.getWorld().getName(),
                    location.getX() + " " + location.getY() + " " + location.getZ(),
                    description.toString());

            player.sendMessage(ChatColor.GREEN + "L'évènement \"" + args[1] + "\" a été créé");
            return true;
        }

        return false;
    }
}
