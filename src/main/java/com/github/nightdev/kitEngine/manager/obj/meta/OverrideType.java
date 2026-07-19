package com.github.nightdev.kitEngine.manager.obj.meta;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public enum OverrideType implements ConfigurationSerializable {
    SET_ITEMS,
    ADD_ITEMS;

    public static OverrideType next(OverrideType overrideType) {
        if (overrideType == OverrideType.SET_ITEMS) {
            return OverrideType.ADD_ITEMS;
        } else if (overrideType == OverrideType.ADD_ITEMS) {
            return OverrideType.SET_ITEMS;
        } else {
            return OverrideType.SET_ITEMS;
        }
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of(
                "override-type", String.valueOf(this).toUpperCase()
        );
    }

    public static OverrideType deserialize(Map<String, Object> data) {
        return OverrideType.valueOf(((String) data.getOrDefault("override-type", OverrideType.SET_ITEMS.name())).toUpperCase());
    }
}
