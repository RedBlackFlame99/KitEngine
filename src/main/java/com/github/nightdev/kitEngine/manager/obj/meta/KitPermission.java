package com.github.nightdev.kitEngine.manager.obj.meta;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class KitPermission implements ConfigurationSerializable {

    public boolean enabled;
    public String permission;

    public KitPermission(boolean enabled, String permission) {
        this.enabled = enabled;
        this.permission = permission;
    }
    public KitPermission() {
        this.enabled = false;
        this.permission = "group.default";
    }

    @Override
    public String toString() {
        return this.permission;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of(
                "enabled", enabled,
                "perm", permission
        );
    }

    public static KitPermission deserialize(Map<String, Object> data) {
        return new KitPermission(
                (boolean) data.getOrDefault("enabled", new KitPermission().enabled),
                (String) data.getOrDefault("perm", new KitPermission().permission)
        );
    }
}
