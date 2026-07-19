package com.github.nightdev.kitEngine.manager.obj.meta;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class KitCooldown implements ConfigurationSerializable {

    public boolean enabled;
    public int seconds;

    public KitCooldown(boolean enabled, int seconds) {
        this.enabled = enabled;
        this.seconds = seconds;
    }
    public KitCooldown() {
        this.enabled = true;
        this.seconds = 3600;
    }


    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of(
                "enabled", enabled,
                "seconds", seconds
        );
    }

    public static KitCooldown deserialize(Map<String, Object> data) {
        return new KitCooldown(
                (boolean) data.getOrDefault("enabled", new KitCooldown().enabled),
                (int) data.getOrDefault("seconds", new KitCooldown().seconds)
        );
    }
}
