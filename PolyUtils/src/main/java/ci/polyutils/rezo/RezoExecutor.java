package ci.polyutils.rezo;

import ci.polyutils.DBHandler;
import ci.polyutils.PolyUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class RezoExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player))
            return PolyUtils.errorOut(sender, "Les commandes de ce plugin ne sont accessibles qu'aux joueurs");

        if (args.length == 0)
            return PolyUtils.errorOut(sender, "Tu n'as spécifié aucune sous-commande");

        if (args[0].equals("join")) {
            if (args.length < 2)
                return PolyUtils.errorOut(sender, "Tu n'as spécifié aucune ville");

            Server server = player.getServer();

            if (player.getScoreboard().getPlayerTeam(player) != null)
                return PolyUtils.errorOut(sender, "Tu fais déjà partie d'une ville, contacte un modérateur en cas d'erreur ;)");

            Team team = player.getScoreboard().getTeam(args[1]);

            if (team == null)
                return PolyUtils.errorOut(sender, "La ville \"" + args[1] + "\" est introuvable");

            team.addPlayer(player);
            server.dispatchCommand(
                    server.getConsoleSender(),
                    "rg addmember " + args[1] + " " + player.getName() + " -w " + player.getWorld().getName()
            );

            return PolyUtils.successOut(sender, ChatColor.GREEN + "Tu as rejoint la ville \"" + args[1] + "\"");
        }

        if (args[0].equals("leaderboard")) {
            try {
                ResultSet entries = DBHandler.getTowns();
                StringBuilder builder = new StringBuilder();

                if (entries == null)
                    return PolyUtils.errorOut(sender, "Aucune ville dans le classement");

                while (entries.next())
                    builder.append(ChatColor.GREEN)
                            .append(entries.getString("id"))
                            .append(ChatColor.AQUA)
                            .append(" - ")
                            .append(entries.getInt("score"))
                            .append("\n");

                return PolyUtils.successOut(sender, builder.toString());
            } catch (SQLException e) {
                Bukkit.getLogger().log(Level.SEVERE, "Error in leaderboard command");
            }
        }

        if (args[0].equals("give") && sender.isOp()) {
            if (args.length < 3)
                return PolyUtils.errorOut(sender, "Tu n'as spécifié de ville ou le nombre de points à donner");

            try {
                DBHandler.addPoints(args[1], Integer.parseInt(args[2]));
                return PolyUtils.successOut(
                        sender,
                        ChatColor.GREEN + args[1] + ChatColor.RESET + " a gagné " + ChatColor.GREEN + args[2] + ChatColor.RESET + " points"
                );
            } catch (NumberFormatException e) {
                return PolyUtils.errorOut(sender, "Le nombre de points doit être un nombre entier");
            }
        }

        if (args[0].equals("reset") && sender.isOp()) {
            DBHandler.resetPoints();
            return PolyUtils.successOut(sender, ChatColor.GREEN + "Le classement a été réinitialisé");
        }

        if (args[0].equals("sync") && sender.isOp()) {
            DBHandler.sync(player.getScoreboard().getTeams());
            return PolyUtils.successOut(sender, "La base de données et les teams ont été synchronisées");
        }

        String usage =
                ChatColor.GREEN + "/spawn" + ChatColor.AQUA + " - Se téléporter au spawn de son monde\n" +
                ChatColor.GREEN + "/lobby" + ChatColor.AQUA + " - Se téléporter au lobby\n" +
                ChatColor.GREEN + "/rezo join [ville]" + ChatColor.AQUA + " - Rejoindre le groupe de sa ville\n" +
                ChatColor.GREEN + "/rezo leaderboard" + ChatColor.AQUA + " - Afficher le classement des villes";

        if (sender.isOp())
            usage += "\n" +
                    ChatColor.GREEN + "/rezo reset" + ChatColor.AQUA + " - Réinitialiser le classement" +
                    ChatColor.GREEN + "/rezo give [points] [ville]" + ChatColor.AQUA + " - Donner des points à une ville" +
                    ChatColor.GREEN + "/rezo sync" + ChatColor.AQUA + " - Synchroniser les teams et la base de données";

        sender.sendMessage(usage);
        return true;
    }
}