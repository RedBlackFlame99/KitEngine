package com.github.nightdev.kitEngine.kits;

import com.github.nightdev.kitEngine.KitEngine;
import com.github.nightdev.kitEngine.kits.obj.Kit;
import com.github.nightdev.kitEngine.kits.obj.KitContents;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public final class LayoutManager {

    private static final Map<UUID, Map<String, KitContents>> CACHE = new HashMap<>();
    private static final Set<UUID> DIRTY = new HashSet<>();

    private static KitEngine plugin;

    public static void reload() {
        plugin = KitEngine.getInstance();

        CACHE.clear();
        DIRTY.clear();

        Bukkit.getScheduler().runTaskTimer(plugin, task -> saveDirty(), 20L * 30, 20L * 30);
    }

    /* ------------------------------------------------ */
    /* Cache                                            */
    /* ------------------------------------------------ */

    public static KitContents getLayout(Player player, Kit kit) {
        return CACHE
                .computeIfAbsent(player.getUniqueId(), k -> new HashMap<>())
                .computeIfAbsent(kit.getName(), k -> load(player.getUniqueId(), kit));
    }

    public static boolean hasLayout(Player player, Kit kit) {
        return getPlayerFile(player.getUniqueId(), kit).exists();
    }

    public static void saveLayout(Player player, Kit kit, KitContents contents) {
        CACHE
                .computeIfAbsent(player.getUniqueId(), k -> new HashMap<>())
                .put(kit.getName(), contents);

        DIRTY.add(player.getUniqueId());
    }

    public static void resetLayout(Player player, Kit kit) {
        Map<String, KitContents> layouts = CACHE.get(player.getUniqueId());

        if (layouts != null) {
            layouts.remove(kit.getName());
        }

        getPlayerFile(player.getUniqueId(), kit).delete();
    }

    public static void deleteLayout(Player player, Kit kit) {
        Map<String, KitContents> layouts = CACHE.get(player.getUniqueId());

        if (layouts != null) {
            layouts.remove(kit.getName());

            if (layouts.isEmpty()) {
                CACHE.remove(player.getUniqueId());
                DIRTY.remove(player.getUniqueId());
            } else {
                DIRTY.add(player.getUniqueId());
            }
        }

        getPlayerFile(player.getUniqueId(), kit).delete();
    }

    public static void deleteLayouts(Player player) {
        CACHE.remove(player.getUniqueId());
        DIRTY.remove(player.getUniqueId());

        File[] folders = playerDataFolder().listFiles();

        if (folders == null) {
            return;
        }

        for (File folder : folders) {
            new File(folder, player.getUniqueId() + ".yml").delete();
        }
    }

    public static void deleteLayouts(Kit kit) {
        deleteDirectory(getKitFolder(kit));

        for (Map<String, KitContents> layouts : CACHE.values()) {
            layouts.remove(kit.getName());
        }
    }

    /* ------------------------------------------------ */
    /* Saving                                           */
    /* ------------------------------------------------ */

    public static void unload(Player player) {
        save(player.getUniqueId());

        CACHE.remove(player.getUniqueId());
        DIRTY.remove(player.getUniqueId());
    }

    public static void shutdown() {
        for (UUID uuid : new HashSet<>(DIRTY)) {
            save(uuid);
        }

        CACHE.clear();
        DIRTY.clear();
    }

    private static void saveDirty() {
        for (UUID uuid : new HashSet<>(DIRTY)) {
            save(uuid);
        }
    }

    private static void save(UUID uuid) {
        Map<String, KitContents> layouts = CACHE.get(uuid);

        if (layouts == null) {
            DIRTY.remove(uuid);
            return;
        }

        for (Map.Entry<String, KitContents> entry : layouts.entrySet()) {
            File file = getPlayerFile(uuid, entry.getKey());

            YamlConfiguration config = new YamlConfiguration();
            config.set("contents", entry.getValue());

            try {
                config.save(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        DIRTY.remove(uuid);
    }

    /* ------------------------------------------------ */
    /* Loading                                          */
    /* ------------------------------------------------ */

    private static KitContents load(UUID uuid, Kit kit) {
        File file = getPlayerFile(uuid, kit);

        if (!file.exists()) {
            return kit.contents;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        KitContents contents = config.getSerializable("contents", KitContents.class);

        return contents == null ? kit.contents : contents;
    }

    /* ------------------------------------------------ */
    /* Files                                            */
    /* ------------------------------------------------ */

    private static File playerDataFolder() {
        File folder = new File(plugin.getDataFolder(), "playerdata");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        return folder;
    }

    private static File getKitFolder(Kit kit) {
        File folder = new File(playerDataFolder(), kit.getName());

        if (!folder.exists()) {
            folder.mkdirs();
        }

        return folder;
    }

    private static File getPlayerFile(UUID uuid, Kit kit) {
        return new File(getKitFolder(kit), uuid + ".yml");
    }

    private static File getPlayerFile(UUID uuid, String kitName) {
        File folder = new File(playerDataFolder(), kitName);

        if (!folder.exists()) {
            folder.mkdirs();
        }

        return new File(folder, uuid + ".yml");
    }

    private static void deleteDirectory(File file) {
        if (!file.exists()) {
            return;
        }

        File[] files = file.listFiles();

        if (files != null) {
            for (File child : files) {
                child.delete();
            }
        }

        file.delete();
    }
}