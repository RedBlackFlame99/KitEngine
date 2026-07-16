package com.github.nightdev.kitEngine.manager.obj;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public record KitPermission(
        boolean required,
        String permission
) implements ConfigurationSerializable {
    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of(
                "required", required,
                "perm", permission
        );
    }

    public static KitPermission deserialize(Map<String, Object> data) {
        return new KitPermission(
                (boolean) data.getOrDefault("required", false),
                (String) data.getOrDefault("perm", "kitengine.kit")
        );
    }
}
