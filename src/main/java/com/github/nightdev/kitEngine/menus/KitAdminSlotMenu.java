package com.github.nightdev.kitEngine.menus;

import com.github.nightdev.kitEngine.KitEngine;
import com.github.nightdev.kitEngine.api.KitEngineItems;
import com.github.nightdev.kitEngine.api.Menu;
import com.github.nightdev.kitEngine.kits.KitsManager;
import com.github.nightdev.kitEngine.kits.obj.Kit;
import com.github.nightdev.kitEngine.utils.KitUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class KitAdminSlotMenu implements Menu<KitAdminSlotMenu> {

    private static NamespacedKey SLOT_KEY;

    private final Kit kit;
    private final int page;

    public KitAdminSlotMenu(Kit kit, int page) {
        this.kit = kit;
        this.page = page;
    }

    @Override
    public KitAdminSlotMenu register(KitEngine plugin) {
        SLOT_KEY = Menu.key("slot_edit");
        return this;
    }


    @Override
    public @NotNull Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, 6 * 9, KitUtils.format("Pick a slot!"));

        for (int i = inv.getSize() - 9; i < inv.getSize(); i++) {
            inv.setItem(i, KitEngineItems.backgroundItem());
        }

        if (page > 1) {
            inv.setItem(48, KitEngineItems.backPage());
        }
        if (page < 10) {
            inv.setItem(50, KitEngineItems.nextPage());
        }

        for (int i = KitsMenu.minSlot(this.page); i <= KitsMenu.maxSlot(this.page); i++) {
            int realSlot = KitsMenu.realSlot(i, this.page);
            ItemStack item = ItemStack.of(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
            int finalI = i;
            item.editMeta(meta -> {
                meta.displayName(KitUtils.format("&7" + finalI));
            });
            item.editPersistentDataContainer(pdc -> pdc.set(SLOT_KEY, PersistentDataType.INTEGER, finalI));
            inv.setItem(realSlot, item);
        }

        for (Kit kit : KitsManager.getKits()) {
            int slot = kit.meta.slot;
            if (slot >= KitsMenu.minSlot(this.page) && slot <= KitsMenu.maxSlot(this.page)) {
                inv.setItem(KitsMenu.realSlot(slot, this.page), ItemStack.of(Material.RED_STAINED_GLASS_PANE));
            }
        }

        return inv;
    }

    @Override
    public void onClick(InventoryClickEvent event, Player player, Inventory inv, ItemStack item, KitAdminSlotMenu menu) {
        event.setCancelled(true);
        if (this.isItem(SLOT_KEY, item)) {
            int slot = item.getPersistentDataContainer().get(SLOT_KEY, PersistentDataType.INTEGER);
            KitsManager.editKit(kit.getName(), kit -> {
                kit.meta.slot = slot;
            });
            Menu.openKitAdminMenu(player, menu.kit);
        }
        else if (this.isItem(KitEngineItems.BACK_PAGE, item)) {
            player.openInventory(new KitAdminSlotMenu(menu.kit, menu.page - 1)
                    .register(KitEngine.getInstance())
                    .getInventory());
        }
        else if (this.isItem(KitEngineItems.NEXT_PAGE, item)) {
            player.openInventory(new KitAdminSlotMenu(menu.kit, menu.page + 1)
                    .register(KitEngine.getInstance())
                    .getInventory());
        }
    }
}
