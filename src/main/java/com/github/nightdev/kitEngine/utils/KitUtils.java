package com.github.nightdev.kitEngine.utils;

import com.github.nightdev.kitEngine.KitEngine;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class KitUtils {
    public static Component format(String text) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(text)
                .decoration(TextDecoration.ITALIC, false);
    }

    public static String color(String id) {
        KitEngine plugin = KitEngine.getInstance();
        return "&" + plugin.getConfig().getString("colors." + id, "#FF0000");
    }

}
