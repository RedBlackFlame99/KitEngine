package com.github.nightdev.kitEngine.kits.obj;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Gatherer;

public class KitContents implements ConfigurationSerializable {

    private final Map<Integer, ItemStack> CONTENTS;

    public KitContents(Map<Integer, ItemStack> items) {
        this.CONTENTS = items;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of(
                "contents", this.CONTENTS
        );
    }

    @SuppressWarnings("unchecked")
    public static KitContents deserialize(Map<String, Object> data) {
        return new KitContents(
                (Map<Integer, ItemStack>) data.get("contents")
        );
    }
}
