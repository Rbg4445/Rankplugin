package me.rbg.rankplugin;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;

public class TabFormatter implements Listener {
    private final RankManager rankManager;

    public TabFormatter(RankManager manager) {
        this.rankManager = manager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String rank = rankManager.getRank(player);
        String symbol = rankManager.getSymbol(rank);

        player.setPlayerListName(symbol + " " + player.getName());
    }
}
