package com.github.nightdev.kitEngine.api;

import com.github.nightdev.kitEngine.KitEngine;
import com.github.nightdev.kitEngine.kits.LayoutManager;
import com.github.nightdev.kitEngine.kits.obj.Kit;
import com.github.nightdev.kitEngine.kits.obj.meta.KitGroup;
import com.github.nightdev.kitEngine.menus.KitAdminMenu;
import com.github.nightdev.kitEngine.menus.KitEditorMenu;
import com.github.nightdev.kitEngine.menus.KitsMenu;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import javax.naming.InvalidNameException;
import java.util.Random;

public interface Menu<T> extends InventoryHolder, Listener {
    KitEngine plugin = KitEngine.getInstance();

    T register(KitEngine plugin);

    default void onClick(InventoryClickEvent event, Player player, Inventory inv, ItemStack item, T menu) {
        event.setCancelled(true);
    }
    default void onOpen(InventoryOpenEvent event, Player player, Inventory inv, T menu) {

    }
    default void onClose(InventoryCloseEvent event, Player player, Inventory inv, T menu) {
        event.getView().setCursor(null);
    }
    default void onDrag(InventoryDragEvent event, Player player, Inventory inv, T menu) {
        event.setCancelled(true);
    }

    static NamespacedKey key(String key) {
        return new NamespacedKey(plugin, key);
    }
    static NamespacedKey random() {
        return new NamespacedKey(plugin, "item_" + new Random().nextInt(Integer.MAX_VALUE));
    }
    default boolean isItem(NamespacedKey key, ItemStack item) {
        if (item == null) return false;
        return item.getPersistentDataContainer().has(key);
    }

    static void openKitsMenu(Player player, KitGroup group, int page) {
        player.openInventory(
                new KitsMenu(player, group, page)
                        .register(Menu.plugin)
                        .getInventory()
        );
    }
    static void openKitEditorMenu(Player player, Kit kit) {
        if (!LayoutManager.hasLayout(player, kit)) {
            player.openInventory(new KitEditorMenu(kit)
                    .register(KitEngine.getInstance())
                    .getInventory());
        } else {
            player.openInventory(new KitEditorMenu(kit, LayoutManager.getLayout(player, kit))
                    .register(KitEngine.getInstance())
                    .getInventory());
        }
    }
    static void openKitAdminMenu(Player player, Kit kit) {
        player.openInventory(
                new KitAdminMenu(kit)
                        .register(Menu.plugin)
                        .getInventory()
        );
    }
}
