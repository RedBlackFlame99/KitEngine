package com.github.nightdev.kitEngine.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class KitUtils {
    public static Component format(String text) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(text)
                .decoration(TextDecoration.ITALIC, false);
    }

    public static String formatTime(long seconds) {
        long years = seconds / (365 * 24 * 60 * 60);
        seconds %= (365 * 24 * 60 * 60);

        long months = seconds / (30 * 24 * 60 * 60);
        seconds %= (30 * 24 * 60 * 60);

        long weeks = seconds / (7 * 24 * 60 * 60);
        seconds %= (7 * 24 * 60 * 60);

        long days = seconds / (24 * 60 * 60);
        seconds %= (24 * 60 * 60);

        long hours = seconds / (60 * 60);
        seconds %= (60 * 60);

        long minutes = seconds / 60;
        seconds %= 60;

        StringBuilder builder = new StringBuilder();

        if (years > 0) builder.append(years).append("y ");
        if (months > 0) builder.append(months).append("mo ");
        if (weeks > 0) builder.append(weeks).append("w ");
        if (days > 0) builder.append(days).append("d ");
        if (hours > 0) builder.append(hours).append("h ");
        if (minutes > 0) builder.append(minutes).append("m ");
        if (seconds > 0 || builder.isEmpty()) builder.append(seconds).append("s");

        return builder.toString().trim();
    }
}
