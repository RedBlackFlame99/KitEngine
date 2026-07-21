package com.github.nightdev.kitEngine.kits;

import com.github.nightdev.kitEngine.KitEngine;
import com.github.nightdev.kitEngine.kits.obj.Kit;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class KitsManager {
    private static KitEngine plugin;
    private static Map<String, Kit> CACHED_KITS = new HashMap<>();


    public static void reload() {
        plugin = KitEngine.getInstance();

        File[] kitsFiles = kitsFolder().listFiles();
        if (kitsFiles != null) {
            for (File kitFile : kitsFiles) {
                YamlConfiguration config = YamlConfiguration.loadConfiguration(kitFile);
                Kit kit = config.getSerializable("kit", Kit.class);
            }
        }
    }

    public static void save() {
        File[] kitsFiles = kitsFolder().listFiles();
        if (kitsFiles != null) {
            for (File file : kitsFiles) {
                if (file.delete()) {
                    Bukkit.getLogger().severe("Deleted file: " + file.getPath());
                } else {
                    Bukkit.getLogger().severe("Failed to delete file: " + file.getPath());
                }
            }
        }

        for (String kitName : CACHED_KITS.keySet()) {
            File file = new File(kitsFolder(), kitName + ".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set("kit", CACHED_KITS.get(kitName));
            try {
                config.save(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static File kitsFolder() {
        File kitsFolder = new File(plugin.getDataFolder(), "kits");
        if (kitsFolder.exists()) kitsFolder.mkdirs();
        return kitsFolder;
    }
}
