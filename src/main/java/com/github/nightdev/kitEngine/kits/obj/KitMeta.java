package com.github.nightdev.kitEngine.kits.obj;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KitMeta implements ConfigurationSerializable {

    public String displayName;
    public ItemStack displayItem;
    public int slot;
    public boolean requiresEmptyInv;
    public boolean useKitEditor;

    public boolean permissionEnabled;
    public String permission;
    public ItemStack noPermDisplayItem;

    public boolean cooldownEnabled;
    public int cooldown;
    public ItemStack cooldownDisplayItem;

    public boolean costEnabled;
    public double cost;
    public ItemStack costDisplayItem;

    public boolean usesEnabled;
    public int uses;
    public ItemStack usesDisplayItem;

    public List<String> onSuccess;
    public List<String> onFailure;

    public KitMeta(
            String displayName,
            ItemStack displayItem,
            int slot,
            boolean requiresEmptyInv,
            boolean useKitEditor,
            boolean permissionEnabled,
            String permission,
            ItemStack noPermDisplayItem,
            boolean cooldownEnabled,
            int cooldown,
            ItemStack cooldownDisplayItem,
            boolean costEnabled,
            double cost,
            ItemStack costDisplayItem,
            boolean usesEnabled,
            int uses,
            ItemStack usesDisplayItem,
            List<String> onSuccess,
            List<String> onFailure
    ) {
        this.displayName = displayName;
        this.displayItem = displayItem;
        this.slot = slot;
        this.requiresEmptyInv = requiresEmptyInv;
        this.useKitEditor = useKitEditor;
        this.permissionEnabled = permissionEnabled;
        this.permission = permission;
        this.noPermDisplayItem = noPermDisplayItem;
        this.cooldownEnabled = cooldownEnabled;
        this.cooldown = cooldown;
        this.cooldownDisplayItem = cooldownDisplayItem;
        this.costEnabled = costEnabled;
        this.cost = cost;
        this.costDisplayItem = costDisplayItem;
        this.usesEnabled = usesEnabled;
        this.uses = uses;
        this.usesDisplayItem = usesDisplayItem;
        this.onSuccess = onSuccess;
        this.onFailure = onFailure;
    }

    public static KitMeta create(String kitName) {
        return new KitMeta(
                KitMetaValue.DISPLAY_NAME.getDefaultValueAsString(kitName),
                KitMetaValue.DISPLAY_ITEM.getDefaultValueAsItem(),
                KitMetaValue.SLOT.getDefaultValueAsInt(),
                KitMetaValue.REQUIRES_EMPTY_INVENTORY.getDefaultValueAsBool(),
                KitMetaValue.USE_KIT_EDITOR.getDefaultValueAsBool(),
                KitMetaValue.PERMISSION_ENABLED.getDefaultValueAsBool(),
                KitMetaValue.PERMISSION.getDefaultValueAsString(kitName),
                KitMetaValue.NO_PERMISSION_DISPLAY_ITEM.getDefaultValueAsItem(),
                KitMetaValue.COOLDOWN_ENABLED.getDefaultValueAsBool(),
                KitMetaValue.COOLDOWN.getDefaultValueAsInt(),
                KitMetaValue.COOLDOWN_DISPLAY_ITEM.getDefaultValueAsItem(),
                KitMetaValue.COST_ENABLED.getDefaultValueAsBool(),
                KitMetaValue.COST.getDefaultValueAsDouble(),
                KitMetaValue.COST_DISPLAY_ITEM.getDefaultValueAsItem(),
                KitMetaValue.USES_ENABLED.getDefaultValueAsBool(),
                KitMetaValue.USES.getDefaultValueAsInt(),
                KitMetaValue.USES_DISPLAY_ITEM.getDefaultValueAsItem(),
                (List<String>) KitMetaValue.ON_SUCCESS.getDefaultValue(),
                (List<String>) KitMetaValue.ON_FAILURE.getDefaultValue()
        );
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put(KitMetaValue.DISPLAY_NAME.getPath(), this.displayName);
        data.put(KitMetaValue.DISPLAY_ITEM.getPath(), this.displayItem);
        data.put(KitMetaValue.SLOT.getPath(), this.slot);
        data.put(KitMetaValue.REQUIRES_EMPTY_INVENTORY.getPath(), this.requiresEmptyInv);
        data.put(KitMetaValue.USE_KIT_EDITOR.getPath(), this.useKitEditor);
        data.put(KitMetaValue.PERMISSION_ENABLED.getPath(), this.permissionEnabled);
        data.put(KitMetaValue.PERMISSION.getPath(), this.permission);
        data.put(KitMetaValue.NO_PERMISSION_DISPLAY_ITEM.getPath(), this.noPermDisplayItem);
        data.put(KitMetaValue.COOLDOWN_ENABLED.getPath(), this.cooldownEnabled);
        data.put(KitMetaValue.COOLDOWN.getPath(), this.cooldown);
        data.put(KitMetaValue.COOLDOWN_DISPLAY_ITEM.getPath(), this.cooldownDisplayItem);
        data.put(KitMetaValue.COST_ENABLED.getPath(), this.costEnabled);
        data.put(KitMetaValue.COST.getPath(), this.cost);
        data.put(KitMetaValue.COST_DISPLAY_ITEM.getPath(), this.costDisplayItem);
        data.put(KitMetaValue.USES_ENABLED.getPath(), this.usesEnabled);
        data.put(KitMetaValue.USES.getPath(), this.uses);
        data.put(KitMetaValue.USES_DISPLAY_ITEM.getPath(), this.usesDisplayItem);
        data.put(KitMetaValue.ON_SUCCESS.getPath(), this.onSuccess);
        data.put(KitMetaValue.ON_FAILURE.getPath(), this.onFailure);
        return data;
    }

    public static KitMeta deserialize(Map<String, Object> data) {
        return new KitMeta(
                (String) KitMetaValue.DISPLAY_NAME.value(data),
                (ItemStack) KitMetaValue.DISPLAY_ITEM.value(data),
                (int) KitMetaValue.SLOT.value(data),
                (boolean) KitMetaValue.REQUIRES_EMPTY_INVENTORY.value(data),
                (boolean) KitMetaValue.USE_KIT_EDITOR.value(data),

                (boolean) KitMetaValue.PERMISSION_ENABLED.value(data),
                (String) KitMetaValue.PERMISSION.value(data),
                (ItemStack) KitMetaValue.NO_PERMISSION_DISPLAY_ITEM.value(data),

                (boolean) KitMetaValue.COOLDOWN_ENABLED.value(data),
                (int) KitMetaValue.COOLDOWN.value(data),
                (ItemStack) KitMetaValue.COOLDOWN_DISPLAY_ITEM.value(data),

                (boolean) KitMetaValue.COST_ENABLED.value(data),
                (double) KitMetaValue.COST.value(data),
                (ItemStack) KitMetaValue.COST_DISPLAY_ITEM.value(data),

                (boolean) KitMetaValue.USES_ENABLED.value(data),
                (int) KitMetaValue.USES.value(data),
                (ItemStack) KitMetaValue.USES_DISPLAY_ITEM.value(data),

                (List<String>) KitMetaValue.ON_SUCCESS.value(data),
                (List<String>) KitMetaValue.ON_FAILURE.value(data)
        );
    }
}
