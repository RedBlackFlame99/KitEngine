package com.github.nightdev.kitEngine.menus;

import com.github.nightdev.kitEngine.KitEngine;
import com.github.nightdev.kitEngine.api.KitEngineItems;
import com.github.nightdev.kitEngine.api.Menu;
import com.github.nightdev.kitEngine.kits.KitsManager;
import com.github.nightdev.kitEngine.kits.LayoutManager;
import com.github.nightdev.kitEngine.kits.obj.Kit;
import com.github.nightdev.kitEngine.kits.obj.KitContents;
import com.github.nightdev.kitEngine.utils.KitUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class KitEditorMenu implements Menu<KitEditorMenu> {
    public static final Map<Integer, Integer> SLOT_PLACEMENTS = new HashMap<>();
    public static final Map<Integer, Integer> SLOT_PLACEMENTS_REVERSED = new HashMap<>();
    static {
        SLOT_PLACEMENTS.put(0, 27);
        SLOT_PLACEMENTS.put(1, 28);
        SLOT_PLACEMENTS.put(2, 29);
        SLOT_PLACEMENTS.put(3, 30);
        SLOT_PLACEMENTS.put(4, 31);
        SLOT_PLACEMENTS.put(5, 32);
        SLOT_PLACEMENTS.put(6, 33);
        SLOT_PLACEMENTS.put(7, 34);
        SLOT_PLACEMENTS.put(8, 35);

        SLOT_PLACEMENTS.put(36, 39);
        SLOT_PLACEMENTS.put(37, 38);
        SLOT_PLACEMENTS.put(38, 37);
        SLOT_PLACEMENTS.put(39, 36);
        SLOT_PLACEMENTS.put(40, 40);
        for (int i = 0; i < 27; i++) {
            SLOT_PLACEMENTS.put(i + 9, i);
        }
        for (Map.Entry<Integer, Integer> entry : SLOT_PLACEMENTS.entrySet()) {
            SLOT_PLACEMENTS_REVERSED.put(entry.getValue(), entry.getKey());
        }
    }

    private static NamespacedKey SAVE;
    private static NamespacedKey RESET;

    private final Kit kit;
    private final KitContents contents;

    public KitEditorMenu(Kit kit) {
        this.kit = kit;
        this.contents = kit.contents;
    }
    public KitEditorMenu(Kit kit, KitContents contents) {
        this.kit = kit;
        this.contents = contents;
    }

    @Override
    public KitEditorMenu register(KitEngine plugin) {
        SAVE = Menu.random();
        RESET = Menu.random();
        return this;
    }

    @Override
    public @NotNull Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, 6 * 9);

        for (int i = 41; i < inv.getSize(); i++) {
            inv.setItem(i, KitEngineItems.backgroundItem());
        }
        for (int i : SLOT_PLACEMENTS.keySet()) {
            ItemStack item = contents.getItems().get(i);
            int slot = SLOT_PLACEMENTS.get(i);
            inv.setItem(slot, item);
        }

        inv.setItem(51, resetItem());
        inv.setItem(52, saveItem());

        inv.setItem(45, ItemStack.of(Material.LEATHER_HELMET));
        inv.setItem(46, ItemStack.of(Material.LEATHER_CHESTPLATE));
        inv.setItem(47, ItemStack.of(Material.LEATHER_LEGGINGS));
        inv.setItem(48, ItemStack.of(Material.LEATHER_BOOTS));
        inv.setItem(49, ItemStack.of(Material.SHIELD));

        return inv;
    }

    @Override
    public void onClick(InventoryClickEvent event, Player player, Inventory inv, ItemStack item, KitEditorMenu menu) {
        if (event.getRawSlot() == -999 || event.getRawSlot() > 40) {
            event.setCancelled(true);
        }
        if (event.getClick() != ClickType.LEFT) {
            event.setCancelled(true);
        }
        KitContents contents = fromInventory(inv);
        if (this.isItem(SAVE, item)) {
            LayoutManager.saveLayout(player, menu.kit, contents);
            Menu.openKitsMenu(player, 1);
        }
        else if (this.isItem(RESET, item)) {
            LayoutManager.deleteLayout(player, menu.kit);
            Menu.openKitEditorMenu(player, menu.kit);
        }
    }

    public KitContents fromInventory(Inventory inv) {
        Map<Integer, ItemStack> items = new HashMap<>();
        for (int from : SLOT_PLACEMENTS_REVERSED.keySet()) {
            ItemStack i = inv.getItem(from);
            int to = SLOT_PLACEMENTS_REVERSED.get(from);
            items.put(to, i);
        }
        return new KitContents(items);
    }

    public ItemStack saveItem() {
        ItemStack item = ItemStack.of(Material.NETHER_STAR);
        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&b&lSAVE"));
        });
        item.editPersistentDataContainer(pdc -> pdc.set(SAVE, PersistentDataType.BOOLEAN, true));
        return item;
    }
    public ItemStack resetItem() {
        ItemStack item = ItemStack.of(Material.ANVIL);
        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&b&lRESET"));
        });
        item.editPersistentDataContainer(pdc -> pdc.set(RESET, PersistentDataType.BOOLEAN, true));
        return item;
    }
}
