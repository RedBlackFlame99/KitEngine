package com.github.nightdev.kitEngine.api;

import org.bukkit.entity.Player;

public enum KitEnginePerms {
    KIT_USE("kitengine.kit.use"),
    KITS_USE("kitengine.kits.use"),
    KIT_ADMIN_USE("kitengine.admin.use")
    ;

    private final String perm;
    KitEnginePerms(String perm) {
        this.perm = perm;
    }

    public boolean hasPermission(Player player) {
        return player.hasPermission(perm);
    }
}
