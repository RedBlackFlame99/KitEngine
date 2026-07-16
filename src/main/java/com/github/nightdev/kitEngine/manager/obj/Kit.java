package com.github.nightdev.kitEngine.manager.obj;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Kit implements ConfigurationSerializable {

    public KitMeta meta;
    public KitContents contents;
    public KitPermission permission;
    public KitCooldown cooldown;

    public Kit(
            KitMeta meta,
            KitContents kitContents,
            KitPermission kitPermission,
            KitCooldown kitCooldown
    ) {
        this.meta = meta;
        this.contents = kitContents;
        this.permission = kitPermission;
        this.cooldown = kitCooldown;
    }

    public Kit(Kit kit, KitContents contents) {
        this.meta = kit.meta;
        this.contents = contents;
        this.permission = kit.permission;
        this.cooldown = kit.cooldown;
    }

    public static Kit from(PlayerInventory inv) {
        return new Kit(
                KitMeta.def(),
                KitContents.fromInventory(inv),
                new KitPermission(false, "kitengine.kit"),
                new KitCooldown(false, -1)
        );
    }

    public void setDisplayName(String displayName) {
        this.meta = new KitMeta(
                displayName,
                this.meta.display(),
                this.meta.slot(),
                this.meta.color()
        );
    }


    public void setDisplay(ItemStack item) {
        this.meta = new KitMeta(
                this.meta.displayName(),
                item,
                this.meta.slot(),
                this.meta.color()
        );
    }
    public ItemStack getDisplay() {
        return meta.display();
    }

    public void setSlot(int slot) {
        this.meta = new KitMeta(
                this.meta.displayName(),
                this.meta.display(),
                slot,
                this.meta.color()
        );
    }
    public int getSlot() {
        return meta.slot();
    }

    public void updateContents(PlayerInventory inv) {
        this.contents = KitContents.fromInventory(inv);
    }

    public void setPermission(String perm) {
        this.permission = new KitPermission(
                true,
                perm
        );
    }

    public void setCooldown(int ticks) {
        this.cooldown = new KitCooldown(
                true,
                ticks
        );
    }
    public void unsetCooldown() {
        this.cooldown = new KitCooldown(
                false,
                0
        );
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("meta", meta);
        map.put("contents", contents);
        map.put("perm", permission);
        map.put("cooldown", cooldown);
        return map;
    }

    public static Kit deserialize(Map<String, Object> data) {
        return new Kit(
                (KitMeta) data.get("meta"),
                (KitContents) data.get("contents"),
                (KitPermission) data.get("perm"),
                (KitCooldown) data.get("cooldown")
        );
    }
}
