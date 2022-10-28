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
        if (!(sender instanceof Player player))
            return PolyEvents.errorOut(sender, "La commande \"events\" n'est accessible qu'aux joueurs");

        if (args.length == 0)
            return PolyEvents.errorOut(sender, "Tu n'as spécifié aucune sous-commande");

        if (args[0].equals("list")) {
            StringBuilder builder = new StringBuilder("Liste des évènements :\n");
            ResultSet events = DBHandler.getEvents();

            if (events == null)
                return PolyEvents.errorOut(sender, "Aucun évènement en cours.");

            try {
                while (events.next()) {
                    builder.append(ChatColor.GREEN)
                            .append(ChatColor.BOLD)
                            .append(events.getString("id"))
                            .append(" : ")
                            .append(ChatColor.RESET)
                            .append(ChatColor.ITALIC)
                            .append(ChatColor.AQUA)
                            .append(events.getString("description"))
                            .append("\n");
                }

                if (builder.length() == 23)
                    sender.sendMessage(ChatColor.RED + "Aucun évènements en cours");
                else
                    sender.sendMessage(builder.toString());
            } catch (SQLException e) {
                Bukkit.getLogger().log(Level.SEVERE, "Error while listing events");
            }

            return true;
        }

        if (args[0].equals("join")) {
            if (args.length < 2)
                return PolyEvents.errorOut(sender, "Tu n'as spécifié aucun évènement à rejoindre");

            ResultSet event = DBHandler.getEvent(args[1]);

            try {
                if (event == null || !event.next())
                    return PolyEvents.errorOut(sender, "L'évènement \"" + args[1] + "\" n'existe pas");

                String[] coords = event.getString("location").split(" ");
                Location loc = player.getLocation();

                DBHandler.updatePlayer(
                        player.getName(),
                        player.getWorld().getName(),
                        loc.getX()+" "+loc.getY()+" "+ loc.getZ(),
                        event.getString("id")
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
            if (args.length < 2)
                return PolyEvents.errorOut(sender, "Tu n'as spécifié aucun évènement à quitter");

            ResultSet event = DBHandler.getPlayerByEvent(args[1], player.getName());

            try {
                if (event == null || !event.next())
                    return PolyEvents.errorOut(sender, "Tu n'as pas rejoint l'évènement \"" + args[1] + "\"");

                String[] coords = event.getString("location").split(" ");

                player.teleport(new Location(
                        player.getServer().getWorld(event.getString("world")),
                        Double.parseDouble(coords[0]),
                        Double.parseDouble(coords[1]),
                        Double.parseDouble(coords[2])
                ));

                DBHandler.deletePlayer(args[1], player.getName());
                player.sendMessage(ChatColor.GREEN + "Tu as quitté l'évènement \"" + args[1] + "\"");
            } catch (SQLException e) {
                Bukkit.getLogger().log(Level.SEVERE, "Error while using leave command");
            }

            return true;
        }

        if (args[0].equals("delete") && sender.isOp()) {
            if (args.length < 2)
                return PolyEvents.errorOut(sender, "Tu n'as spécifié aucun évènement à supprimer");

            DBHandler.deleteEvent(args[1]);
            player.sendMessage(ChatColor.GREEN + "L'évènement \"" + args[1] + "\" a été supprimé");
            return true;
        }

        if (args[0].equals("create") && sender.isOp()) {
            if (args.length < 2)
                return PolyEvents.errorOut(sender, "Tu n'as pas spécifié le nom de l'évènement à créer");

            Location location = player.getLocation();
            StringBuilder description = new StringBuilder();

            for (int i=2; i<args.length; i++)
                description.append(args[i]).append(" ");

            DBHandler.createEvent(
                    args[1],
                    location.getWorld().getName(),
                    location.getX() + " " + location.getY() + " " + location.getZ(),
                    description.toString());

            player.sendMessage(ChatColor.GREEN + "L'évènement \"" + args[1] + "\" a été créé");
            return true;
        }

        String usage =
                ChatColor.GREEN + "/events join [nom]" + ChatColor.AQUA + " - Rejoindre un évènement\n" +
                ChatColor.GREEN + "/events leave [nom]" + ChatColor.AQUA + " - Quitter un èvement\n" +
                ChatColor.GREEN + "/events list" + ChatColor.AQUA + " - Afficher la liste des évènements";

        if (sender.isOp())
            usage += "\n" +
                    ChatColor.GREEN + "/events create [nom] [description]" + ChatColor.AQUA + " - Créer un évènement à sa position\n" +
                    ChatColor.GREEN + "/events delete [nom]" + ChatColor.AQUA + " - Supprimer un évènement\n";

        sender.sendMessage(usage);
        return true;
    }
}
