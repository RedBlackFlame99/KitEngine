package com.github.nightdev.kitEngine.core;

import com.github.nightdev.kitEngine.utils.KitUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class KitEngineLang {
    public static String NO_PERMISSION = "You do not have permission!";

    public static Component color(String s) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(s);
    }

    public static Component noPermission(String perm) {
        return KitUtils.format("&cYou are lacking permission &4" + perm);
    }
    public static Object kitDoesNotExist(String name, boolean comp) {
        Component c = KitUtils.format("&cKit with name '" + name + "' does not exist!");
        if (comp) {
            return c;
        }
        return LegacyComponentSerializer.legacyAmpersand().serialize(c);
    }

    /*
    public static Component loadKit(Kit kit, PlayerKitClaimEvent.Result result) {
        if (result.isSuccess()) {
            return KitUtils.format("&aYou have successfully claimed the kit!");
        } else if (result == PlayerKitClaimEvent.Result.FAIL_PERMISSION) {
            return noPermission(kit.meta.permMeta.permission);
        } else if (result == PlayerKitClaimEvent.Result.FAIL_COOLDOWN) {
            return KitUtils.format("&cThis kit is on cooldown!");
        } else if (result == PlayerKitClaimEvent.Result.FAIL_REQUIREMENTS) {
            return KitUtils.format("&cYou do not meet the requirements to claim this kit!");
        } else if (result == PlayerKitClaimEvent.Result.FAIL_INV_NOT_EMPTY) {
            return KitUtils.format("&cYou do not have an empty inventory!");
        } else if (result == PlayerKitClaimEvent.Result.FAIL_COST) {
            return KitUtils.format("&cYou cannot afford this kit!");
        } else if (result == PlayerKitClaimEvent.Result.FAIL_COST_NO_ECONOMY) {
            return KitUtils.format("&cThere was no economy plugin found! Please contact an administrator!");
        }
        else {
            return KitUtils.format("Unknown result");
        }
    }

     */
}
