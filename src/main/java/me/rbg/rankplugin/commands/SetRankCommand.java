package me.rbg.rankplugin.commands;

import me.rbg.rankplugin.RankManager;
import me.rbg.rankplugin.RankPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class SetRankCommand implements CommandExecutor {
    private final RankManager rankManager;

    public SetRankCommand(RankManager manager) {
        this.rankManager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("rankplugin.setrank")) {
            sender.sendMessage("§cYou don’t have permission!");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage("§cUsage: /setrank <player> <rank> [time]");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        String rank = args[1].toLowerCase();
        long durationMillis = -1;

        if (args.length == 3) {
            durationMillis = parseTime(args[2]);
            if (durationMillis <= 0) {
                sender.sendMessage("§cInvalid time format! Use 1d, 2h, 30m.");
                return true;
            }
        }

        if (target == null) {
            sender.sendMessage("§cPlayer not found!");
            return true;
        }

        if (!rankManager.isValidRank(rank)) {
            sender.sendMessage("§cInvalid rank! Available: " + String.join(", ", rankManager.getAllRanks()));
            return true;
        }

        rankManager.setRank(target, rank, durationMillis);
        sender.sendMessage("§aSet rank of " + target.getName() + " to " + rank);
        target.sendMessage("§aYour rank has been set to " + rank);

        target.setPlayerListName(rankManager.getSymbol(rank) + " " + target.getName());
        return true;
    }

    private long parseTime(String input) {
        try {
            if (input.endsWith("d")) {
                return Long.parseLong(input.replace("d", "")) * 86400000L;
            } else if (input.endsWith("h")) {
                return Long.parseLong(input.replace("h", "")) * 3600000L;
            } else if (input.endsWith("m")) {
                return Long.parseLong(input.replace("m", "")) * 60000L;
            } else {
                return Long.parseLong(input) * 1000L;
            }
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
