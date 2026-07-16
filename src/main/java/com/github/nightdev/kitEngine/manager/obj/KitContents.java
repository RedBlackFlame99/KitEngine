package com.github.nightdev.kitEngine.manager.obj;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public record KitContents(
        Map<Integer, ItemStack> contents,
        ItemStack helmet,
        ItemStack chestplate,
        ItemStack leggings,
        ItemStack boots,
        ItemStack offhand
) implements ConfigurationSerializable {
    public static KitContents fromInventory(PlayerInventory inv) {
        Map<Integer, ItemStack> contents = new HashMap<>();
        for (int i = 0; i < 36; i++) {
            contents.put(i, inv.getItem(i));
        }
        return new KitContents(
                contents,
                inv.getHelmet(),
                inv.getChestplate(),
                inv.getLeggings(),
                inv.getBoots(),
                inv.getItemInOffHand()
        );
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of(
                "contents", contents,
                "helmet", helmet,
                "chestplate", chestplate,
                "leggings", leggings,
                "boots", boots,
                "offhand", offhand
        );
    }

    public static KitContents deserialize(Map<String, Object> data) {
        return new KitContents(
                (Map<Integer, ItemStack>) data.get("contents"),
                (ItemStack) data.get("helmet"),
                (ItemStack) data.get("chestplate"),
                (ItemStack) data.get("leggings"),
                (ItemStack) data.get("boots"),
                (ItemStack) data.get("offhand")
        );
    }
}
