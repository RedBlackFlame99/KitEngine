package com.github.nightdev.kitEngine.core;

import com.github.nightdev.kitEngine.KitEngine;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class KitEngineConfig {

    private static FileConfiguration CONFIG;

    public static void reload(KitEngine plugin) {
        CONFIG = plugin.getConfig();
    }

    public static List<String> getColors() {
        return new ArrayList<>(CONFIG.getConfigurationSection("colors").getKeys(false));
    }
    public static String getColor(String id) {
        return "&" + CONFIG.getString("colors." + id);
    }
}
