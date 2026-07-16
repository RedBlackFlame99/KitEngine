package com.github.nightdev.kitEngine.manager.obj;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public record KitCooldown(
        boolean enabled,
        int ticks
) implements ConfigurationSerializable {
    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of(
                "enabled", enabled,
                "ticks", ticks
        );
    }

    public static KitCooldown deserialize(Map<String, Object> data) {
        return new KitCooldown(
                (boolean) data.get("enabled"),
                (int) data.get("ticks")
        );
    }
}
