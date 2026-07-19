package com.github.nightdev.kitEngine.manager;

import com.github.nightdev.kitEngine.manager.obj.meta.*;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class KitMeta2 implements ConfigurationSerializable {
    public String displayName;
    public ItemStack displayItem;
    public int slot;
    public boolean overrideInventory;
    public boolean requiresEmptyInventory;
    public boolean useKitEditor;
    public KitCooldown cooldown;
    public KitCost cost;
    public KitEvents events;
    public KitPermission permission;
    public KitRequirements requirements;
    public KitUses uses;

    public KitMeta2(
            String displayName,
            ItemStack displayItem,
            int slot,
            boolean overrideInventory,
            boolean requiresEmptyInventory,
            boolean useKitEditor,
            KitCooldown cooldown,
            KitCost cost,
            KitEvents events,
            KitPermission permission,
            KitRequirements requirements,
            KitUses uses
    ) {
        this.displayName = displayName;
        this.displayItem = displayItem;
        this.slot = slot;
        this.overrideInventory = overrideInventory;
        this.requiresEmptyInventory = requiresEmptyInventory;
        this.useKitEditor = useKitEditor;
        this.cooldown = cooldown;
        this.cost = cost;
        this.events = events;
        this.permission = permission;
        this.requirements = requirements;
        this.uses = uses;
    }

    public static KitMeta2 def() {
        return def("");
    }
    public static KitMeta2 def(String kitName) {
        return new KitMeta2(
                kitName,
                ItemStack.of(Material.CHEST),
                -1,
                true,
                true,
                true,
                new KitCooldown(true, 3600),
                new KitCost(false, 100),
                new KitEvents(),
                new KitPermission(true, "kitengine.kit." + kitName),
                new KitRequirements(),
                new KitUses()
        );
    }

    @Override
    public @NotNull Map<String, Object> serialize() {

        Map<String, Object> map = new HashMap<>();

        map.put("displayName", displayName);
        map.put("displayItem", displayItem);
        map.put("slot", slot);
        map.put("overrideInventory", overrideInventory);
        map.put("requiresEmptyInventory", requiresEmptyInventory);
        map.put("useKitEditor", useKitEditor);
        map.put("cooldown", cooldown);
        map.put("cost", cost);
        map.put("events", events);
        map.put("permission", permission);
        map.put("requirements", requirements);
        map.put("uses", uses);

        return map;
    }

    public static KitMeta2 deserialize(Map<String, Object> map) {

        return new KitMeta2(
                (String) map.get("displayName"),
                (ItemStack) map.get("displayItem"),
                (Integer) map.get("slot"),
                (Boolean) map.get("overrideInventory"),
                (Boolean) map.get("requiresEmptyInventory"),
                (Boolean) map.getOrDefault("useKitEditor", KitMeta2.def().useKitEditor),
                (KitCooldown) map.getOrDefault("cooldown", new KitCooldown()),
                (KitCost) map.getOrDefault("cost", new KitCost()),
                (KitEvents) map.getOrDefault("events", new KitEvents()),
                (KitPermission) map.getOrDefault("permission", new KitPermission()),
                (KitRequirements) map.getOrDefault("requirements", new KitRequirements()),
                (KitUses) map.getOrDefault("uses", new KitUses())
        );
    }
}
