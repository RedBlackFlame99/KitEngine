package com.github.nightdev.kitEngine.kits;

import com.github.nightdev.kitEngine.kits.input.PlayerInputListener;
import com.github.nightdev.kitEngine.menus.KitAdminMenu;
import com.github.nightdev.kitEngine.menus.KitAdminSlotMenu;
import com.github.nightdev.kitEngine.menus.KitEditorMenu;
import com.github.nightdev.kitEngine.menus.KitsMenu;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class KitsListener implements Listener {
    @EventHandler
    public void on(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getView().getTopInventory();
        ItemStack item = event.getCurrentItem();
        if (inv.getHolder() instanceof KitsMenu menu) {
            menu.onClick(event, player, inv, item, menu);
        }
        else if (inv.getHolder() instanceof KitEditorMenu menu) {
            menu.onClick(event, player, inv, item, menu);
        }
        else if (inv.getHolder() instanceof KitAdminMenu menu) {
            menu.onClick(event, player, inv, item, menu);
        }
        else if (inv.getHolder() instanceof KitAdminSlotMenu menu) {
            menu.onClick(event, player, inv, item, menu);
        }
    }

    @EventHandler
    public void on(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getView().getTopInventory();
        if (inv.getHolder() instanceof KitEditorMenu menu) {
            menu.onDrag(event, player, inv, menu);
        }
    }

    @EventHandler
    public void on(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory inv = event.getView().getTopInventory();
        if (inv.getHolder() instanceof KitsMenu menu) {
            menu.onClose(event, player, inv, menu);
        }
        else if (inv.getHolder() instanceof KitEditorMenu menu) {
            menu.onClose(event, player, inv, menu);
        }
    }

    @EventHandler
    public void on(AsyncChatEvent event) {
        PlayerInputListener.handle(event, event.getPlayer(), toString(event.message()));
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {
        LayoutManager.unload(event.getPlayer());
    }

    public String toString(Component text) {
        return LegacyComponentSerializer.legacyAmpersand().serialize(text);
    }
}
