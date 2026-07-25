package com.github.nightdev.kitEngine.kits;

import com.github.nightdev.kitEngine.KitEngine;
import com.github.nightdev.kitEngine.api.KitEngineLang;
import com.github.nightdev.kitEngine.kits.obj.Kit;
import com.github.nightdev.kitEngine.kits.obj.KitContents;
import com.github.nightdev.kitEngine.utils.KitUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.eclipse.sisu.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class KitsManager {
    private static KitEngine plugin;
    private static final Map<String, Kit> CACHED_KITS = new HashMap<>();

    private static File COOLDOWNS_FILE;
    private static YamlConfiguration COOLDOWNS_CONFIG;

    private static File USES_FILE;
    private static YamlConfiguration USES_CONFIG;

    public static void reload() {
        plugin = KitEngine.getInstance();
        if (plugin == null) {
            Bukkit.getLogger().severe("KitEngine plugin instance is null, aborting reload!");
            return;
        }

        CACHED_KITS.clear();

        File folder = kitsFolder();
        File[] kitsFiles = (folder != null) ? folder.listFiles() : null;
        if (kitsFiles != null) {
            for (File kitFile : kitsFiles) {
                if (kitFile == null) continue;

                YamlConfiguration config = YamlConfiguration.loadConfiguration(kitFile);
                if (config == null) {
                    Bukkit.getLogger().severe("Failed to load config for kit file: " + kitFile.getPath());
                    continue;
                }

                Kit kit = config.getSerializable("kit", Kit.class);
                if (kit != null && kit.getName() != null) {
                    CACHED_KITS.put(kit.getName(), kit);
                } else {
                    Bukkit.getLogger().severe("Failed to load kit: " + kitFile.getPath());
                }
            }
        }

        COOLDOWNS_FILE = new File(plugin.getDataFolder(), "cooldowns.yml");
        COOLDOWNS_CONFIG = YamlConfiguration.loadConfiguration(COOLDOWNS_FILE);

        USES_FILE = new File(plugin.getDataFolder(), "uses.yml");
        USES_CONFIG = YamlConfiguration.loadConfiguration(USES_FILE);

        Bukkit.getScheduler().runTaskTimer(plugin, task -> {
            saveCooldowns();
            saveUses();
        }, 20 * 30, 20 * 30);
    }

    public static void createKit(Kit kit) {
        if (kit == null) {
            Bukkit.getLogger().warning("Attempted to create a null kit!");
            return;
        }
        if (kit.getName() == null) {
            Bukkit.getLogger().warning("Attempted to create a kit with a null name!");
            return;
        }

        saveKit(kit);
        CACHED_KITS.put(kit.getName(), kit);
    }

    public static void editKit(String kitName, Consumer<Kit> consumer) {
        if (kitName == null || consumer == null) {
            return;
        }

        Kit kit = CACHED_KITS.remove(kitName);

        if (kit == null) {
            return;
        }

        consumer.accept(kit);

        if (kit.getName() == null) {
            Bukkit.getLogger().warning("Kit name became null after edit, re-caching under original name.");
            CACHED_KITS.put(kitName, kit);
            saveKit(kit);
            return;
        }

        saveKit(kit);
        CACHED_KITS.put(kit.getName(), kit);
    }

    public static void deleteKit(String name) {
        if (name == null) {
            return;
        }

        File folder = kitsFolder();
        if (folder == null) {
            return;
        }

        File file = new File(folder, name + ".yml");
        if (file.exists()) {
            if (!file.delete()) {
                Bukkit.getLogger().warning("Failed to delete kit file: " + file.getPath());
            }
        }
        CACHED_KITS.remove(name);
    }

    public static Kit retrieveKit(String name) {
        if (name == null) {
            return null;
        }
        return CACHED_KITS.get(name);
    }

    public static boolean kitExists(String kitName) {
        if (kitName == null) {
            return false;
        }

        for (Kit kit : getKits()) {
            if (kit != null && kitName.equals(kit.getName())) {
                return true;
            }
        }
        return false;
    }

    public static void saveKit(Kit kit) {
        if (kit == null || kit.getName() == null) {
            Bukkit.getLogger().warning("Attempted to save a null kit or kit with null name!");
            return;
        }

        File folder = kitsFolder();
        if (folder == null) {
            Bukkit.getLogger().severe("Kits folder is null, cannot save kit: " + kit.getName());
            return;
        }

        File file = new File(folder, kit.getName() + ".yml");

        YamlConfiguration config = new YamlConfiguration();
        config.set("kit", kit);
        LayoutManager.deleteLayouts(kit);

        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void claim(String name, Player player) {
        Kit kit = retrieveKit(name);
        Map<String, String> args = Map.of(
                "player", player.getName()
        );
        if (claim(name, player, false)) {
            handleActions(kit.meta.onSuccess, args);
        } else {
            handleActions(kit.meta.onFailure, args);
        }
    }
    public static void force(String name, Player player) {
        Kit kit = retrieveKit(name);
        Map<String, String> args = Map.of(
                "player", player.getName()
        );
        if (claim(name, player, true)) {
            handleActions(kit.meta.onSuccess, args);
        } else {
            handleActions(kit.meta.onFailure, args);
        }
    }

    private static boolean claim(String name, Player player, boolean force) {
        Kit kit = retrieveKit(name);

        if (kit == null) {
            return false;
        }

        if (!force) {

            if (kit.meta == null) {
                Bukkit.getLogger().warning("Kit '" + name + "' has null meta, cannot process claim.");
                return false;
            }

            if (!kit.enabled) {
                KitEngineLang.KIT_DISABLED.send(player);
                return false;
            }

            if (kit.meta.permissionEnabled &&
                    (kit.meta.permission == null || !player.hasPermission(kit.meta.permission))) {
                KitEngineLang.KIT_NO_PERMS.send(player, kit.meta.permission);
                return false;
            }

            if (kit.meta.cooldownEnabled && getRemainingCooldown(player, kit) > 0) {
                KitEngineLang.KIT_COOLDOWN.send(player,
                        KitUtils.formatTime(getRemainingCooldown(player, kit)));
                return false;
            }

            if (kit.meta.requiresEmptyInv && !player.getInventory().isEmpty()) {
                KitEngineLang.KIT_INV_ISNT_EMPTY.send(player);
                return false;
            }
        }

        KitContents contents = LayoutManager.getLayout(player, kit);

        if (contents == null) {
            return false;
        }

        kit.apply(player, contents);

        if (!force) {
            KitEngineLang.KIT_SUCCESS.send(player, kit.getName());
            setCooldown(player, kit);
            addUse(player, kit);
        }
        return true;
    }

    private static void handleActions(List<String> commands, Map<String, String> args) {
        if (commands.isEmpty()) return;
        for (String cmd : commands) {
            for (String arg : args.keySet()) {
                String repl = args.get(arg);
                cmd = cmd.replace("@" + arg, repl);
            }
            Bukkit.dispatchCommand(
                    Bukkit.getConsoleSender(),
                    cmd
            );
        }
    }

    public static List<Kit> getKits() {
        return new ArrayList<>(CACHED_KITS.values());
    }

    public static List<String> getKitNames() {
        return new ArrayList<>(CACHED_KITS.keySet());
    }

    public static void setCooldown(Player player, Kit kit) {
        if (player == null || kit == null || kit.getName() == null || COOLDOWNS_CONFIG == null) {
            return;
        }
        if (player.getUniqueId() == null) {
            return;
        }

        COOLDOWNS_CONFIG.set(kit.getName() + "." + player.getUniqueId(), System.currentTimeMillis());
    }

    public static void unsetCooldown(Player player, @Nullable Kit kit) {
        if (player == null || COOLDOWNS_CONFIG == null || player.getUniqueId() == null) {
            return;
        }

        if (kit == null) {
            for (String key : COOLDOWNS_CONFIG.getKeys(false)) {
                if (key == null) continue;
                COOLDOWNS_CONFIG.set(key + "." + player.getUniqueId(), null);
            }
        } else {
            if (kit.getName() == null) {
                return;
            }
            COOLDOWNS_CONFIG.set(kit.getName() + "." + player.getUniqueId(), null);
        }
    }

    public static long getRemainingCooldown(Player player, Kit kit) {
        if (player == null || kit == null || kit.meta == null || COOLDOWNS_CONFIG == null) {
            return 0;
        }

        if (!kit.meta.cooldownEnabled || kit.meta.cooldown <= 0) {
            return 0;
        }

        if (kit.getName() == null || player.getUniqueId() == null) {
            return 0;
        }

        String path = kit.getName() + "." + player.getUniqueId();

        if (!COOLDOWNS_CONFIG.contains(path)) {
            return 0;
        }

        long lastUse = COOLDOWNS_CONFIG.getLong(path);
        long remainingMillis = (lastUse + (kit.meta.cooldown * 1000L)) - System.currentTimeMillis();

        if (remainingMillis <= 0) {
            COOLDOWNS_CONFIG.set(path, null);
            return 0;
        }

        return (remainingMillis + 999) / 1000;
    }

    private static void saveCooldowns() {
        if (COOLDOWNS_CONFIG == null || COOLDOWNS_FILE == null) {
            return;
        }

        try {
            COOLDOWNS_CONFIG.save(COOLDOWNS_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addUse(Player player, Kit kit) {
        if (player == null || kit == null || kit.getName() == null || USES_CONFIG == null) {
            return;
        }

        String path = kit.getName() + "." + player.getUniqueId();

        int uses = USES_CONFIG.getInt(path, 0);
        USES_CONFIG.set(path, uses + 1);
    }

    public static int getUses(Player player, Kit kit) {
        if (player == null || kit == null || kit.getName() == null || USES_CONFIG == null) {
            return 0;
        }

        String path = kit.getName() + "." + player.getUniqueId();

        return USES_CONFIG.getInt(path, 0);
    }

    public static void resetUses(Player player, Kit kit) {
        if (player == null || kit == null || kit.getName() == null || USES_CONFIG == null) {
            return;
        }

        String path = kit.getName() + "." + player.getUniqueId();

        USES_CONFIG.set(path, null);
    }

    private static void saveUses() {
        if (USES_CONFIG == null || USES_FILE == null) {
            return;
        }

        try {
            USES_CONFIG.save(USES_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static File kitsFolder() {
        if (plugin == null) {
            Bukkit.getLogger().severe("Cannot resolve kits folder, plugin instance is null!");
            return null;
        }

        File dataFolder = plugin.getDataFolder();

        File kitsFolder = new File(dataFolder, "kits");
        if (!kitsFolder.exists()) kitsFolder.mkdirs();
        return kitsFolder;
    }
}