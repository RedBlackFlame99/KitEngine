package com.github.nightdev.kitEngine.kits.obj;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class KitContents implements ConfigurationSerializable {

    private final Map<Integer, ItemStack> CONTENTS;

    public KitContents(Map<Integer, ItemStack> items) {
        this.CONTENTS = items;
    }

    public Map<Integer, ItemStack> getItems() {
        return CONTENTS;
    }

    public static KitContents fromInventory(PlayerInventory inv) {
        Map<Integer, ItemStack> contents = new HashMap<>();
        for (int i = 0; i < inv.getSize(); i++) {
            contents.put(i, inv.getItem(i));
        }
        return new KitContents(contents);
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of(
                "contents", this.CONTENTS
        );
    }

    public static KitContents deserialize(Map<String, Object> data) {
        return new KitContents(
                (Map<Integer, ItemStack>) data.get("contents")
        );
    }
}
