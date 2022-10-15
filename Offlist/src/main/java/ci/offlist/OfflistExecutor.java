package ci.offlist;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class OfflistExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length < 1 || !sender.isOp())
            return  false;

        switch (args[0]) {
            case "add" -> {
                for (int i=1; i<args.length; i++) {
                    int index = OfflistHandler.getIndex(args[i]);

                    if (index == -1) {
                        OfflistHandler.addPlayer(args[i], sender);
                        sender.sendMessage(
                                ChatColor.GREEN + "Le joueur " + ChatColor.BLUE + ChatColor.BOLD + args[i] + ChatColor.RESET + ChatColor.GREEN + " a été ajouté à la whitelist"
                        );
                    } else
                        sender.sendMessage(
                                ChatColor.RED+"Le joueur "+ChatColor.BLUE+ChatColor.BOLD+args[i]+ChatColor.RESET+ChatColor.RED+" est déjà dans la whitelist"
                        );
                }

                return true;
            }
            case "remove" -> {
                for (int i=1; i<args.length; i++)
                    if (OfflistHandler.removePlayer(args[i], sender))
                        sender.sendMessage(
                                ChatColor.GRAY+"Le joueur "+ChatColor.BLUE+ChatColor.BOLD+args[i]+ChatColor.RESET+ChatColor.GRAY+" a été enlevé à la whitelist"
                        );
                    else
                        sender.sendMessage(
                                ChatColor.RED+"Le joueur "+ChatColor.BLUE+ChatColor.BOLD+args[i]+ChatColor.RESET+ChatColor.RED+" n'est pas dans la whitelist"
                        );

                return true;
            }
        }

        return false;
    }
}
