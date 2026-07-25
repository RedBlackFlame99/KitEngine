package com.github.nightdev.kitEngine.kits.obj.meta;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@SerializableAs("KitGroup")
public class KitGroup implements ConfigurationSerializable {
    public final boolean global;
    public final String id;
    public String displayName;

    public KitGroup() {
        this.global = true;
        this.id = "";
    }
    public KitGroup(String id) {
        this.global = false;
        this.id = id.toLowerCase();
    }
    public KitGroup(boolean global, String id) {
        this.global = global;
        this.id = id;
    }

    public boolean is(String id) {
        return this.id.equalsIgnoreCase(id);
    }

    public boolean isGlobal() {
        return this.global;
    }

    public static KitGroup global() {
        return new KitGroup();
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of(
                "global", this.global,
                "id", this.id
        );
    }

    public static KitGroup deserialize(Map<String, Object> data) {
        return new KitGroup(
                (boolean) data.getOrDefault("global", true),
                (String) data.getOrDefault("id", "")
        );
    }
}
