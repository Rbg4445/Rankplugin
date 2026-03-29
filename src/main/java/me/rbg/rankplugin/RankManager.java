package me.rbg.rankplugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class RankManager {
    private final RankPlugin plugin;
    private final Map<UUID, RankData> playerRanks = new HashMap<>();
    private final Map<String, String> rankSymbols = new HashMap<>();

    public RankManager(RankPlugin plugin) {
        this.plugin = plugin;
        loadRanks();
    }

    public void loadRanks() {
        File rankFile = new File(plugin.getDataFolder(), "ranks.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(rankFile);

        rankSymbols.clear();
        for (String rank : config.getConfigurationSection("ranks").getKeys(false)) {
            rankSymbols.put(rank.toLowerCase(), config.getString("ranks." + rank));
        }
    }

    public void setRank(Player player, String rank, long durationMillis) {
        long expireAt = durationMillis > 0 ? System.currentTimeMillis() + durationMillis : -1;
        playerRanks.put(player.getUniqueId(), new RankData(rank, expireAt));
    }

    public String getRank(Player player) {
        RankData data = playerRanks.get(player.getUniqueId());
        if (data == null) return "player";
        if (data.isExpired()) {
            playerRanks.put(player.getUniqueId(), new RankData("player", -1));
            return "player";
        }
        return data.getRank();
    }

    public boolean isValidRank(String rank) {
        return rankSymbols.containsKey(rank.toLowerCase());
    }

    public String getSymbol(String rank) {
        return rankSymbols.getOrDefault(rank.toLowerCase(), "§7");
    }

    public Set<String> getAllRanks() {
        return rankSymbols.keySet();
    }

    public Map<UUID, RankData> getPlayerRanks() {
        return playerRanks;
    }
}
