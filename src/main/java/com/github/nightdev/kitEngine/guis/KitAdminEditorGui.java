package com.github.nightdev.kitEngine.guis;

import com.github.nightdev.kitEngine.core.KitEngineItems;
import com.github.nightdev.kitEngine.core.KitEngineLang;
import com.github.nightdev.kitEngine.core.KitEnginePerms;
import com.github.nightdev.kitEngine.guis.input.InputType;
import com.github.nightdev.kitEngine.guis.input.PlayerInputListener;
import com.github.nightdev.kitEngine.guis.input.SlotInputListener;
import com.github.nightdev.kitEngine.manager.KitContents2;
import com.github.nightdev.kitEngine.manager.KitMeta2;
import com.github.nightdev.kitEngine.manager.KitsManager2;
import com.github.nightdev.kitEngine.utils.KitUtils;
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

public class KitAdminEditorGui implements InventoryHolder, Listener {

    private final String kitName;

    public KitAdminEditorGui(String kitName) {
        this.kitName = kitName;
    }
    public KitAdminEditorGui() {
        this.kitName = "";
    }

    @Override
    public @NotNull Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, 4 * 9, KitUtils.format(kitName));

        KitContents2 contents = KitsManager2.loadKitContents(kitName);
        KitMeta2 meta = KitsManager2.loadKitMeta(kitName);

        //inv.setItem(4, KitEngineItems.adminUpdateContentsItem(contents));

        inv.setItem(10, KitEngineItems.adminUpdateDisplayItem(meta));
        inv.setItem(11, KitEngineItems.adminUpdateDisplayNameItem(meta));
        inv.setItem(12, KitEngineItems.adminUpdateSlotItem(meta));
        inv.setItem(13, KitEngineItems.adminUpdateOverrideItem(meta));
        inv.setItem(14, KitEngineItems.adminEditEditorToggleItem(meta));
        inv.setItem(15, KitEngineItems.adminUpdateOnSuccessItem(meta));
        inv.setItem(16, KitEngineItems.adminUpdateOnFailureItem(meta));

        inv.setItem(19, KitEngineItems.adminUpdatePermissionItem(meta));
        inv.setItem(20, KitEngineItems.adminUpdateCooldownItem(meta));
        inv.setItem(21, KitEngineItems.adminUpdateCostItem(meta));
        inv.setItem(22, KitEngineItems.adminUpdateRequirementsItem(meta));
        inv.setItem(23, KitEngineItems.adminEditREIItem(meta));
        inv.setItem(24, KitEngineItems.adminEditUsesItem(meta));

        return inv;
    }

    @EventHandler
    public void on(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getView().getTopInventory();
        if (inv.getHolder() instanceof KitAdminEditorGui adminEditorGui) {
            ItemStack item = event.getCurrentItem();
            if (item == null) return;
            if (event.getRawSlot() <= inv.getSize()) {
                event.setCancelled(true);
            }


            String kitName = adminEditorGui.kitName;

            boolean reopenAdminEditor = false;

            if (KitEngineItems.isItem(KitEngineItems.ADMIN_UPDATE_CONTENTS_KEY, item)) {
                if (!player.hasPermission(KitEnginePerms.ADMIN_EDIT_CONTENTS)) {
                    player.sendMessage(KitEngineLang.noPermission(KitEnginePerms.ADMIN_EDIT_CONTENTS));
                    return;
                }
                if (event.getClick() == ClickType.LEFT) {
                    KitsManager2.saveKitContents(adminEditorGui.kitName, KitContents2.fromInventory(player.getInventory()));
                }
            }
            else if (KitEngineItems.isItem(KitEngineItems.ADMIN_UPDATE_PERMISSION_KEY, item)) {
                if (!player.hasPermission(KitEnginePerms.ADMIN_EDIT_PERMISSION)) {
                    player.sendMessage(KitEngineLang.noPermission(KitEnginePerms.ADMIN_EDIT_PERMISSION));
                    return;
                }

                if (event.getClick() == ClickType.LEFT) {
                    KitsManager2.updateMeta(kitName, meta -> {
                        meta.permission.enabled = !meta.permission.enabled;
                    });
                    reopenAdminEditor = true;
                } else if (event.getClick() == ClickType.RIGHT) {
                    PlayerInputListener.setRequestingInput(player, "PERMISSION", kitName, InputType.STRING);
                }
            }
            else if (KitEngineItems.isItem(KitEngineItems.ADMIN_UPDATE_COOLDOWN_KEY, item)) {
                if (!player.hasPermission(KitEnginePerms.ADMIN_EDIT_COOLDOWN)) {
                    player.sendMessage(KitEngineLang.noPermission(KitEnginePerms.ADMIN_EDIT_COOLDOWN));
                    return;
                }

                if (event.getClick() == ClickType.LEFT) {
                    KitsManager2.updateMeta(adminEditorGui.kitName, meta -> {
                        meta.cooldown.enabled = !meta.cooldown.enabled;
                    });
                    reopenAdminEditor = true;
                } else if (event.getClick() == ClickType.RIGHT) {
                    PlayerInputListener.setRequestingInput(player, "COOLDOWN", kitName, InputType.INTEGER);
                }
            }
            else if (KitEngineItems.isItem(KitEngineItems.ADMIN_UPDATE_DISPLAY_KEY, item)) {
                if (!player.hasPermission(KitEnginePerms.ADMIN_EDIT_DISPLAY_ITEM)) {
                    player.sendMessage(KitEngineLang.noPermission(KitEnginePerms.ADMIN_EDIT_DISPLAY_ITEM));
                    return;
                }

                ItemStack cursor = event.getCursor();
                if (cursor.isEmpty()) {
                    player.sendMessage("You must drag an item on to here.");
                    return;
                }

                KitsManager2.updateMeta(adminEditorGui.kitName, meta -> {
                    meta.displayItem = event.getCursor();
                });
                reopenAdminEditor = true;
            }
            else if (KitEngineItems.isItem(KitEngineItems.ADMIN_UPDATE_SLOT_KEY, item)) {
                if (!player.hasPermission(KitEnginePerms.ADMIN_EDIT_SLOT)) {
                    player.sendMessage(KitEngineLang.noPermission(KitEnginePerms.ADMIN_EDIT_SLOT));
                    return;
                }
                player.openInventory(new SlotInputListener(kitName, 1).getInventory());
                //PlayerInputListener.setRequestingInput(player, "SLOT", kitName);
            }
            else if (KitEngineItems.isItem(KitEngineItems.ADMIN_UPDATE_DISPLAY_NAME_KEY, item)) {
                if (!player.hasPermission(KitEnginePerms.ADMIN_EDIT_DISPLAY_NAME)) {
                    player.sendMessage(KitEngineLang.noPermission(KitEnginePerms.ADMIN_EDIT_DISPLAY_NAME));
                    return;
                }
                if (event.getClick() == ClickType.LEFT) {
                    PlayerInputListener.setRequestingInput(player, "DISPLAY_NAME", kitName, InputType.STRING);
                } else if (event.getClick() == ClickType.RIGHT) {
                    KitsManager2.updateMeta(kitName, meta -> {
                        meta.displayName = kitName;
                    });
                    reopenAdminEditor = true;
                }
            }
            else if (KitEngineItems.isItem(KitEngineItems.ADMIN_UPDATE_COST_KEY, item)) {
                if (!player.hasPermission(KitEnginePerms.ADMIN_EDIT_COST)) {
                    player.sendMessage(KitEngineLang.noPermission(KitEnginePerms.ADMIN_EDIT_COST));
                    return;
                }
                if (event.getClick() == ClickType.LEFT) {
                    reopenAdminEditor = true;
                } else if (event.getClick() == ClickType.RIGHT) {
                    PlayerInputListener.setRequestingInput(player, "COST", kitName, InputType.NUMBER);
                }
            }
            else if (KitEngineItems.isItem(KitEngineItems.ADMIN_UPDATE_REQ_KEY, item)) {
                if (!player.hasPermission(KitEnginePerms.ADMIN_EDIT_REQ)) {
                    player.sendMessage(KitEngineLang.noPermission(KitEnginePerms.ADMIN_EDIT_REQ));
                    return;
                }

                if (event.getClick() == ClickType.LEFT) {
                    PlayerInputListener.setRequestingInput(player, "REQUIREMENTS_ADD", kitName, InputType.STRING);
                } else if (event.getClick() == ClickType.RIGHT) {
                    PlayerInputListener.setRequestingInput(player, "REQUIREMENTS_REMOVE", kitName, InputType.INTEGER);
                } else if (event.getClick() == ClickType.SHIFT_RIGHT) {
                    KitsManager2.updateMeta(kitName, meta -> {
                        meta.requirements.list().clear();
                    });
                    reopenAdminEditor = true;
                }
            }
            else if (KitEngineItems.isItem(KitEngineItems.ADMIN_UPDATE_OVERRIDE_KEY, item)) {
                if (!player.hasPermission(KitEnginePerms.ADMIN_EDIT_OVERRIDE)) {
                    player.sendMessage(KitEngineLang.noPermission(KitEnginePerms.ADMIN_EDIT_OVERRIDE));
                    return;
                }
                if (event.getClick() == ClickType.LEFT) {
                    KitsManager2.updateMeta(kitName, meta -> {
                        meta.overrideInventory = !meta.overrideInventory;
                    });
                    reopenAdminEditor = true;
                }
            }
            else if (KitEngineItems.isItem(KitEngineItems.ADMIN_UPDATE_ON_SUCCESS_KEY, item)) {
                if (event.getClick() == ClickType.LEFT) {
                    PlayerInputListener.setRequestingInput(player, "ON_SUCCESS_ADD", kitName, InputType.STRING);
                }
                else if (event.getClick() == ClickType.RIGHT) {
                    PlayerInputListener.setRequestingInput(player, "ON_SUCCESS_REMOVE", kitName, InputType.INTEGER);
                }
                else if (event.getClick() == ClickType.SHIFT_RIGHT) {
                    KitsManager2.updateMeta(kitName, meta -> {
                        meta.events.onSuccess.clear();
                    });
                    reopenAdminEditor = true;
                }
            }
            else if (KitEngineItems.isItem(KitEngineItems.ADMIN_UPDATE_ON_FAILURE_KEY, item)) {
                if (event.getClick() == ClickType.LEFT) {
                    PlayerInputListener.setRequestingInput(player, "ON_FAILURE_ADD", kitName, InputType.STRING);
                }
                else if (event.getClick() == ClickType.RIGHT) {
                    PlayerInputListener.setRequestingInput(player, "ON_FAILURE_REMOVE", kitName, InputType.INTEGER);
                }
                else if (event.getClick() == ClickType.SHIFT_RIGHT) {
                    KitsManager2.updateMeta(kitName, meta -> {
                        meta.events.onFailure.clear();
                    });
                    reopenAdminEditor = true;
                }
            }
            else if (KitEngineItems.isItem(KitEngineItems.ADMIN_EDIT_REI_KEY, item)) {
                if (event.getClick() == ClickType.LEFT) {
                    KitsManager2.updateMeta(kitName, meta -> {
                        meta.requiresEmptyInventory = !meta.requiresEmptyInventory;
                    });
                    reopenAdminEditor = true;
                }
            }
            else if (KitEngineItems.isItem(KitEngineItems.ADMIN_EDIT_USE_KIT_EDITOR_KEY, item)) {
                if (event.getClick() == ClickType.LEFT) {
                    KitsManager2.updateMeta(kitName, meta -> {
                        meta.useKitEditor = !meta.useKitEditor;
                    });
                    reopenAdminEditor = true;
                }
            }
            else if (KitEngineItems.isItem(KitEngineItems.ADMIN_EDIT_USES_KEY, item)) {
                if (event.getClick() == ClickType.LEFT) {
                    KitsManager2.updateMeta(kitName, meta -> {
                        meta.uses.enabled = !meta.uses.enabled;
                    });
                    reopenAdminEditor = true;
                } else if (event.getClick() == ClickType.RIGHT) {
                    PlayerInputListener.setRequestingInput(player, "USES", kitName, InputType.INTEGER);
                }
            }

            if (reopenAdminEditor) {
                player.openInventory(new KitAdminEditorGui(kitName).getInventory());
            }
        }
    }
}
