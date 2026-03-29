package me.rbg.rankplugin;

import me.rbg.rankplugin.commands.GUIManager;
import me.rbg.rankplugin.commands.SetRankCommand;
import me.rbg.rankplugin.listeners.ChatFormatter;
import me.rbg.rankplugin.listeners.TabFormatter;
import me.rbg.rankplugin.util.FontGenerator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class RankPlugin extends JavaPlugin {
    private static RankPlugin instance;
    private RankManager rankManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        saveResource("ranks.yml", false);

        rankManager = new RankManager(this);
        FontGenerator.generate(this, rankManager);

        // Commands
        getCommand("rankgui").setExecutor(new GUIManager(rankManager));
        getCommand("setrank").setExecutor(new SetRankCommand(rankManager));

        // Events
        getServer().getPluginManager().registerEvents(new GUIManager(rankManager), this);
        getServer().getPluginManager().registerEvents(new ChatFormatter(rankManager), this);
        getServer().getPluginManager().registerEvents(new TabFormatter(rankManager), this);

        // Süreli rank kontrolü
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (UUID uuid : rankManager.getPlayerRanks().keySet()) {
                RankData data = rankManager.getPlayerRanks().get(uuid);
                if (data != null && data.isExpired()) {
                    rankManager.getPlayerRanks().put(uuid, new RankData("player", -1));
                    Player p = Bukkit.getPlayer(uuid);
                    if (p != null) {
                        p.sendMessage("§cYour rank has expired. You are now a Player.");
                        p.setPlayerListName(rankManager.getSymbol("player") + " " + p.getName());
                    }
                }
            }
        }, 20L, 1200L); // 1 dk'da bir kontrol

        getLogger().info("RankPlugin başarıyla aktif edildi!");
    }

    @Override
    public void onDisable() {
        getLogger().info("RankPlugin kapatıldı!");
    }

    public static RankPlugin getInstance() {
        return instance;
    }

    public RankManager getRankManager() {
        return rankManager;
    }
}
