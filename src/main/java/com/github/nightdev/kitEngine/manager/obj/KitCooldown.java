package com.github.nightdev.kitEngine.manager.obj;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public record KitCooldown(
        boolean enabled,
        int seconds
) implements ConfigurationSerializable {
    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of(
                "enabled", enabled,
                "seconds", seconds
        );
    }

    public static KitCooldown deserialize(Map<String, Object> data) {
        return new KitCooldown(
                (boolean) data.getOrDefault("enabled", true),
                (int) data.getOrDefault("seconds", 3600)
        );
    }
}
