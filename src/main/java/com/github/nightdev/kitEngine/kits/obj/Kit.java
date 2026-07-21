package com.github.nightdev.kitEngine.kits.obj;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Kit implements ConfigurationSerializable {
    private final String name;
    private final KitMeta meta;
    private final KitContents contents;

    public Kit(String name, KitMeta meta, KitContents contents) {
        this.name = name;
        this.meta = meta;
        this.contents = contents;
    }

    public String getName() {
        return this.name;
    }


    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of(
                "name", this.name,
                "meta", this.meta,
                "contents", this.contents
        );
    }
}
