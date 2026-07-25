package com.github.nightdev.kitEngine.kits.obj;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("Kit")
public class Kit implements ConfigurationSerializable {
    public boolean enabled;
    private final String name;
    public final KitMeta meta;
    public KitContents contents;

    public Kit(boolean enabled, String name, KitMeta meta, KitContents contents) {
        this.enabled = enabled;
        this.name = name;
        this.meta = meta;
        this.contents = contents;
    }

    public static Kit fromInventory(String name, PlayerInventory inv) {
        return new Kit(
                true,
                name,
                KitMeta.create(name),
                KitContents.fromInventory(inv)
        );
    }


    public String getName() {
        return this.name;
    }
    public void apply(Player player) {
        apply(player, contents);
    }
    public void apply(Player player, KitContents contents) {
        Map<Integer, ItemStack> items = contents.getItems();
        for (int i : items.keySet()) {
            ItemStack item = items.get(i);
            player.getInventory().setItem(i, item);
        }
    }


    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of(
                "enabled", this.enabled,
                "name", this.name,
                "meta", this.meta,
                "contents", this.contents
        );
    }

    public static Kit deserialize(Map<String, Object> data) {
        return new Kit(
                (boolean) data.get("enabled"),
                (String) data.get("name"),
                (KitMeta) data.get("meta"),
                (KitContents) data.get("contents")
        );
    }
}
