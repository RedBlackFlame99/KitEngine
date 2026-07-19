package com.github.nightdev.kitEngine.guis.input;

import com.github.nightdev.kitEngine.core.KitEngineConfig;
import com.github.nightdev.kitEngine.core.KitEngineItems;
import com.github.nightdev.kitEngine.guis.KitAdminEditorGui;
import com.github.nightdev.kitEngine.guis.KitsGui;
import com.github.nightdev.kitEngine.manager.KitsManager2;
import com.github.nightdev.kitEngine.utils.KitUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SlotInputListener implements InventoryHolder, Listener {
    private final String kitName;
    private final int page;

    public SlotInputListener(String kitName, int page) {
        this.kitName = kitName;
        this.page = page;
    }

    @Override
    public @NotNull Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, 6 * 9, KitUtils.format("Pick a slot!"));
        for (int i = inv.getSize() - 9; i < inv.getSize(); i++) {
            inv.setItem(i, KitEngineItems.backgroundItem());
        }
        if (page > 1) {
            inv.setItem(48, KitEngineItems.backPageItem(""));
        }
        if (page < KitEngineConfig.getMaxPages()) {
            inv.setItem(50, KitEngineItems.nextPageItem(""));
        }

        int min = KitsGui.minSlot(this.page);
        int max = KitsGui.maxSlot(this.page);
        for (int i = min; i <= max; i++) {
            int realSlot = KitsGui.realSlot(i, this.page);
            inv.setItem(realSlot, KitEngineItems.adminUpdateSlotInputItem(i));
        }

        return inv;
    }

    @EventHandler
    public void on(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getView().getTopInventory();
        if (inv.getHolder() instanceof SlotInputListener slotInputGui) {
            ItemStack item = event.getCurrentItem();
            if (item == null) return;

            event.setCancelled(true);

            if (KitEngineItems.isItem(KitEngineItems.NEXT_PAGE_KEY, item)) {
                player.openInventory(new SlotInputListener(
                        slotInputGui.kitName,
                        slotInputGui.page + 1
                ).getInventory());
            } else if (KitEngineItems.isItem(KitEngineItems.BACK_PAGE_KEY, item)) {
                player.openInventory(new SlotInputListener(
                        slotInputGui.kitName,
                        slotInputGui.page - 1
                ).getInventory());
            } else if (KitEngineItems.isItem(KitEngineItems.ADMIN_UPDATE_SLOT_INPUT_KEY, item)) {
                int slot = KitEngineItems.slotInput(item);
                KitsManager2.updateMeta(slotInputGui.kitName, meta -> {
                    meta.slot = slot;
                });
                player.openInventory(new KitAdminEditorGui(slotInputGui.kitName).getInventory());
            }
        }
    }
}
