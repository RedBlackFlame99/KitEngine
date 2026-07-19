package com.github.nightdev.kitEngine.core;

import com.github.nightdev.kitEngine.KitEngine;
import com.github.nightdev.kitEngine.manager.KitContents2;
import com.github.nightdev.kitEngine.manager.KitMeta2;
import com.github.nightdev.kitEngine.manager.KitsManager2;
import com.github.nightdev.kitEngine.manager.obj.meta.KitEvents;
import com.github.nightdev.kitEngine.manager.obj.meta.KitRequirements;
import com.github.nightdev.kitEngine.utils.KitUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class KitEngineItems {
    public static int itemID = 0;
    public static NamespacedKey BACKGROUND_ITEM_KEY;
    public static NamespacedKey NEXT_PAGE_KEY;
    public static NamespacedKey BACK_PAGE_KEY;
    public static NamespacedKey ADMIN_UPDATE_CONTENTS_KEY;
    public static NamespacedKey ADMIN_UPDATE_PERMISSION_KEY;
    public static NamespacedKey ADMIN_UPDATE_COOLDOWN_KEY;
    public static NamespacedKey ADMIN_UPDATE_DISPLAY_KEY;
    public static NamespacedKey ADMIN_UPDATE_SLOT_KEY;
    public static NamespacedKey ADMIN_UPDATE_SLOT_INPUT_KEY;
    public static NamespacedKey ADMIN_UPDATE_DISPLAY_NAME_KEY;
    public static NamespacedKey ADMIN_UPDATE_COST_KEY;
    public static NamespacedKey ADMIN_UPDATE_REQ_KEY;
    public static NamespacedKey ADMIN_UPDATE_OVERRIDE_KEY;
    public static NamespacedKey ADMIN_UPDATE_ON_SUCCESS_KEY;
    public static NamespacedKey ADMIN_UPDATE_ON_FAILURE_KEY;
    public static NamespacedKey ADMIN_EDIT_REI_KEY;
    public static NamespacedKey ADMIN_EDIT_USE_KIT_EDITOR_KEY;
    public static NamespacedKey ADMIN_EDIT_USES_KEY;
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
        ADMIN_UPDATE_SLOT_INPUT_KEY = random(plugin);
        ADMIN_UPDATE_DISPLAY_NAME_KEY = random(plugin);
        ADMIN_UPDATE_COST_KEY = random(plugin);
        ADMIN_UPDATE_REQ_KEY = random(plugin);
        ADMIN_UPDATE_OVERRIDE_KEY = random(plugin);
        ADMIN_UPDATE_ON_SUCCESS_KEY = random(plugin);
        ADMIN_UPDATE_ON_FAILURE_KEY = random(plugin);
        ADMIN_EDIT_REI_KEY = random(plugin);
        ADMIN_EDIT_USE_KIT_EDITOR_KEY = random(plugin);
        ADMIN_EDIT_USES_KEY = random(plugin);
        EDITOR_RESET_KEY = random(plugin);
        EDITOR_SAVE_KEY = random(plugin);
        KIT_ITEM_KEY = random(plugin);
    }

    private static NamespacedKey random(KitEngine plugin) {
        return new NamespacedKey(plugin, "item_" + itemID++);
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

    public static ItemStack adminUpdateContentsItem(KitContents2 contents) {
        ItemStack item = ItemStack.of(Material.CHEST);

        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&b&lCONTENTS"));
            meta.lore(List.of(
                    KitUtils.format("&8| &7Change the contents of the kit."),
                    KitUtils.format("&8|"),
                    KitUtils.format("&8| &7Left Click to update all contents"),
                    KitUtils.format("&8|"),
                    KitUtils.format("&8| &cThis will use items in your inventory!"),
                    KitUtils.format("&8| &cThis will wipe all player layouts for this kit!"),
                    KitUtils.format("&8| &c&n⚠ This cannot be undone.")
            ));
        });

        item.editPersistentDataContainer(pdc -> {
            pdc.set(ADMIN_UPDATE_CONTENTS_KEY, PersistentDataType.BOOLEAN, false);
        });

        return item;
    }
    public static ItemStack adminUpdatePermissionItem(KitMeta2 m) {
        ItemStack item = ItemStack.of(Material.PAPER);

        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&b&lPERMISSION"));

            boolean enabled = (m.permission.enabled);
            String permission = m.permission.permission;

            meta.lore(List.of(
                    KitUtils.format("&8| &7Should there be a permission for this kit?"),
                    KitUtils.format("&8|"),
                    KitUtils.format("&8| &7Required: &f" + KitUtils.bool(enabled)),
                    KitUtils.format("&8| &7Permission: &f" + permission),
                    KitUtils.format("&8|"),
                    KitUtils.format("&8| &7Left Click to toggle"),
                    KitUtils.format("&8| &7Right Click to change permission")
            ));
        });

        item.editPersistentDataContainer(pdc -> {
            pdc.set(ADMIN_UPDATE_PERMISSION_KEY, PersistentDataType.BOOLEAN, true);
        });

        return item;
    }
    public static ItemStack adminUpdateCooldownItem(KitMeta2 m) {
        ItemStack item = ItemStack.of(Material.CLOCK);

        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&b&lCOOLDOWN"));
            meta.lore(List.of(
                    KitUtils.format("&8| &7Should there be a cooldown with this kit?"),
                    KitUtils.format("&8|"),
                    KitUtils.format("&8| &7Enabled: &f" + KitUtils.bool(m.cooldown.enabled)),
                    KitUtils.format("&8| &7Seconds: &f" + m.cooldown.seconds),
                    KitUtils.format("&8|"),
                    KitUtils.format("&8| &7Left Click to toggle"),
                    KitUtils.format("&8| &7Right Click to change seconds")
            ));
        });

        item.editPersistentDataContainer(pdc -> {
            pdc.set(ADMIN_UPDATE_COOLDOWN_KEY, PersistentDataType.BOOLEAN, true);
        });

        return item;
    }
    public static ItemStack adminUpdateDisplayItem(KitMeta2 m) {
        ItemStack item = ItemStack.of(Material.ITEM_FRAME);

        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&b&lDISPLAY ITEM"));
            meta.lore(List.of(
                    KitUtils.format("&8| &7Current Item: &f" + m.displayItem.getType().name()),
                    KitUtils.format("&8|"),
                    KitUtils.format("&8| &7Drag an item here to change it")
            ));
        });

        item.editPersistentDataContainer(pdc -> {
            pdc.set(ADMIN_UPDATE_DISPLAY_KEY, PersistentDataType.BOOLEAN, true);
        });

        return item;
    }
    public static ItemStack adminUpdateSlotItem(KitMeta2 m) {
        ItemStack item = ItemStack.of(Material.TRIPWIRE_HOOK);

        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&b&lSLOT"));
            meta.lore(List.of(
                    KitUtils.format("&8| &7What slot should this be in the /kits menu?"),
                    KitUtils.format("&8|"),
                    KitUtils.format("&8| &7Slot: &e" + m.slot),
                    KitUtils.format("&8|"),
                    KitUtils.format("&8| &7Left Click to change the slot")
            ));
        });

        item.editPersistentDataContainer(pdc -> {
            pdc.set(ADMIN_UPDATE_SLOT_KEY, PersistentDataType.BOOLEAN, true);
        });

        return item;
    }
    public static ItemStack adminUpdateSlotInputItem(int slot) {
        ItemStack item = ItemStack.of(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&7" + slot));
        });
        item.editPersistentDataContainer(pdc -> {
            pdc.set(ADMIN_UPDATE_SLOT_INPUT_KEY, PersistentDataType.INTEGER, slot);
        });
        return item;
    }
    public static ItemStack adminUpdateDisplayNameItem(KitMeta2 m) {
        ItemStack item = ItemStack.of(Material.NAME_TAG);

        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&b&lDISPLAY NAME"));
            meta.lore(List.of(
                    KitUtils.format("&8| &7What should the display name of this kit be?"),
                    KitUtils.format("&8|"),
                    KitUtils.format("&8| &7Display Name: &f" + m.displayName),
                    KitUtils.format("&8|"),
                    KitUtils.format("&8| &7Left Click to change the display name"),
                    KitUtils.format("&8| &7Right Click to remove display name")
            ));
        });

        item.editPersistentDataContainer(pdc -> {
            pdc.set(ADMIN_UPDATE_DISPLAY_NAME_KEY, PersistentDataType.BOOLEAN, true);
        });

        return item;
    }
    public static ItemStack adminUpdateCostItem(KitMeta2 m) {
        ItemStack item = ItemStack.of(Material.SUNFLOWER);
        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&b&lCOST"));
            List<Component> lore = new ArrayList<>();
            lore.add(KitUtils.format("&8| &7Should there be a cost to claim this kit?"));
            lore.add(KitUtils.format("&8|"));
            lore.add(KitUtils.format("&8| &7Enabled: &f" + KitUtils.bool(m.cost.enabled)));
            lore.add(KitUtils.format("&8| &7Cost: &e" + m.cost.value));
            lore.add(KitUtils.format("&8|"));
            lore.add(KitUtils.format("&8| &7Left Click to toggle."));
            lore.add(KitUtils.format("&8| &7Right Click to change cost."));
            if (!KitEngine.isEconomyHooked()) {
                lore.add(KitUtils.format("&8| &c* No economy plugin found."));
            }
            meta.lore(lore);
        });
        item.editPersistentDataContainer(pdc -> {
            pdc.set(ADMIN_UPDATE_COST_KEY, PersistentDataType.BOOLEAN, true);
        });
        return item;
    }
    public static ItemStack adminUpdateRequirementsItem(KitMeta2 m) {
        ItemStack item = ItemStack.of(Material.SHIELD);
        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&b&lREQUIREMENTS"));
            List<Component> lore = new ArrayList<>();
            lore.add(KitUtils.format("&8| &7Should there be requirements to claim this kit?"));
            lore.add(KitUtils.format("&8|"));
            KitRequirements requirements = m.requirements;
            if (requirements.list().isEmpty()) {
                lore.add(KitUtils.format("&8| &7Requirements: &fNone"));
            } else {
                lore.add(KitUtils.format("&8| &7Requirements:"));
                for (int i = 0; i < requirements.list().size(); i++) {
                    String req = requirements.list().get(i);
                    lore.add(KitUtils.format("&8| &e" + i + ": &f" + req));
                }
            }
            lore.add(KitUtils.format("&8|"));
            lore.add(KitUtils.format("&8| &7Left Click to add requirement"));
            lore.add(KitUtils.format("&8| &7Right Click to delete requirement"));
            lore.add(KitUtils.format("&8| &7Shift Right Click to delete all requirements"));

            meta.lore(lore);
        });

        item.editPersistentDataContainer(pdc -> {
            pdc.set(ADMIN_UPDATE_REQ_KEY, PersistentDataType.BOOLEAN, true);
        });
        return item;
    }
    public static ItemStack adminUpdateOverrideItem(KitMeta2 m) {
        ItemStack item = ItemStack.of(Material.NETHER_STAR);
        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&b&lOVERRIDE"));
            meta.lore(List.of(
                    KitUtils.format("&8| &7Should the kit override items in player's inventories?"),
                    KitUtils.format("&8|"),
                    KitUtils.format("&8| &7Override: " + KitUtils.bool(m.overrideInventory)),
                    KitUtils.format("&8|"),
                    KitUtils.format("&8| &7Left Click to toggle")
            ));
        });
        item.editPersistentDataContainer(pdc -> {
            pdc.set(ADMIN_UPDATE_OVERRIDE_KEY, PersistentDataType.BOOLEAN, true);
        });
        return item;
    }
    public static ItemStack adminUpdateOnSuccessItem(KitMeta2 m) {
        ItemStack item = ItemStack.of(Material.LIME_DYE);
        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&a&lON SUCCESS"));
            List<Component> lore = new ArrayList<>();
            lore.add(KitUtils.format("&8| &7What commands should be ran when its a success?"));
            lore.add(KitUtils.format("&8|"));
            KitEvents events = m.events;
            if (events.onSuccess.isEmpty()) {
                lore.add(KitUtils.format("&8| &7Commands: &fNone"));
            } else {
                lore.add(KitUtils.format("&8| &7Commands:"));
                for (int i = 0; i < events.onSuccess.size(); i++) {
                    String cmd = events.onSuccess.get(i);
                    lore.add(KitUtils.format("&8| &e" + i + ": &f" + cmd));
                }
            }
            lore.add(KitUtils.format("&8|"));
            lore.add(KitUtils.format("&8| &7Left Click to add command"));
            lore.add(KitUtils.format("&8| &7Right Click to remove command"));
            lore.add(KitUtils.format("&8| &7Shift Right Click to remove all commands"));
            meta.lore(lore);
        });
        item.editPersistentDataContainer(pdc -> {
            pdc.set(ADMIN_UPDATE_ON_SUCCESS_KEY, PersistentDataType.BOOLEAN, true);
        });
        return item;
    }
    public static ItemStack adminUpdateOnFailureItem(KitMeta2 m) {
        ItemStack item = ItemStack.of(Material.RED_DYE);
        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&c&lON FAILURE"));
            List<Component> lore = new ArrayList<>();
            lore.add(KitUtils.format("&8| &7What commands should be ran when its a failure?"));
            lore.add(KitUtils.format("&8|"));
            KitEvents events = m.events;
            if (events.onFailure.isEmpty()) {
                lore.add(KitUtils.format("&8| &7Commands: &fNone"));
            } else {
                lore.add(KitUtils.format("&8| &7Commands: "));
                for (int i = 0; i < events.onFailure.size(); i++) {
                    String cmd = events.onFailure.get(i);
                    lore.add(KitUtils.format("&8| &e" + i + ": &f" + cmd));
                }
            }
            lore.add(KitUtils.format("&8|"));
            lore.add(KitUtils.format("&8| &7Left Click to add command"));
            lore.add(KitUtils.format("&8| &7Right Click to remove command"));
            lore.add(KitUtils.format("&8| &7Shift Right Click to remove all commands"));
            meta.lore(lore);
        });
        item.editPersistentDataContainer(pdc -> {
            pdc.set(ADMIN_UPDATE_ON_FAILURE_KEY, PersistentDataType.BOOLEAN, true);
        });
        return item;
    }
    public static ItemStack adminEditREIItem(KitMeta2 m) {
        ItemStack item = ItemStack.of(Material.SUNFLOWER);
        item.editMeta(meta -> {
            meta.setItemModel(new NamespacedKey("minecraft", "bundle"));
            meta.displayName(KitUtils.format("&b&lREQUIRES EMPTY INVENTORY"));
            meta.lore(List.of(
                    KitUtils.format("&8| &7Should the player require an empty inventory?"),
                    KitUtils.format("&8| "),
                    KitUtils.format("&8| &7Value: " + KitUtils.bool(m.requiresEmptyInventory)),
                    KitUtils.format("&8| "),
                    KitUtils.format("&8| &7Left Click to toggle")
            ));
        });
        item.editPersistentDataContainer(pdc -> {
            pdc.set(ADMIN_EDIT_REI_KEY, PersistentDataType.BOOLEAN, true);
        });
        return item;
    }
    public static ItemStack adminEditEditorToggleItem(KitMeta2 m) {
        ItemStack item = ItemStack.of(Material.BLAZE_POWDER);
        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&b&lKIT EDITOR"));
            meta.lore(List.of(
                    KitUtils.format("&8| &7Should players be able to have their own layouts?"),
                    KitUtils.format("&8| &7"),
                    KitUtils.format("&8| &7Kit Editor: " + KitUtils.bool(m.useKitEditor)),
                    KitUtils.format("&8| &7"),
                    KitUtils.format("&8| &7Left Click to toggle")
            ));
        });
        item.editPersistentDataContainer(pdc -> {
            pdc.set(ADMIN_EDIT_USE_KIT_EDITOR_KEY, PersistentDataType.BOOLEAN, true);
        });
        return item;
    }
    public static ItemStack adminEditUsesItem(KitMeta2 m) {
        ItemStack item = ItemStack.of(Material.EXPERIENCE_BOTTLE);
        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&b&lUSES"));
            meta.lore(List.of(
                    KitUtils.format("&8| &7Should there be a limit on this kit?"),
                    KitUtils.format("&8|"),
                    KitUtils.format("&8| &7Enabled: " + KitUtils.bool(m.uses.enabled)),
                    KitUtils.format("&8| &7Uses: &e" + m.uses.uses),
                    KitUtils.format("&8| "),
                    KitUtils.format("&8| &7Left Click to toggle"),
                    KitUtils.format("&8| &7Right Click to change uses")
            ));
        });
        item.editPersistentDataContainer(pdc -> {
            pdc.set(ADMIN_EDIT_USES_KEY, PersistentDataType.BOOLEAN, true);
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

    public static ItemStack kitItem(String name, Player player) {
        if (KitsManager2.kitExists(name)) {
            KitMeta2 m = KitsManager2.loadKitMeta(name);
            ItemStack item = m.displayItem.clone();
            item.editMeta(meta -> {
                if (m.displayName.isEmpty()) {
                    meta.displayName(KitUtils.format("&b" + name));
                } else {
                    meta.displayName(KitUtils.format("&b" + m.displayName));
                }

                List<Component> lore = new ArrayList<>();
                lore.add(KitUtils.format("&8ᴘʟᴀʏᴇʀ ᴋɪᴛ"));
                lore.add(Component.empty());
                lore.add(KitUtils.format("&8| &7Left Click to claim"));
                if (m.useKitEditor) {
                    lore.add(KitUtils.format("&8| &7Right Click to edit"));
                } else {
                    lore.add(KitUtils.format("&8| &7Right Click to edit &c(Disabled)"));
                }
                lore.add(KitUtils.format("&8|"));

                if (m.cost.enabled) {
                    double cost = m.cost.value;
                    if (cost > 0) {
                        lore.add(KitUtils.format("&8| &6Cost: " + cost));
                    }
                }


                if (m.permission.enabled && !player.hasPermission(m.permission.permission)) {
                    lore.add(KitUtils.format("&8| &cRequires permission: &4" + m.permission.permission));
                }
                if (m.cooldown.enabled) {
                    long remaining = KitsManager2.remaining(name, player.getUniqueId(), m);
                    if (remaining > 0) {
                        lore.add(KitUtils.format("&8| &eCooldown: " + KitUtils.formatTime((int) remaining)));
                        meta.setItemModel(NamespacedKey.minecraft("red_dye"));
                    } else {
                        lore.add(KitUtils.format("&8| &aReady to claim!"));
                    }
                    //Bukkit.getLogger().info(name + "remaining=" + remaining);
                }
                if (m.uses.enabled) {
                    lore.add(KitUtils.format("&8| &7Uses: " + KitsManager2.getUses(name, player.getUniqueId()) + "/" + m.uses.uses));
                }

                meta.lore(lore);

            });

            item.editPersistentDataContainer(pdc -> {
                pdc.set(KIT_ITEM_KEY, PersistentDataType.STRING, name);
            });

            item.addItemFlags(ItemFlag.values());
            return item;
        } else {
            return ItemStack.of(Material.BARRIER);
        }
    }

    public static boolean isItem(NamespacedKey key, ItemStack item) {
        return item.getPersistentDataContainer().has(key);
    }
    public static String kit(ItemStack item) {
        return item.getPersistentDataContainer().get(KIT_ITEM_KEY, PersistentDataType.STRING);
    }
    public static int slotInput(ItemStack item) {
        return item.getPersistentDataContainer().get(ADMIN_UPDATE_SLOT_INPUT_KEY, PersistentDataType.INTEGER);
    }
}
