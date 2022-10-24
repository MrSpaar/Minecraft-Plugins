package ci.polyutils.rezo;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RezoCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of(
                "Nancy", "Nice",
                "Lille", "Lyon",
                "Chambérry", "Grenoble",
                "Montpellier", "Tours",
                "Angers", "Nantes",
                "Clermont-Ferrand",
                "Orléans", "Marseille",
                "Saclay",  "Sorbonne"
        );
    }
}