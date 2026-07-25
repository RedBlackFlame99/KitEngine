package com.github.nightdev.kitEngine.kits.obj;

import com.github.nightdev.kitEngine.kits.obj.meta.KitGroup;
import com.sun.source.tree.BreakTree;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Map;

public enum KitMetaValue {
    DISPLAY_NAME("displayName", "{0}"),
    DISPLAY_ITEM("displayItem", ItemStack.of(Material.CHEST)),
    GROUP("kitGroup", KitGroup.global()),
    SLOT("slot", -1),
    REQUIRES_EMPTY_INVENTORY("requiresEmptyInv", true),
    USE_KIT_EDITOR("useKitEditor", true),


    PERMISSION_ENABLED("permissionEnabled", false),
    PERMISSION("permission", "kitengine.kit.{0}"),
    NO_PERMISSION_DISPLAY_ITEM("noPermDisplayItem", ItemStack.of(Material.BARRIER)),

    COOLDOWN_ENABLED("cooldownEnabled", true),
    COOLDOWN("cooldown", 300),
    COOLDOWN_DISPLAY_ITEM("cooldownDisplayItem", ItemStack.of(Material.BARRIER)),

    COST_ENABLED("costEnabled", false),
    COST("cost", 100.0),
    COST_DISPLAY_ITEM("costDisplayItem", ItemStack.of(Material.BARRIER)),

    USES_ENABLED("usesEnabled", false),
    USES("uses", 10),
    USES_DISPLAY_ITEM("usesDisplayItem", ItemStack.of(Material.BARRIER)),

    ON_SUCCESS("onSuccess", new ArrayList<>()),
    ON_FAILURE("onFailure", new ArrayList<>())

    ;

    private final String path;
    private final Object defaultValue;

    KitMetaValue(String path, Object defaultValue) {
        this.path = path;
        this.defaultValue = defaultValue;
    }

    public String getPath() {
        return this.path;
    }
    public Object getDefaultValue() {
        return this.defaultValue;
    }
    public String getDefaultValueAsString(String... replace) {
        String e = String.valueOf(this.defaultValue);
        for (int i = 0; i < replace.length; i++) {
            e = e.replace("{" + i + "}", replace[i]);
        }
        return e;
    }
    public boolean getDefaultValueAsBool() {
        return (boolean) this.defaultValue;
    }
    public int getDefaultValueAsInt() {
        return (int) this.defaultValue;
    }
    public double getDefaultValueAsDouble() {
        return (double) this.defaultValue;
    }
    public ItemStack getDefaultValueAsItem() {
        return (ItemStack) this.defaultValue;
    }

    public Object value(Map<String, Object> data) {
        if (!data.containsKey(this.getPath())) return this.defaultValue;
        return data.get(this.getPath());
    }

}
