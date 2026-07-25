package com.github.nightdev.kitEngine.menus;

import com.github.nightdev.kitEngine.KitEngine;
import com.github.nightdev.kitEngine.api.KitEngineItems;
import com.github.nightdev.kitEngine.api.Menu;
import com.github.nightdev.kitEngine.kits.KitsManager;
import com.github.nightdev.kitEngine.kits.obj.Kit;
import com.github.nightdev.kitEngine.kits.obj.meta.KitGroup;
import com.github.nightdev.kitEngine.utils.KitUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class KitsMenu implements Menu<KitsMenu> {

    private static NamespacedKey KIT_ITEM_KEY;

    private BukkitTask UPDATE_MENU;

    private final Player player;
    private final KitGroup group;
    private final int page;

    public KitsMenu(Player player, KitGroup group, int page) {
        this.player = player;
        this.group = group;
        this.page = page;
    }


    @Override
    public KitsMenu register(KitEngine plugin) {
        KIT_ITEM_KEY = Menu.key("kits_kit");
        return this;
    }

    @Override
    public @NotNull Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, 6 * 9, KitUtils.format("Kits (Page " + this.page + ")"));

        for (int i = inv.getSize() - 9; i < inv.getSize(); i++) {
            inv.setItem(i, KitEngineItems.backgroundItem());
        }
        if (page > 1) {
            inv.setItem(48, KitEngineItems.backPage());
        }
        if (page < 10) {
            inv.setItem(50, KitEngineItems.nextPage());
        }

        UPDATE_MENU = new BukkitRunnable() {
            @Override
            public void run() {
                refreshItems(inv);
            }
        }.runTaskTimer(KitEngine.getInstance(), 0, 20);

        return inv;
    }

    private void refreshItems(Inventory inv) {
        for (Kit kit : KitsManager.getKits()) {
            if (kit.meta.group.global && this.group.global) continue;
            int slot = kit.meta.slot;
            if (slot >= minSlot(this.page) && slot <= maxSlot(this.page)) {
                int realSlot = realSlot(slot, this.page);
                inv.setItem(realSlot, kitsItem(this.player, kit));
            }
        }
    }

    private static final int SLOTS_PER_PAGE = 45;

    public static int minSlot(int page) {
        return SLOTS_PER_PAGE * (page - 1);
    }
    public static int maxSlot(int page) {
        return SLOTS_PER_PAGE * page - 1;
    }
    public static int realSlot(int slot, int page) {
        return slot - minSlot(page);
    }

    @Override
    public void onClick(InventoryClickEvent event, Player player, Inventory inv, ItemStack item, KitsMenu menu) {
        event.setCancelled(true);
        if (item == null) return;

        if (this.isItem(KIT_ITEM_KEY, item)) {
            String kitName = item.getPersistentDataContainer().get(KIT_ITEM_KEY, PersistentDataType.STRING);
            if (event.getClick() == ClickType.LEFT) {
                KitsManager.claim(kitName, player);
                refreshItems(inv);
            } else if (event.getClick() == ClickType.RIGHT) {
                Kit kit = KitsManager.retrieveKit(kitName);
                if (kit != null && kit.meta.useKitEditor) {
                    Menu.openKitEditorMenu(player, KitsManager.retrieveKit(kitName));
                } else {
                    player.sendMessage("Kit Editor has been disabled for this kit!");
                }
            }
        }
        else if (this.isItem(KitEngineItems.BACK_PAGE, item)) {
            Menu.openKitsMenu(player, menu.group, this.page - 1);
        }
        else if (this.isItem(KitEngineItems.NEXT_PAGE, item)) {
            Menu.openKitsMenu(player, menu.group, this.page + 1);
        }
    }

    @Override
    public void onClose(InventoryCloseEvent event, Player player, Inventory inv, KitsMenu menu) {
        if (menu.UPDATE_MENU != null && !menu.UPDATE_MENU.isCancelled()) {
            menu.UPDATE_MENU.cancel();
        }
    }

    public ItemStack kitsItem(Player player, Kit kit) {
        ItemStack item = kit.meta.displayItem.clone();
        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&b" + kit.meta.displayName));
            meta.lore(List.of(
                    KitUtils.format("&8ᴘʟᴀʏᴇʀ ᴋɪᴛ"),
                    KitUtils.format("&7"),
                    KitUtils.format("&7Left Click to claim!"),
                    KitUtils.format("&7Right Click to edit!")
            ));
        });
        item.addItemFlags(ItemFlag.values());
        item.editPersistentDataContainer(pdc -> pdc.set(KIT_ITEM_KEY, PersistentDataType.STRING, kit.getName()));
        if (kit.meta.permissionEnabled) {
            if (!player.hasPermission(kit.meta.permission)) {
                item = kit.meta.noPermDisplayItem.clone();
                item.editMeta(meta -> {
                    meta.displayName(KitUtils.format("&c" + kit.meta.displayName));
                    meta.lore(List.of(
                            KitUtils.format("&8ɴᴏ ᴘᴇʀᴍɪѕѕɪᴏɴ"),
                            KitUtils.format("&7"),
                            KitUtils.format("&7You do not have permission for this kit!")
                    ));
                });
                return item;
            }
        }
        if (kit.meta.usesEnabled) {
            int maxUses = kit.meta.uses;
            int uses = KitsManager.getUses(player, kit);
            if (uses >= maxUses) {
                item = kit.meta.usesDisplayItem.clone();
                item.editMeta(meta -> {
                    meta.displayName(KitUtils.format("&c" + kit.meta.displayName));
                    meta.lore(List.of(
                            KitUtils.format("&8ᴍᴀx ᴜѕᴇѕ ʀᴇᴀᴄʜᴇᴅ"),
                            KitUtils.format("&7"),
                            KitUtils.format("&e♠ Uses: " + uses + "/" + maxUses)
                    ));
                });
                return item;
            }
        }
        if (kit.meta.cooldownEnabled) {
            if (KitsManager.getRemainingCooldown(player, kit) > 0) {
                item = kit.meta.cooldownDisplayItem.clone();
                item.editMeta(meta -> {
                    meta.displayName(KitUtils.format("&c" + kit.meta.displayName));
                    meta.lore(List.of(
                            KitUtils.format("&8ᴏɴ ᴄᴏᴏʟᴅᴏᴡɴ"),
                            KitUtils.format("&7"),
                            KitUtils.format("&e⏳ " + KitUtils.formatTime(KitsManager.getRemainingCooldown(player, kit)))
                    ));
                });
                return item;
            }
        }
        return item;
    }
}
