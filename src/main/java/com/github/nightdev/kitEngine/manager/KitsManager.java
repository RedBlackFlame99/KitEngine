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
    private static File KITS_FILE;
    private static YamlConfiguration KITS_CONFIG;

    private static File PLAYER_DATA_FOLDER;


    public static void init(KitEngine plugin) {
        KITS_FILE = new File(plugin.getDataFolder(), "kits.yml");
        KITS_CONFIG = YamlConfiguration.loadConfiguration(KITS_FILE);

        PLAYER_DATA_FOLDER = new File(plugin.getDataFolder(), "playerdata");
    }

    public static void save() {
        try {
            KITS_CONFIG.save(KITS_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        KITS_CONFIG = YamlConfiguration.loadConfiguration(KITS_FILE);
    }

    public static void create(String name, Kit kit) {
        KITS_CONFIG.set(name, kit);
        save();
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
        if (!playerLayoutFile.exists()) {
            playerLayoutFile.delete();
            return;
        }
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(playerLayoutFile);
        conf.set("layout", kitContents);

        try {
            conf.save(playerLayoutFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void load(String name, Player player) {
        Kit kit = kit(name, player);
        KitContents contents = kit.contents;
        for (int slot : contents.contents().keySet()) {
            ItemStack item = contents.contents().get(slot);
            player.getInventory().setItem(slot, item);
        }
        player.getEquipment().setHelmet(contents.helmet());
        player.getEquipment().setChestplate(contents.chestplate());
        player.getEquipment().setLeggings(contents.leggings());
        player.getEquipment().setBoots(contents.boots());
        player.getEquipment().setItemInOffHand(contents.offhand());
    }



    public static List<String> kits() {
        return new ArrayList<>(KITS_CONFIG.getKeys(false));
    }
}
