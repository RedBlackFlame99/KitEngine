package com.github.nightdev.kitEngine.manager.obj;

import com.github.nightdev.kitEngine.utils.KitUtils;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public record KitMeta(
        ItemStack display,
        int slot,
        String color
) implements ConfigurationSerializable {

    public static KitMeta def() {
        return new KitMeta(
                ItemStack.of(Material.CHEST),
                -1,
                KitUtils.color("light_blue")
        );
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of(
                "display", display,
                "slot", slot,
                "color", color
        );
    }

    public static KitMeta deserialize(Map<String, Object> data) {
        return new KitMeta(
                (ItemStack) data.getOrDefault("display", ItemStack.of(Material.CHEST)),
                (int) data.getOrDefault("slot", -1),
                (String) data.getOrDefault("color", KitUtils.color("light_blue"))
        );
    }
}
