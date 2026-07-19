package com.github.nightdev.kitEngine.manager.obj.meta;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KitEvents implements ConfigurationSerializable {

    public List<String> onSuccess;
    public List<String> onFailure;

    public KitEvents(List<String> onSuccess, List<String> onFailure) {
        this.onSuccess = onSuccess;
        this.onFailure = onFailure;
    }
    public KitEvents() {
        this.onSuccess = new ArrayList<>();
        this.onFailure = new ArrayList<>();
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of(
                "onSuccess", onSuccess,
                "onFailure", onFailure
        );
    }

    public static KitEvents deserialize(Map<String, Object> data) {
        return new KitEvents(
                (List<String>) data.getOrDefault("onSuccess", new KitEvents().onSuccess),
                (List<String>) data.getOrDefault("onFailure", new KitEvents().onFailure)
        );
    }
}
