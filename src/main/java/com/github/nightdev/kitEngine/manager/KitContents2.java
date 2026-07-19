package com.github.nightdev.kitEngine.manager;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;

public class KitContents2 {
    private final Map<Integer, ItemStack> items;

    public KitContents2(Map<Integer, ItemStack> items, ItemStack offhand, ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots) {
        this.items = items;
        this.items.put(40, offhand);
        this.items.put(39, helmet);
        this.items.put(38, chestplate);
        this.items.put(37, leggings);
        this.items.put(36, boots);
    }

    public KitContents2(Map<Integer, ItemStack> items) {
        this.items = items;
    }

    public Map<Integer, ItemStack> getItems() {
        return this.items;
    }

    public static KitContents2 fromInventory(PlayerInventory inv) {
        Map<Integer, ItemStack> items = new HashMap<>();
        for (int i = 0; i < inv.getSize(); i++) {
            items.put(i, inv.getItem(i));
        }
        return new KitContents2(items);
    }
}
