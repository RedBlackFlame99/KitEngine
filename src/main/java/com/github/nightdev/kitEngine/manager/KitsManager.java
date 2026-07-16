package com.github.nightdev.kitEngine.manager;

import com.github.nightdev.kitEngine.KitEngine;
import com.github.nightdev.kitEngine.manager.obj.Kit;
import com.github.nightdev.kitEngine.manager.obj.KitContents;
import com.github.nightdev.kitEngine.utils.KitUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class KitsManager {
    /*

    ADD A KIT-version thing instead of mass deleting files

     */
    private static File KITS_FILE;
    private static YamlConfiguration KITS_CONFIG;

    private static File PLAYER_DATA_FOLDER;

    private static File COOLDOWNS_FILE;
    private static YamlConfiguration COOLDOWNS_CONFIG;


    public static void init(KitEngine plugin) {
        KITS_FILE = new File(plugin.getDataFolder(), "kits.yml");
        KITS_CONFIG = YamlConfiguration.loadConfiguration(KITS_FILE);
        PLAYER_DATA_FOLDER = new File(plugin.getDataFolder(), "playerdata");
        COOLDOWNS_FILE = new File(plugin.getDataFolder(), "cooldowns.yml");
        COOLDOWNS_CONFIG = YamlConfiguration.loadConfiguration(COOLDOWNS_FILE);
    }

    public static void save() {
        try {
            KITS_CONFIG.save(KITS_FILE);
            COOLDOWNS_CONFIG.save(COOLDOWNS_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        KITS_CONFIG = YamlConfiguration.loadConfiguration(KITS_FILE);
    }

    public static void create(String name, Kit kit) {
        KITS_CONFIG.set(name, kit);
        save();
    }
    public static void delete(String name) {
        KITS_CONFIG.set(name, null);
        save();
        wipeLayouts(name);
    }
    public static void wipeLayouts(String name) {
        File kitFolder = new File(PLAYER_DATA_FOLDER, name);
        File[] children = kitFolder.listFiles();
        if (children != null) {
            for (File child : children) {
                child.delete();
            }
        }
        kitFolder.delete();
    }

    public static void update(String name, Consumer<Kit> consumer) {
        Kit kit = KITS_CONFIG.getSerializable(name, Kit.class);
        if (kit != null) {
            consumer.accept(kit);
            KITS_CONFIG.set(name, kit);
            save();
        } else {
            Bukkit.broadcast(KitUtils.format("Invalid kitt.."));
        }
        save();
    }

    public static Kit kit(String name, Player player) {
        Kit kit = KITS_CONFIG.getSerializable(name, Kit.class);
        if (kit == null) throw new RuntimeException("Invalid kit.");
        if (player != null) {
            File kitFolder = new File(PLAYER_DATA_FOLDER, name);
            File playerFile = new File(kitFolder, player.getUniqueId() + ".yml");
            if (playerFile.exists()) {
                YamlConfiguration conf = YamlConfiguration.loadConfiguration(playerFile);
                KitContents playerContents = conf.getSerializable("layout", KitContents.class);
                if (playerContents != null) {
                    return new Kit(kit, playerContents);
                }
            }
        }
        return kit;
    }
    public static Kit kit(String name) {
        return kit(name, null);
    }

    public static void layout(String kitName, Player player, @Nullable KitContents kitContents) {
        File kitFolder = new File(PLAYER_DATA_FOLDER, kitName);
        File playerLayoutFile = new File(kitFolder, player.getUniqueId() + ".yml");
        if (kitContents == null) {
            if (!playerLayoutFile.exists()) {
                playerLayoutFile.delete();
                return;
            }
        }
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(playerLayoutFile);
        conf.set("layout", kitContents);

        try {
            conf.save(playerLayoutFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int cooldown(String kitName, Player player) {
        Kit kit = kit(kitName, player);
        if (kit.cooldown.enabled()) {
            long last = COOLDOWNS_CONFIG.getLong(kitName + "." + player.getUniqueId(), 0);
            long elapsedSeconds = (System.currentTimeMillis() - last) / 1000;
            int cooldownSeconds = kit.cooldown.seconds();
            int remaining = cooldownSeconds - Math.toIntExact(elapsedSeconds);
            return Math.max(remaining, 0);
        } else {
            return 0;
        }
    }
    public static void resetCooldown(String kitName, UUID uuid) {
        COOLDOWNS_CONFIG.set(kitName + "." + String.valueOf(uuid), null);
        save();
    }

    public static void load(String name, Player player) {
        Kit kit = kit(name, player);

        if (kit.permission.required()) {
            if (!player.hasPermission(kit.permission.permission())) {
                player.sendMessage("Failed to load kit! You are lacking the required permissions.");
                return;
            }
        }

        if (kit.cooldown.enabled()) {
            int cooldown = cooldown(name, player);
            if (cooldown > 0) {
                player.sendMessage("You are on cooldown! " + cooldown);
                return;
            }
        }

        KitContents contents = kit.contents;
        for (int slot : contents.contents().keySet()) {
            if (player.getInventory().getItem(slot) == null) {
                ItemStack item = contents.contents().get(slot);
                player.getInventory().setItem(slot, item);
            }
        }
        player.getEquipment().setHelmet(contents.helmet());
        player.getEquipment().setChestplate(contents.chestplate());
        player.getEquipment().setLeggings(contents.leggings());
        player.getEquipment().setBoots(contents.boots());
        player.getEquipment().setItemInOffHand(contents.offhand());

        COOLDOWNS_CONFIG.set(name + "." + player.getUniqueId(), System.currentTimeMillis());
        save();
    }



    public static List<String> kits() {
        return new ArrayList<>(KITS_CONFIG.getKeys(false));
    }
}
