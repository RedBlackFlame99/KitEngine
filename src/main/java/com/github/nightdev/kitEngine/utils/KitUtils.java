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

    public static String formatTime(int seconds) {
        int days = 0;
        int hours = 0;
        int minutes = 0;
        while (seconds > 59) {
            minutes++;
            seconds-=60;
        }
        while (minutes > 59) {
            hours++;
            minutes-=60;
        }
        while (hours > 23) {
            days++;
            hours-=24;
        }
        String m = "";
        if (days > 0) {
            m = days + "d ";
        }
        if (hours > 0) {
            m = m + hours + "h ";
        }
        if (minutes > 0) {
            m = m + minutes + "m ";
        }
        if (seconds > 0) {
            m = m + seconds + "s";
        }
        return m;

    }

}
