package com.github.nightdev.kitEngine.guis;

import com.github.nightdev.kitEngine.KitEngine;
import com.github.nightdev.kitEngine.core.KitEngineItems;
import com.github.nightdev.kitEngine.manager.KitsManager;
import com.github.nightdev.kitEngine.manager.obj.Kit;
import com.github.nightdev.kitEngine.utils.KitUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class KitsGui implements InventoryHolder, Listener {
    private final int page;

    public KitsGui(int page) {
        this.page = page;
    }

    @Override
    public @NotNull Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, 6 * 9, KitUtils.format("Kits (Page " + page + ")"));

        for (int i = inv.getSize() - 9; i < inv.getSize(); i++) {
            inv.setItem(i, KitEngineItems.backgroundItem());
        }
        if (page > 1) {
            inv.setItem(48, KitEngineItems.backPageItem(""));
        }
        if (page < KitEngine.getInstance().getConfig().getInt("max-pages")) {
            inv.setItem(50, KitEngineItems.nextPageItem(""));
        }

        for (String kitName : KitsManager.kits()) {
            Kit kit = KitsManager.kit(kitName);
            int slot = kit.meta.slot();

            if (slot >= minSlot() && slot <= maxSlot()) {
                int realSlot = realSlot(slot);
                inv.setItem(realSlot, KitEngineItems.kitItem(kitName, kit));
            }
        }

        return inv;
    }

    public int minSlot() {
        return (44 * page) - 44;
    }
    public int maxSlot() {
        return (44 * page);
    }
    public int realSlot(int slot) {
        while (slot > 44) {
            slot -= 44;
        }
        return slot;
    }

    @EventHandler
    public void on(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTopInventory().getHolder() instanceof KitsGui gui) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) return;
            ItemStack item = event.getCurrentItem();
            if (item == null) return;

            if (KitEngineItems.isItem(KitEngineItems.KIT_ITEM_KEY, item)) {
                String kitName = KitEngineItems.kit(item);
                if (event.getClick() == ClickType.LEFT) {
                    KitsManager.load(kitName, player);
                    player.closeInventory();
                } else if (event.getClick() == ClickType.RIGHT) {
                    player.openInventory(new KitEditorGui(player, kitName).getInventory());
                }
            }

            else if (KitEngineItems.isItem(KitEngineItems.NEXT_PAGE_KEY, item)) {
                player.openInventory(new KitsGui(gui.page + 1).getInventory());
            }
            else if (KitEngineItems.isItem(KitEngineItems.BACK_PAGE_KEY, item)) {
                player.openInventory(new KitsGui(gui.page - 1).getInventory());
            }

        }
    }
}
