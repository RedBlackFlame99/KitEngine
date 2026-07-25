package com.github.nightdev.kitEngine.api;

import com.github.nightdev.kitEngine.KitEngine;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public enum KitEngineLang {
    PREFIX("prefix"),

    KIT_SUCCESS("kit.success"),
    KIT_DISABLED("kit.disabled"),
    KIT_NO_PERMS("kit.no-perms"),
    KIT_COOLDOWN("kit.cooldown"),
    KIT_INV_ISNT_EMPTY("kit.inv-isnt-empty"),

    SUCCESS_KIT_CREATE("kit.admin.create"),
    SUCCESS_KIT_DELETE("kit.admin.delete"),
    SUCCESS_KIT_EDIT("kit.admin.edit"),

    ERROR_NO_PERMISSION("error.no-permission"),
    ERROR_KIT_ALREADY_EXISTS("error.kit-already-exists"),
    ERROR_KIT_DOES_NOT_EXIST("error.kit-does-not-exist"),
    ;

    private static YamlConfiguration LANG_CONFIG;

    public static void reload(KitEngine plugin) {
        LANG_CONFIG = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "lang.yml"));
    }
    private final String path;

    KitEngineLang(String path) {
        this.path = path;
    }

    public Component asComponent(boolean prefix, Object... arguments) {
        if (!LANG_CONFIG.contains(this.path)) throw new RuntimeException("Invalid Lang Path: " + this.path);
        String value = LANG_CONFIG.getString(this.path);
        if (value != null) {
            int i = 0;
            for (Object arg : arguments) {
                value = value.replace("{" + i + "}", String.valueOf(arg));
                i++;
            }
            if (prefix) {
                return KitEngineLang.PREFIX.asComponent(false).append(LegacyComponentSerializer.legacyAmpersand().deserialize(value)
                        .decoration(TextDecoration.ITALIC, false));
            } else {
                return LegacyComponentSerializer.legacyAmpersand().deserialize(value)
                        .decoration(TextDecoration.ITALIC, false);
            }
        } else {
            return Component.text("Null Lang: " + this.path);
        }
    }
    public void send(Player player, Object... arguments) {
        player.sendMessage(this.asComponent(true, arguments));
    }
}
