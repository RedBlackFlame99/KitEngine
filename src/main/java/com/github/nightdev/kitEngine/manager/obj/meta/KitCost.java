package com.github.nightdev.kitEngine.manager.obj.meta;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class KitCost implements ConfigurationSerializable {
    public boolean enabled;
    public double value;

    public KitCost(boolean value, double cost) {
        this.enabled = value;
        this.value = cost;
    }
    public KitCost() {
        this.enabled = false;
        this.value = 100;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of(
                "enabled", enabled,
                "value", value
        );
    }

    public static KitCost deserialize(Map<String, Object> data) {
        return new KitCost(
                (boolean) data.getOrDefault("enabled", new KitCost().enabled),
                (double) data.getOrDefault("value", new KitCost().value)
        );
    }
}
