package com.github.nightdev.kitEngine.kits.obj;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class KitMeta implements ConfigurationSerializable {

    private final String displayName;

    public KitMeta(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of(
                "displayName", this.displayName
        );
    }

    public static KitMeta deserialize(Map<String, Object> data) {
        return new KitMeta(
                (String) data.get("displayName")
        );
    }
}
