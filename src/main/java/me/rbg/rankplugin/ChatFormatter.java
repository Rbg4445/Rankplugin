package me.rbg.rankplugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.entity.Player;

public class ChatFormatter implements Listener {
    private final RankManager rankManager;

    public ChatFormatter(RankManager manager) {
        this.rankManager = manager;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String rank = rankManager.getRank(player);
        String symbol = rankManager.getSymbol(rank);

        event.setFormat(symbol + " " + player.getName() + ": " + event.getMessage());
    }
}
