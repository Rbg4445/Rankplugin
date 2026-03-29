package me.rbg.rankplugin.commands;

import me.rbg.rankplugin.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.command.*;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.EventHandler;

public class GUIManager implements CommandExecutor, Listener {
    private final RankManager rankManager;

    public GUIManager(RankManager manager) {
        this.rankManager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;
        if (!player.isOp()) {
            player.sendMessage("§cOnly admins can use this!");
            return true;
        }

        Inventory gui = Bukkit.createInventory(null, 27, "Select Rank");

        for (String rank : rankManager.getAllRanks()) {
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§f" + rank);
            item.setItemMeta(meta);
            gui.addItem(item);
        }

        player.openInventory(gui);
        return true;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!event.getView().getTitle().equals("Select Rank")) return;

        event.setCancelled(true);
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) return;

        String rank = clicked.getItemMeta().getDisplayName().substring(2).toLowerCase();
        if (!rankManager.isValidRank(rank)) {
            player.sendMessage("§cInvalid rank!");
            return;
        }

        rankManager.setRank(player, rank);
        player.sendMessage("§aYour rank is now: " + rank);
        player.setPlayerListName(rankManager.getSymbol(rank) + " " + player.getName());
        player.closeInventory();
    }
}
