package com.github.nightdev.kitEngine.core;

import com.github.nightdev.kitEngine.KitEngine;
import com.github.nightdev.kitEngine.manager.KitsManager;
import com.github.nightdev.kitEngine.manager.obj.Kit;
import com.github.nightdev.kitEngine.utils.KitUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.units.qual.N;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KitEngineItems {
    public static NamespacedKey BACKGROUND_ITEM_KEY;
    public static NamespacedKey NEXT_PAGE_KEY;
    public static NamespacedKey BACK_PAGE_KEY;
    public static NamespacedKey ADMIN_UPDATE_CONTENTS_KEY;
    public static NamespacedKey ADMIN_UPDATE_PERMISSION_KEY;
    public static NamespacedKey ADMIN_UPDATE_COOLDOWN_KEY;
    public static NamespacedKey ADMIN_UPDATE_DISPLAY_KEY;
    public static NamespacedKey ADMIN_UPDATE_SLOT_KEY;
    public static NamespacedKey ADMIN_UPDATE_DISPLAY_NAME_KEY;
    public static NamespacedKey ADMIN_UPDATE_COLOR_KEY;
    public static NamespacedKey EDITOR_RESET_KEY;
    public static NamespacedKey EDITOR_SAVE_KEY;
    public static NamespacedKey KIT_ITEM_KEY;

    public static void init(KitEngine plugin) {
        BACKGROUND_ITEM_KEY = random(plugin);
        NEXT_PAGE_KEY = random(plugin);
        BACK_PAGE_KEY = random(plugin);
        ADMIN_UPDATE_CONTENTS_KEY = random(plugin);
        ADMIN_UPDATE_PERMISSION_KEY = random(plugin);
        ADMIN_UPDATE_COOLDOWN_KEY = random(plugin);
        ADMIN_UPDATE_DISPLAY_KEY = random(plugin);
        ADMIN_UPDATE_SLOT_KEY = random(plugin);
        ADMIN_UPDATE_DISPLAY_NAME_KEY = random(plugin);
        ADMIN_UPDATE_COLOR_KEY = random(plugin);
        EDITOR_RESET_KEY = random(plugin);
        EDITOR_SAVE_KEY = random(plugin);
        KIT_ITEM_KEY = random(plugin);
    }

    private static NamespacedKey random(KitEngine plugin) {
        return new NamespacedKey(plugin, String.valueOf(UUID.randomUUID()));
    }

    public static ItemStack backgroundItem() {
        ItemStack item = ItemStack.of(Material.GRAY_STAINED_GLASS_PANE);
        item.editMeta(meta -> {
            meta.customName(Component.empty());
        });
        return item;
    }

    public static ItemStack nextPageItem(String value) {
        ItemStack item = ItemStack.of(Material.ARROW);

        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&eNext Page"));
        });
        item.editPersistentDataContainer(pdc -> {
            pdc.set(NEXT_PAGE_KEY, PersistentDataType.BOOLEAN, true);
        });

        return item;
    }
    public static ItemStack backPageItem(String value) {
        ItemStack item = ItemStack.of(Material.ARROW);

        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&eBack Page"));
        });
        item.editPersistentDataContainer(pdc -> {
            pdc.set(BACK_PAGE_KEY, PersistentDataType.BOOLEAN, true);
        });

        return item;
    }

    public static ItemStack adminUpdateContentsItem(Kit kit) {
        ItemStack item = ItemStack.of(Material.CHEST);

        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&b&lCONTENTS"));
            meta.lore(List.of(
                    Component.empty(),
                    KitUtils.format("&8| &7Left Click to update all contents"),
                    Component.empty(),
                    KitUtils.format("&cThis will use items in your inventory!"),
                    KitUtils.format("&cThis will wipe all player layouts for this kit!"),
                    KitUtils.format("&c&n⚠ This cannot be undone.")
            ));
        });

        item.editPersistentDataContainer(pdc -> {
            pdc.set(ADMIN_UPDATE_CONTENTS_KEY, PersistentDataType.BOOLEAN, false);
        });

        return item;
    }
    public static ItemStack adminUpdatePermissionItem(Kit kit) {
        ItemStack item = ItemStack.of(Material.NAME_TAG);

        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&b&lPERMISSION"));
            meta.lore(List.of(
                    KitUtils.format("&7Required: &f" + kit.permission.required()),
                    KitUtils.format("&7Permission: &f" + kit.permission.permission()),
                    KitUtils.format(""),
                    KitUtils.format("&7Left Click to toggle.")
            ));
        });

        item.editPersistentDataContainer(pdc -> {
            pdc.set(ADMIN_UPDATE_PERMISSION_KEY, PersistentDataType.BOOLEAN, true);
        });

        return item;
    }
    public static ItemStack adminUpdateCooldownItem(Kit kit) {
        ItemStack item = ItemStack.of(Material.CLOCK);

        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&b&lCOOLDOWN"));
            meta.lore(List.of(
                    KitUtils.format("&7Enabled: &f" + kit.cooldown.enabled()),
                    KitUtils.format("&7Seconds: &f" + kit.cooldown.seconds()),
                    Component.empty(),
                    KitUtils.format("&8| &7Left Click to toggle."),
                    KitUtils.format("&8| &7Right Click to change ticks.")
            ));
        });

        item.editPersistentDataContainer(pdc -> {
            pdc.set(ADMIN_UPDATE_COOLDOWN_KEY, PersistentDataType.BOOLEAN, true);
        });

        return item;
    }
    public static ItemStack adminUpdateDisplayItem(Kit kit) {
        ItemStack item = ItemStack.of(Material.ITEM_FRAME);

        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&b&lDISPLAY ITEM"));
            meta.lore(List.of(
                    KitUtils.format("&7Current Item: &f" + kit.getDisplay().getType().name()),
                    Component.empty(),
                    KitUtils.format("&8| &7Drag an item here to change it.")
            ));
        });

        item.editPersistentDataContainer(pdc -> {
            pdc.set(ADMIN_UPDATE_DISPLAY_KEY, PersistentDataType.BOOLEAN, true);
        });

        return item;
    }
    public static ItemStack adminUpdateSlotItem(Kit kit) {
        ItemStack item = ItemStack.of(Material.TRIPWIRE_HOOK);

        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&b&lSLOT"));
            meta.lore(List.of(
                    KitUtils.format("&7Slot: &e" + kit.meta.slot()),
                    Component.empty(),
                    KitUtils.format("&8| &7Left Click to change the slot.")
            ));
        });

        item.editPersistentDataContainer(pdc -> {
            pdc.set(ADMIN_UPDATE_SLOT_KEY, PersistentDataType.BOOLEAN, true);
        });

        return item;
    }
    public static ItemStack adminUpdateDisplayNameItem(Kit kit) {
        ItemStack item = ItemStack.of(Material.NAME_TAG);

        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&b&lDISPLAY NAME"));
            meta.lore(List.of(
                    KitUtils.format("&7Display Name: &f" + kit.meta.displayName()),
                    Component.empty(),
                    KitUtils.format("&8| &7Left Click to change the display name.")
            ));
        });

        item.editPersistentDataContainer(pdc -> {
            pdc.set(ADMIN_UPDATE_DISPLAY_NAME_KEY, PersistentDataType.BOOLEAN, true);
        });

        return item;
    }

    public static ItemStack editorResetItem() {
        ItemStack item = ItemStack.of(Material.ANVIL);

        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&d&lRESET"));
        });
        item.editPersistentDataContainer(pdc -> {
            pdc.set(EDITOR_RESET_KEY, PersistentDataType.BOOLEAN, true);
        });

        return item;
    }
    public static ItemStack editorSaveItem() {
        ItemStack item = ItemStack.of(Material.NETHER_STAR);
        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&d&lSAVE"));
        });
        item.editPersistentDataContainer(pdc -> {
            pdc.set(EDITOR_SAVE_KEY, PersistentDataType.BOOLEAN, true);
        });
        return item;
    }

    public static ItemStack kitItem(String name, Kit kit, Player player) {
        ItemStack item = kit.getDisplay().clone();
        item.editMeta(meta -> {
            if (kit.meta.displayName().isEmpty()) {
                meta.displayName(KitUtils.format("&b" + name));
            } else {
                meta.displayName(KitUtils.format("&b" + kit.meta.displayName()));
            }

            List<Component> lore = new ArrayList<>();
            lore.add(KitUtils.format("&8ᴘʟᴀʏᴇʀ ᴋɪᴛ"));
            lore.add(Component.empty());
            lore.add(KitUtils.format("&8| &7Left Click to claim."));
            lore.add(KitUtils.format("&8| &7Right Click to edit."));
            if (KitsManager.cooldown(name, player) == 0) {
                lore.add(KitUtils.format("&8| &aReady to claim!"));
            } else {
                lore.add(KitUtils.format("&8| &eCooldown: " + KitUtils.formatTime(KitsManager.cooldown(name, player))));
            }
            meta.lore(lore);

        });

        item.editPersistentDataContainer(pdc -> {
            pdc.set(KIT_ITEM_KEY, PersistentDataType.STRING, name);
        });

        item.addItemFlags(ItemFlag.values());
        return item;
    }

    public static boolean isItem(NamespacedKey key, ItemStack item) {
        return item.getPersistentDataContainer().has(key);
    }
    public static String kit(ItemStack item) {
        return item.getPersistentDataContainer().get(KIT_ITEM_KEY, PersistentDataType.STRING);
    }
}
