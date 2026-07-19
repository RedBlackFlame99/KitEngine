package com.github.nightdev.kitEngine.guis;

import com.github.nightdev.kitEngine.KitEngine;
import com.github.nightdev.kitEngine.core.KitEngineConfig;
import com.github.nightdev.kitEngine.core.KitEngineItems;
import com.github.nightdev.kitEngine.manager.KitMeta2;
import com.github.nightdev.kitEngine.manager.KitsManager2;
import com.github.nightdev.kitEngine.manager.result.KitResult;
import com.github.nightdev.kitEngine.utils.KitUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class KitsGui implements InventoryHolder, Listener {
    private final int page;
    private final Player player;
    private Inventory inventory;
    private BukkitTask task;

    public KitsGui(int page, Player player) {
        this.page = page;
        this.player = player;
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
        if (page < KitEngineConfig.getMaxPages()) {
            inv.setItem(50, KitEngineItems.nextPageItem(""));
        }

        task = new BukkitRunnable() {
            @Override
            public void run() {
                refreshItems(inv);
            }
        }.runTaskTimer(KitEngine.getInstance(), 0, 20);

        return inv;
    }

    private void refreshItems(Inventory inv) {
        for (String kitName : KitsManager2.getKits()) {
            KitMeta2 meta = KitsManager2.loadKitMeta(kitName);
            int slot = meta.slot;

            if (slot >= minSlot(this.page) && slot <= maxSlot(this.page)) {
                int realSlot = realSlot(slot, this.page);
                inv.setItem(realSlot, KitEngineItems.kitItem(kitName, this.player));
            }
        }
    }

    private static final int SLOTS_PER_PAGE = 45; // real slots 0-44 are usable per page

    public static int minSlot(int page) {
        return SLOTS_PER_PAGE * (page - 1); // inclusive
    }

    public static int maxSlot(int page) {
        return SLOTS_PER_PAGE * page - 1; // inclusive
    }

    public static int realSlot(int slot, int page) {
        return slot - minSlot(page);
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
                if (!KitsManager2.kitExists(kitName)) {
                    player.sendMessage("Failed to load kit.");
                    return;
                }
                KitMeta2 meta = KitsManager2.loadKitMeta(kitName);
                if (event.getClick() == ClickType.LEFT) {
                    KitResult result = KitsManager2.claimKit(kitName, player);
                    KitsManager2.handleEvents(kitName, player, result);
                    if (KitsManager2.remaining(kitName, player.getUniqueId(), meta) == 0) {
                        KitsManager2.setCooldown(kitName, player.getUniqueId());
                    }
                    event.setCurrentItem(KitEngineItems.kitItem(kitName, player));
                } else if (event.getClick() == ClickType.RIGHT) {
                    gui.task.cancel();
                    player.openInventory(new KitEditorGui(player, kitName).getInventory());
                }
            }

            else if (KitEngineItems.isItem(KitEngineItems.NEXT_PAGE_KEY, item)) {
                gui.task.cancel();
                player.openInventory(new KitsGui(gui.page + 1, player).getInventory());
            }
            else if (KitEngineItems.isItem(KitEngineItems.BACK_PAGE_KEY, item)) {
                gui.task.cancel();
                player.openInventory(new KitsGui(gui.page - 1, player).getInventory());
            }

        }
    }
    @EventHandler
    public void on(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory inv = event.getView().getTopInventory();
        if (inv.getHolder() instanceof KitsGui gui) {
            gui.task.cancel();
        }
    }
}
