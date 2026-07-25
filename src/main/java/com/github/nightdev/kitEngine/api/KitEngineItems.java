package com.github.nightdev.kitEngine.api;

import com.github.nightdev.kitEngine.utils.KitUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class KitEngineItems {
    public static NamespacedKey NEXT_PAGE;
    public static NamespacedKey BACK_PAGE;

    public static void register() {
        NEXT_PAGE = Menu.random();
        BACK_PAGE = Menu.random();
    }

    public static ItemStack backgroundItem() {
        ItemStack item = ItemStack.of(Material.GRAY_STAINED_GLASS_PANE);
        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&7"));
        });
        return item;
    }


    public static ItemStack nextPage() {
        ItemStack item = ItemStack.of(Material.ARROW);
        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&eNext Page"));
        });
        item.editPersistentDataContainer(pdc -> pdc.set(NEXT_PAGE, PersistentDataType.BOOLEAN, true));
        return item;
    }

    public static ItemStack backPage() {
        ItemStack item = ItemStack.of(Material.ARROW);
        item.editMeta(meta -> {
            meta.displayName(KitUtils.format("&eBack Page"));
        });
        item.editPersistentDataContainer(pdc -> pdc.set(BACK_PAGE, PersistentDataType.BOOLEAN, true));
        return item;
    }
}
