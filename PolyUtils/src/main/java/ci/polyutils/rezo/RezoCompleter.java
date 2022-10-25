package ci.polyutils.rezo;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class RezoCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1)
            return List.of("join");

        System.out.println(Arrays.toString(args));

        if (sender instanceof Player player && args[0].equals("join"))
            return player.getScoreboard().getTeams().stream().map(Team::getName).toList();

        return null;
    }
}