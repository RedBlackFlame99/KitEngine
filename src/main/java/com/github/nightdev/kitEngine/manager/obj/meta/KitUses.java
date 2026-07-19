package com.github.nightdev.kitEngine.manager.obj.meta;

import com.github.nightdev.kitEngine.utils.KitUtils;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class KitUses implements ConfigurationSerializable {
    public boolean enabled;
    public int uses;

    public KitUses(boolean enabled, int uses) {
        this.enabled = enabled;
        this.uses = uses;
    }
    public KitUses() {
        this.enabled = false;
        this.uses = 1;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of(
                "enabled", enabled,
                "uses", uses
        );
    }

    public static KitUses deserialize(Map<String, Object> data) {
        return new KitUses(
                (boolean) data.get("enabled"),
                (int) data.get("uses")
        );
    }
}
