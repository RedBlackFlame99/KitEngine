package com.github.nightdev.kitEngine.menus;

import com.github.nightdev.kitEngine.KitEngine;
import com.github.nightdev.kitEngine.api.Menu;
import com.github.nightdev.kitEngine.kits.KitsManager;
import com.github.nightdev.kitEngine.kits.input.PlayerInput;
import com.github.nightdev.kitEngine.kits.input.PlayerInputListener;
import com.github.nightdev.kitEngine.kits.obj.Kit;
import com.github.nightdev.kitEngine.kits.obj.KitContents;
import com.github.nightdev.kitEngine.kits.obj.meta.KitGroup;
import com.github.nightdev.kitEngine.utils.KitUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.units.qual.K;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KitAdminMenu implements Menu<KitAdminMenu> {
    private static NamespacedKey BACKGROUND;

    private static NamespacedKey STATUS_KEY;
    private static NamespacedKey EDIT_CONTENTS_KEY;
    private static NamespacedKey EDIT_DISPLAY_NAME_KEY;
    private static NamespacedKey EDIT_REI_KEY;
    private static NamespacedKey EDIT_KE_KEY;

    private static NamespacedKey EDIT_ON_SUCCESS_KEY;
    private static NamespacedKey EDIT_ON_FAILURE_KEY;

    private static NamespacedKey EDIT_SLOT_KEY;
    private static NamespacedKey EDIT_PERMISSION_KEY;
    private static NamespacedKey EDIT_COOLDOWN_KEY;
    private static NamespacedKey EDIT_COST_KEY;
    private static NamespacedKey EDIT_USES_KEY;

    private static NamespacedKey DRAG_DISPLAY_ITEM;
    private static NamespacedKey DRAG_NP_DISPLAY_ITEM;
    private static NamespacedKey DRAG_CD_DISPLAY_ITEM;
    private static NamespacedKey DRAG_C_DISPLAY_ITEM;
    private static NamespacedKey DRAG_U_DISPLAY_ITEM;

    private final Kit kit;

    public KitAdminMenu(Kit kit) {
        this.kit = kit;
    }

    @Override
    public KitAdminMenu register(KitEngine plugin) {
        BACKGROUND = Menu.key("background");

        STATUS_KEY = Menu.key("status");
        EDIT_CONTENTS_KEY = Menu.key("edit_contents");
        EDIT_DISPLAY_NAME_KEY = Menu.key("edit_display_name");
        EDIT_REI_KEY = Menu.key("edit_requires_empty_inventory");
        EDIT_KE_KEY = Menu.key("edit_kit_editor");

        EDIT_ON_SUCCESS_KEY = Menu.key("edit_on_success_key");
        EDIT_ON_FAILURE_KEY = Menu.key("edit_on_failure_key");

        EDIT_SLOT_KEY = Menu.key("edit_slot");
        EDIT_PERMISSION_KEY = Menu.key("edit_permission");
        EDIT_COOLDOWN_KEY = Menu.key("edit_cooldown");
        EDIT_COST_KEY = Menu.key("edit_cost");
        EDIT_USES_KEY = Menu.key("edit_uses");

        DRAG_DISPLAY_ITEM = Menu.key("edit_display_item");
        DRAG_NP_DISPLAY_ITEM = Menu.key("edit_permission_di");
        DRAG_CD_DISPLAY_ITEM = Menu.key("edit_cooldown_di");
        DRAG_C_DISPLAY_ITEM = Menu.key("edit_cost_di");
        DRAG_U_DISPLAY_ITEM = Menu.key("edit_uses_di");
        return this;
    }

    @Override
    public @NotNull Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, 6 * 9, Component.text("Kit Admin Editor"));

        int[] backgroundSlots = { 0,1,2,3,5,6,7,8,9,17,18,19,20,21,22,23,24,25,26,27,35,36,44,45,46,47,48,49,50,51,52,53 };
        for (int slot : backgroundSlots) {
            inv.setItem(slot, backgroundItem());
        }

        inv.setItem(4, statusItem());
        inv.setItem(10, editContentsItem());
        inv.setItem(11, editDisplayNameItem());
        inv.setItem(12, editREIItem());
        inv.setItem(13, editKEItem());

        /* On Success & On Failure
        inv.setItem(15, editOnSuccessItem());
        inv.setItem(16, editOnFailureItem());
         */

        inv.setItem(28, editSlotItem());
        inv.setItem(29, editPermissionItem());
        inv.setItem(30, editCooldownItem());
        inv.setItem(31, editCostItem());
        inv.setItem(32, editUsesItem());

        inv.setItem(37, dragDisplayItem());
        inv.setItem(38, dragNPDisplayItem());
        inv.setItem(39, dragCDDisplayItem());
        inv.setItem(40, dragCostDisplayItem());
        inv.setItem(41, dragUsesDisplayItem());

        return inv;
    }

    @Override
    public void onClick(InventoryClickEvent event, Player player, Inventory inv, ItemStack item, KitAdminMenu menu) {
        if (event.getRawSlot() < inv.getSize()) {
            event.setCancelled(true);
        }
        if (item == null) return;

        boolean reopenMenu = false;

        String kitName = menu.kit.getName();
        if (this.isItem(STATUS_KEY, item)) {
            KitsManager.editKit(kitName, kit -> kit.enabled = !kit.enabled);
            reopenMenu = true;
        }
        else if (this.isItem(EDIT_CONTENTS_KEY, item)) {
            KitsManager.editKit(menu.kit.getName(), kit -> {
                kit.contents = KitContents.fromInventory(player.getInventory());
            });
            reopenMenu = true;
        }
        else if (this.isItem(EDIT_DISPLAY_NAME_KEY, item)) {
            PlayerInputListener.setRequestingInput(
                    player,
                    PlayerInput.DISPLAY_NAME,
                    kitName
            );
        }
        else if (this.isItem(EDIT_REI_KEY, item)) {
            KitsManager.editKit(kitName, kit -> {
                kit.meta.requiresEmptyInv = !kit.meta.requiresEmptyInv;
            });
            reopenMenu = true;
        }
        else if (this.isItem(EDIT_KE_KEY, item)) {
            if (event.getClick() == ClickType.LEFT) {
                KitsManager.editKit(kitName, kit -> {
                    kit.meta.useKitEditor = !kit.meta.useKitEditor;
                });
                reopenMenu = true;
            }
        }

        else if (this.isItem(EDIT_ON_SUCCESS_KEY, item)) {
            if (event.getClick() == ClickType.LEFT) {
                PlayerInputListener.setRequestingInput(
                        player,
                        PlayerInput.ON_SUCCESS_ADD,
                        kitName
                );
            } else if (event.getClick() == ClickType.RIGHT) {
                PlayerInputListener.setRequestingInput(
                        player,
                        PlayerInput.ON_SUCCESS_REMOVE,
                        kitName
                );
            }
        }
        else if (this.isItem(EDIT_ON_FAILURE_KEY, item)) {
            if (event.getClick() == ClickType.LEFT) {
                PlayerInputListener.setRequestingInput(
                        player, PlayerInput.ON_FAILURE_ADD, kitName
                );
            } else if (event.getClick() == ClickType.RIGHT) {
                PlayerInputListener.setRequestingInput(
                        player, PlayerInput.ON_FAILURE_REMOVE, kitName
                );
            }
        }

        else if (this.isItem(EDIT_SLOT_KEY, item)) {
            if (event.getClick() == ClickType.LEFT) {
                KitAdminSlotMenu m = new KitAdminSlotMenu(menu.kit, 1)
                        .register(KitEngine.getInstance());
                player.openInventory(m.getInventory());
            } else if (event.getClick() == ClickType.RIGHT) {
                PlayerInputListener.setRequestingInput(
                        player,
                        PlayerInput.GROUP,
                        kitName
                );
            } else if (event.getClick() == ClickType.SHIFT_RIGHT) {
                KitsManager.editKit(kitName, kit -> {
                    kit.meta.group = KitGroup.global();
                });
                reopenMenu = true;
            }
        }
        else if (this.isItem(EDIT_PERMISSION_KEY, item)) {
            if (event.getClick() == ClickType.LEFT) {
                KitsManager.editKit(kitName, kit -> {
                    kit.meta.permissionEnabled = !kit.meta.permissionEnabled;
                });
                reopenMenu = true;
            } else if (event.getClick() == ClickType.RIGHT) {
                PlayerInputListener.setRequestingInput(
                        player,
                        PlayerInput.PERMISSION,
                        kitName
                );
            }
        }
        else if (this.isItem(EDIT_COOLDOWN_KEY, item)) {
            if (event.getClick() == ClickType.LEFT) {
                KitsManager.editKit(kitName, kit -> {
                    kit.meta.cooldownEnabled = !kit.meta.cooldownEnabled;
                });
                reopenMenu = true;
            } else if (event.getClick() == ClickType.RIGHT) {
                PlayerInputListener.setRequestingInput(
                        player,
                        PlayerInput.COOLDOWN,
                        kitName
                );
            }
        }
        else if (this.isItem(EDIT_COST_KEY, item)) {
            if (event.getClick() == ClickType.LEFT) {
                KitsManager.editKit(kitName, kit -> {
                    kit.meta.costEnabled = !kit.meta.costEnabled;
                });
                reopenMenu = true;
            } else if (event.getClick() == ClickType.RIGHT) {
                PlayerInputListener.setRequestingInput(
                        player,
                        PlayerInput.COST,
                        kitName
                );
            }
        }
        else if (this.isItem(EDIT_USES_KEY, item)) {
            if (event.getClick() == ClickType.LEFT) {
                KitsManager.editKit(kitName, kit -> {
                    kit.meta.usesEnabled = !kit.meta.usesEnabled;
                });
                reopenMenu = true;
            } else if (event.getClick() == ClickType.RIGHT) {
                PlayerInputListener.setRequestingInput(
                        player,
                        PlayerInput.USES,
                        kitName
                );
            }
        }

        ItemStack cursor = event.getCursor().clone();
        if (!cursor.isEmpty()) {
            if (this.isItem(DRAG_DISPLAY_ITEM, item)) {
                KitsManager.editKit(kitName, kit -> {
                    kit.meta.displayItem = cursor;
                });
                reopenMenu = true;
            }
            else if (this.isItem(DRAG_NP_DISPLAY_ITEM, item)) {
                KitsManager.editKit(kitName, kit -> {
                    kit.meta.noPermDisplayItem = cursor;
                });
                reopenMenu = true;
            }
            else if (this.isItem(DRAG_CD_DISPLAY_ITEM, item)) {
                KitsManager.editKit(kitName, kit -> {
                    kit.meta.cooldownDisplayItem = cursor;
                });
                reopenMenu = true;
            }
            else if (this.isItem(DRAG_C_DISPLAY_ITEM, item)) {
                KitsManager.editKit(kitName, kit -> {
                    kit.meta.costDisplayItem = cursor;
                });
                reopenMenu = true;
            }
            else if (this.isItem(DRAG_U_DISPLAY_ITEM, item)) {
                KitsManager.editKit(kitName, kit -> {
                    kit.meta.usesDisplayItem = cursor;
                });
                reopenMenu = true;
            }
        }

        if (reopenMenu) {
            Menu.openKitAdminMenu(player, KitsManager.retrieveKit(kitName));
        }


    }

    public ItemStack backgroundItem() {
        ItemStack item = ItemStack.of(Material.CYAN_STAINED_GLASS_PANE);
        item.editMeta(meta -> meta.displayName(Component.empty()));
        item.editPersistentDataContainer(pdc -> pdc.set(BACKGROUND, PersistentDataType.BOOLEAN, true));
        return item;
    }

    public ItemStack statusItem() {
        ItemStack item;
        Component v;
        if (kit.enabled) {
            item = ItemStack.of(Material.LIME_CONCRETE_POWDER);
            v = KitUtils.format("&a&lENABLED");
        } else {
            item = ItemStack.of(Material.RED_CONCRETE_POWDER);
            v = KitUtils.format("&c&lDISABLED");
        }
        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&7Status &8| ").append(v));
        });
        item.editPersistentDataContainer(pdc -> pdc.set(STATUS_KEY, PersistentDataType.BOOLEAN, true));
        return item;
    }
    public ItemStack editContentsItem() {
        int amount = kit.contents.getItems().values().stream().filter(Objects::nonNull).mapToInt(ItemStack::getAmount).sum();

        ItemStack item = ItemStack.of(Material.SUNFLOWER);
        item.editMeta(meta -> {
            meta.setItemModel(NamespacedKey.minecraft("bundle"));
            meta.displayName(KitUtils.format("&7Contents &8| &e"+ amount + " items"));
        });
        item.editPersistentDataContainer(pdc -> {
            pdc.set(EDIT_CONTENTS_KEY, PersistentDataType.BOOLEAN, true);
        });
        return item;
    }
    public ItemStack editDisplayNameItem() {
        ItemStack item = ItemStack.of(Material.NAME_TAG);
        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&7Display Name &8| &f" + kit.meta.displayName));
        });
        item.editPersistentDataContainer(pdc -> pdc.set(EDIT_DISPLAY_NAME_KEY, PersistentDataType.BOOLEAN, true));
        return item;
    }
    public ItemStack editREIItem() {
        ItemStack item = ItemStack.of(Material.GLASS_BOTTLE);
        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&b&lREQUIRES EMPTY INVENTORY"));
            meta.lore(List.of(
                    KitUtils.format("&7Requires Empty Inventory &8| &f" + kit.meta.requiresEmptyInv)
            ));
        });
        item.editPersistentDataContainer(pdc -> pdc.set(EDIT_REI_KEY, PersistentDataType.BOOLEAN, true));
        return item;
    }
    public ItemStack editKEItem() {
        ItemStack item = ItemStack.of(Material.BLAZE_POWDER);
        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&b&lKIT EDITOR"));
            meta.lore(List.of(
                    KitUtils.format("&7Use Kit Editor &8| &f" + kit.meta.useKitEditor)
            ));
        });
        item.editPersistentDataContainer(pdc -> pdc.set(EDIT_KE_KEY, PersistentDataType.BOOLEAN, true));
        return item;
    }

    public ItemStack editOnSuccessItem() {
        ItemStack item = ItemStack.of(Material.LIME_DYE);
        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&a&lON SUCCESS"));
            List<Component> lore = new ArrayList<>();
            if (kit.meta.onSuccess.isEmpty()) {
                lore.add(KitUtils.format("&7There are no actions to execute."));
            } else {
                int i = 0;
                for (String cmd : kit.meta.onSuccess) {
                    lore.add(KitUtils.format("&7" + i + ": " + cmd));
                    i++;
                }
                lore.add(KitUtils.format("&7"));
            }
            lore.add(KitUtils.format("&7Left Click to add!"));
            meta.lore(lore);
        });
        item.editPersistentDataContainer(pdc -> pdc.set(EDIT_ON_SUCCESS_KEY, PersistentDataType.BOOLEAN, true));
        return item;
    }
    public ItemStack editOnFailureItem() {
        ItemStack item = ItemStack.of(Material.RED_DYE);
        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&c&lON FAILURE"));
            List<Component> lore = new ArrayList<>();
            if (kit.meta.onFailure.isEmpty()) {
                lore.add(KitUtils.format("&7There are no actions to execute."));
            } else {
                int i = 0;
                for (String cmd : kit.meta.onFailure) {
                    lore.add(KitUtils.format("&7" + i + ": " + cmd));
                    i++;
                }
                lore.add(KitUtils.format("&7"));
            }
            lore.add(KitUtils.format("&7Left Click to add!"));
            meta.lore(lore);
        });
        item.editPersistentDataContainer(pdc -> pdc.set(EDIT_ON_FAILURE_KEY, PersistentDataType.BOOLEAN, true));
        return item;
    }

    public ItemStack editSlotItem() {
        ItemStack item = ItemStack.of(Material.NETHER_STAR);
        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&b&lSLOT & GROUP"));
            meta.lore(List.of(
                    KitUtils.format("&7Slot &8| &f" + kit.meta.slot),
                    KitUtils.format("&7Group &8| &f" + kit.meta.group.id)
            ));
        });
        item.editPersistentDataContainer(pdc -> pdc.set(EDIT_SLOT_KEY, PersistentDataType.BOOLEAN, true));
        return item;
    }
    public ItemStack editPermissionItem() {
        ItemStack item = ItemStack.of(Material.OMINOUS_TRIAL_KEY);
        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&b&lPERMISSION"));
            meta.lore(List.of(
                    KitUtils.format("&7Enabled &8| &f" + kit.meta.permissionEnabled),
                    KitUtils.format("&7Permission: &8| &f" + kit.meta.permission)
            ));
        });
        item.editPersistentDataContainer(pdc -> pdc.set(EDIT_PERMISSION_KEY, PersistentDataType.BOOLEAN, true));
        return item;
    }
    public ItemStack editCooldownItem() {
        ItemStack item = ItemStack.of(Material.CLOCK);
        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&b&lCOOLDOWN"));
            meta.lore(List.of(
                    KitUtils.format("&7Enabled &8| &f" + kit.meta.cooldownEnabled),
                    KitUtils.format("&7Cooldown &8| &f" + kit.meta.cooldown)
            ));
        });
        item.editPersistentDataContainer(pdc -> pdc.set(EDIT_COOLDOWN_KEY, PersistentDataType.BOOLEAN, true));
        return item;
    }
    public ItemStack editCostItem() {
        ItemStack item = ItemStack.of(Material.EMERALD);
        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&b&lCOST"));
            meta.lore(List.of(
                    KitUtils.format("&7Enabled &8| &f" + kit.meta.costEnabled),
                    KitUtils.format("&7Cost &8| &f" + kit.meta.cost)
            ));
        });
        item.editPersistentDataContainer(pdc -> pdc.set(EDIT_COST_KEY, PersistentDataType.BOOLEAN, true));
        return item;
    }
    public ItemStack editUsesItem() {
        ItemStack item = ItemStack.of(Material.PUFFERFISH);
        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&b&lUSES"));
            meta.lore(List.of(
                    KitUtils.format("&7Enabled &8| &f" + kit.meta.usesEnabled),
                    KitUtils.format("&7Uses &8| &f" + kit.meta.uses)
            ));
        });
        item.editPersistentDataContainer(pdc -> pdc.set(EDIT_USES_KEY, PersistentDataType.BOOLEAN, true));
        return item;
    }

    public ItemStack dragDisplayItem() {
        ItemStack item = ItemStack.of(Material.ITEM_FRAME);
        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&7Display Item &8| &6" + kit.meta.displayItem.getType().name()));
        });
        item.editPersistentDataContainer(pdc -> pdc.set(DRAG_DISPLAY_ITEM, PersistentDataType.BOOLEAN, true));
        return item;
    }
    public ItemStack dragNPDisplayItem() {
        ItemStack item = ItemStack.of(Material.ITEM_FRAME);
        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&7Display Item (No Permission) &8| &6" + kit.meta.noPermDisplayItem.getType().name()));
        });
        item.editPersistentDataContainer(pdc -> pdc.set(DRAG_NP_DISPLAY_ITEM, PersistentDataType.BOOLEAN, true));
        return item;
    }
    public ItemStack dragCDDisplayItem() {
        ItemStack item = ItemStack.of(Material.ITEM_FRAME);
        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&7Display Item (No Cooldown) &8| &6" + kit.meta.cooldownDisplayItem.getType().name()));
        });
        item.editPersistentDataContainer(pdc -> pdc.set(DRAG_CD_DISPLAY_ITEM, PersistentDataType.BOOLEAN, true));
        return item;
    }
    public ItemStack dragCostDisplayItem() {
        ItemStack item = ItemStack.of(Material.ITEM_FRAME);
        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&7Display Item (Insufficient Funds) &8| &6" + kit.meta.costDisplayItem.getType().name()));
        });
        item.editPersistentDataContainer(pdc -> pdc.set(DRAG_C_DISPLAY_ITEM, PersistentDataType.BOOLEAN, true));
        return item;
    }
    public ItemStack dragUsesDisplayItem() {
        ItemStack item = ItemStack.of(Material.ITEM_FRAME);
        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&7Display Item (Reached Uses) &8| &6" + kit.meta.usesDisplayItem.getType().name()));
        });
        item.editPersistentDataContainer(pdc -> pdc.set(DRAG_U_DISPLAY_ITEM, PersistentDataType.BOOLEAN, true));
        return item;
    }
}
