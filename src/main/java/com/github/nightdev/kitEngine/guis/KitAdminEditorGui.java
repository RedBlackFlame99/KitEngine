package com.github.nightdev.kitEngine.guis;

import com.github.nightdev.kitEngine.core.KitEngineItems;
import com.github.nightdev.kitEngine.guis.input.PlayerInputListener;
import com.github.nightdev.kitEngine.manager.KitsManager;
import com.github.nightdev.kitEngine.manager.obj.Kit;
import com.github.nightdev.kitEngine.manager.obj.KitPermission;
import com.github.nightdev.kitEngine.utils.KitUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
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
        Inventory inv = Bukkit.createInventory(this, InventoryType.DROPPER, KitUtils.format(kitName));

        Kit kit = KitsManager.kit(kitName);

        inv.setItem(0, KitEngineItems.adminUpdateContentsItem());
        inv.setItem(1, KitEngineItems.adminUpdatePermissionItem(kit));
        inv.setItem(2, KitEngineItems.adminUpdateCooldownItem());
        inv.setItem(3, KitEngineItems.adminUpdateDisplayItem());
        inv.setItem(4, KitEngineItems.adminUpdateSlotItem());
        //inv.setItem(5, KitEngineItems.adminUpdateColorItem(kit));

        return inv;
    }

    @EventHandler
    public void on(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTopInventory().getHolder() instanceof KitAdminEditorGui adminEditorGui) {
            if (event.getCurrentItem() == null) return;
            if (event.getRawSlot() <= 8) {
                event.setCancelled(true);
            }


            String kitName = adminEditorGui.kitName;
            ItemStack item = event.getCurrentItem();
            if (KitEngineItems.isItem(KitEngineItems.ADMIN_UPDATE_CONTENTS_KEY, item)) {
                KitsManager.update(kitName, kit -> {
                    kit.updateContents(player.getInventory());
                });
            } else if (KitEngineItems.isItem(KitEngineItems.ADMIN_UPDATE_PERMISSION_KEY, item)) {
                KitsManager.update(kitName, kit -> {
                    kit.permission = new KitPermission(!kit.permission.required(), "kitengine.kits." + kitName);
                });
                player.openInventory(new KitAdminEditorGui(kitName).getInventory());
            } else if (KitEngineItems.isItem(KitEngineItems.ADMIN_UPDATE_COOLDOWN_KEY, item)) {
                PlayerInputListener.setRequestingInput(player, "COOLDOWN", kitName);
            } else if (KitEngineItems.isItem(KitEngineItems.ADMIN_UPDATE_DISPLAY_KEY, item)) {
                ItemStack cursor = event.getCursor();
                if (cursor.isEmpty()) {
                    player.sendMessage("You must drag an item on to here.");
                    return;
                }

                KitsManager.update(kitName, kit -> {
                    kit.setDisplay(event.getCursor());
                });
                player.getInventory().close();
            } else if (KitEngineItems.isItem(KitEngineItems.ADMIN_UPDATE_SLOT_KEY, item)) {
                PlayerInputListener.setRequestingInput(player, "SLOT", kitName);
            } else if (KitEngineItems.isItem(KitEngineItems.ADMIN_UPDATE_COLOR_KEY, item)) {
                PlayerInputListener.setRequestingInput(player, "COLOR", kitName);
            }
        }
    }
}
