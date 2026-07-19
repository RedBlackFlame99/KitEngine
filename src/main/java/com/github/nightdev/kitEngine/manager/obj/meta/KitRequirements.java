package com.github.nightdev.kitEngine.manager.obj.meta;

import com.github.nightdev.kitEngine.KitEngine;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.mvel2.MVEL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record KitRequirements(List<String> list) implements ConfigurationSerializable {
    public KitRequirements() {
        this(new ArrayList<>());
    }

    public boolean fits(Player player) {
        for (String req : list) {
            req = PlaceholderAPI.setPlaceholders(player, req);
            Object result = MVEL.eval(req);
            if (result instanceof Boolean bool) {
                if (!bool) return false;
            } else {
                KitEngine.getInstance().getLogger().severe(String.valueOf(result));
                return false;
            }
        }
        return true;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of(
                "requirements", list
        );
    }

    public static KitRequirements deserialize(Map<String, Object> data) {
        return new KitRequirements(
                (List<String>) data.getOrDefault("requirements", new KitRequirements().list())
        );
    }
}
