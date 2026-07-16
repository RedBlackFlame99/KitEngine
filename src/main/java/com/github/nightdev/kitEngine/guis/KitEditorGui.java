package com.github.nightdev.kitEngine.guis;

import com.github.nightdev.kitEngine.KitEngine;
import com.github.nightdev.kitEngine.core.KitEngineConfig;
import com.github.nightdev.kitEngine.core.KitEngineItems;
import com.github.nightdev.kitEngine.manager.KitsManager;
import com.github.nightdev.kitEngine.manager.obj.Kit;
import com.github.nightdev.kitEngine.manager.obj.KitContents;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KitEditorGui implements InventoryHolder, Listener {
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
        for (int i = 0; i < 27; i++) {
            SLOT_PLACEMENTS.put(i + 9, i);
        }


        for (Map.Entry<Integer, Integer> entry : SLOT_PLACEMENTS.entrySet()) {
            SLOT_PLACEMENTS_REVERSED.put(entry.getValue(), entry.getKey());
        }
    }

    private final Player player;
    private final String kitName;

    public KitEditorGui(Player player, String name) {
        this.player = player;
        this.kitName = name;
    }

    @Override
    public @NotNull Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, 6 * 9, KitUtils.format("Kit Editor (" + kitName + ")"));

        Kit kit = KitsManager.kit(kitName, player);

        for (int i = 41; i < inv.getSize(); i++) {
            inv.setItem(i, KitEngineItems.backgroundItem());
        }
        for (int i = 0; i < 36; i++) {
            ItemStack item = kit.contents.contents().get(i);
            int slot = SLOT_PLACEMENTS.get(i);
            inv.setItem(slot, item);
        }

        inv.setItem(36, kit.contents.helmet());
        inv.setItem(37, kit.contents.chestplate());
        inv.setItem(38, kit.contents.leggings());
        inv.setItem(39, kit.contents.boots());
        inv.setItem(40, kit.contents.offhand());

        inv.setItem(51, KitEngineItems.editorResetItem());
        inv.setItem(52, KitEngineItems.editorSaveItem());

        return inv;
    }

    @EventHandler
    public void on(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTopInventory().getHolder() instanceof KitEditorGui kitEditor) {
            Inventory inv = event.getView().getTopInventory();
            if (event.getRawSlot() > 40) {
                event.setCancelled(true);
            }
            List<ClickType> BLACKLISTED_CLICKS = List.of(
                    ClickType.SWAP_OFFHAND,
                    ClickType.DROP,
                    ClickType.CONTROL_DROP,
                    ClickType.SHIFT_LEFT,
                    ClickType.SHIFT_RIGHT,
                    ClickType.NUMBER_KEY
            );

            if (BLACKLISTED_CLICKS.contains(event.getClick())) event.setCancelled(true);
            ItemStack item = event.getCurrentItem();
            if (item == null) return;
            String kitName = kitEditor.kitName;

            if (KitEngineItems.isItem(KitEngineItems.EDITOR_RESET_KEY, item)) {
                Kit kit = KitsManager.kit(kitName);
                KitsManager.layout(kitName, player, null);
                player.openInventory(new KitEditorGui(player, kitName).getInventory());
            }

            else if (KitEngineItems.isItem(KitEngineItems.EDITOR_SAVE_KEY, item)) {
                Map<Integer, ItemStack> contents = new HashMap<>();
                for (int from : SLOT_PLACEMENTS_REVERSED.keySet()) {
                    ItemStack i = inv.getItem(from);
                    int to = SLOT_PLACEMENTS_REVERSED.get(from);
                    contents.put(to, i);
                }

                KitContents kitContents = new KitContents(
                        contents,
                        inv.getItem(36),
                        inv.getItem(37),
                        inv.getItem(38),
                        inv.getItem(39),
                        inv.getItem(40)
                );
                KitsManager.layout(kitEditor.kitName, player, kitContents);
                player.openInventory(new KitsGui(1).getInventory());
            }
        }
    }
}
