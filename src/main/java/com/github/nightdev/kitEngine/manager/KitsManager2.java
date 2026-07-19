package com.github.nightdev.kitEngine.manager;

import com.github.nightdev.kitEngine.KitEngine;
import com.github.nightdev.kitEngine.database.DatabaseManager;
import com.github.nightdev.kitEngine.manager.obj.meta.KitEvents;
import com.github.nightdev.kitEngine.manager.result.KitResult;
import com.github.nightdev.kitEngine.manager.result.KitResults;
import com.github.nightdev.kitEngine.utils.KitUtils;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;

public class KitsManager2 {

    public static final DatabaseManager DATABASE = new DatabaseManager();


    public static void initialize(KitEngine plugin) {

        File file = new File(plugin.getDataFolder(), "database.db");

        DATABASE.connect(file);

        // Enables ON DELETE CASCADE
        DATABASE.execute("PRAGMA foreign_keys = ON;");


        DATABASE.execute("""
            CREATE TABLE IF NOT EXISTS kits (
                kitName TEXT NOT NULL,

                PRIMARY KEY(kitName)
            )
        """);


        DATABASE.execute("""
            CREATE TABLE IF NOT EXISTS kit_contents (
                kitName TEXT NOT NULL,
                slot INTEGER NOT NULL,
                item TEXT NOT NULL,

                PRIMARY KEY(kitName, slot),

                FOREIGN KEY(kitName)
                REFERENCES kits(kitName)
                ON DELETE CASCADE
            )
        """);


        DATABASE.execute("""
    CREATE TABLE IF NOT EXISTS kit_meta (
        kitName TEXT NOT NULL,
        meta TEXT NOT NULL,

        PRIMARY KEY(kitName),

        FOREIGN KEY(kitName)
        REFERENCES kits(kitName)
        ON DELETE CASCADE
    )
""");

        DATABASE.execute("""
    CREATE TABLE IF NOT EXISTS player_layouts (
        kitName TEXT NOT NULL,
        uuid TEXT NOT NULL,
        slot INTEGER NOT NULL,
        item TEXT NOT NULL,

        PRIMARY KEY(kitName, uuid, slot),

        FOREIGN KEY(kitName)
        REFERENCES kits(kitName)
        ON DELETE CASCADE
    )
""");


        DATABASE.execute("""
    CREATE TABLE IF NOT EXISTS kit_cooldowns (
        kitName TEXT NOT NULL,
        uuid TEXT NOT NULL,
        cooldown INTEGER NOT NULL,

        PRIMARY KEY(kitName, uuid),

        FOREIGN KEY(kitName)
        REFERENCES kits(kitName)
        ON DELETE CASCADE
    )
""");
        DATABASE.execute("""
                CREATE TABLE IF NOT EXISTS kit_uses (
                    kitName TEXT NOT NULL,
                    uuid TEXT NOT NULL,
                    uses INTEGER NOT NULL DEFAULT 0,
                
                    PRIMARY KEY(kitName, uuid),
                
                    FOREIGN KEY(kitName)
                    REFERENCES kits(kitName)
                    ON DELETE CASCADE
                )
                """);
    }
    public static void uninitialize() {
        DATABASE.close();
    }


    public static boolean kitExists(String kitName) {

        ResultSet result = DATABASE.query("""
            SELECT kitName
            FROM kits
            WHERE kitName=?
        """, kitName);


        try {

            return result.next();

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }
    public static List<String> getKits() {

        List<String> kits = new ArrayList<>();

        ResultSet result = DATABASE.query("""
            SELECT kitName
            FROM kits
        """);


        try {

            while (result.next()) {

                kits.add(
                        result.getString("kitName")
                );

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return kits;
    }
    public static void createKit(String kitName, KitContents2 contents) throws Exception {

        if (kitExists(kitName)) {
            throw new Exception("Kit already exists.");
        }


        DATABASE.execute("""
            INSERT INTO kits
            (kitName)

            VALUES (?)
        """,
                kitName);


        saveKitMeta(
                kitName,
                KitMeta2.def(kitName)
        );


        saveKitContents(
                kitName,
                contents
        );
    }

    public static void saveKitMeta(String kitName, KitMeta2 meta) {

        DATABASE.execute("""
        INSERT INTO kit_meta
        (
            kitName,
            meta
        )

        VALUES (?, ?)

        ON CONFLICT(kitName)

        DO UPDATE SET

            meta = excluded.meta
    """,

                kitName,
                serialize(meta)
        );
    }
    public static void saveKitContents(String kitName, KitContents2 contents) {

        deleteKitContents(kitName);


        for (Map.Entry<Integer, ItemStack> entry : contents.getItems().entrySet()) {

            DATABASE.execute("""
                INSERT INTO kit_contents
                (
                    kitName,
                    slot,
                    item
                )

                VALUES (?, ?, ?)
            """,

                    kitName,
                    entry.getKey(),
                    serialize(entry.getValue())
            );
        }
    }

    public static @NotNull KitMeta2 loadKitMeta(String kitName) {

        ResultSet result = DATABASE.query("""
        SELECT meta

        FROM kit_meta

        WHERE kitName=?
    """,
                kitName
        );

        try {

            if (!result.next()) {
                throw new RuntimeException("failed to load kit meta! (1)");
            }

            return deserializeMeta(
                    result.getString("meta")
            );

        } catch (SQLException e) {
            throw new RuntimeException("failed to load kit meta! (2)");
        }
    }
    public static @NotNull KitContents2 loadKitContents(String kitName) {

        ResultSet result = DATABASE.query("""
            SELECT
                slot,
                item

            FROM kit_contents

            WHERE kitName=?
        """, kitName);


        Map<Integer, ItemStack> items = new HashMap<>();


        try {

            while (result.next()) {

                ItemStack item =
                        deserialize(
                                result.getString("item")
                        );


                if (item != null) {

                    items.put(
                            result.getInt("slot"),
                            item
                    );
                }
            }


        } catch (SQLException e) {

            e.printStackTrace();

        }


        return new KitContents2(items);
    }

    public static void updateMeta(String kitName, Consumer<KitMeta2> updater) {


        KitMeta2 meta = loadKitMeta(kitName);


        if (meta == null) {

            throw new IllegalArgumentException(
                    "Kit meta does not exist for " + kitName
            );

        }


        updater.accept(meta);


        saveKitMeta(
                kitName,
                meta
        );
    }

    public static void deleteKit(String kitName) throws Exception {

        if (!kitExists(kitName)) {
            throw new Exception("Kit doesn't exist.");
        }


        DATABASE.execute("""
            DELETE FROM kits
            WHERE kitName=?
        """,
                kitName);
    }
    public static void deleteKitContents(String kitName) {

        DATABASE.execute("""
            DELETE FROM kit_contents
            WHERE kitName=?
        """,
                kitName);
    }

    public static KitResult claimKit(String kitName, Player player) {
        KitMeta2 meta = KitsManager2.loadKitMeta(kitName);
        if (meta == null) return KitResults.ERROR;

        if (meta.cooldown.enabled && meta.cooldown.seconds > 0) {
            long remaining = remaining(kitName, player.getUniqueId(), meta);
            if (remaining > 0) return KitResults.FAIL_COOLDOWN;
        }

        if (meta.cost.enabled && meta.cost.value > 0) {
            if (!KitEngine.isEconomyHooked()) return KitResults.error("This kit has a cost but there was no economy plugin found! Please contact an administrator!");
            EconomyResponse response = KitEngine.getEconomy().withdrawPlayer(player, meta.cost.value);
            if (!response.transactionSuccess()) return KitResults.FAIL_COST;
        }

        if (!meta.requirements.fits(player)) return KitResults.FAIL_REQUIREMENTS;

        String perm = meta.permission.toString();
        if (meta.permission.enabled && !perm.isBlank() && !player.hasPermission(perm)) return KitResults.FAIL_PERMISSION;

        if (meta.uses.enabled && meta.uses.uses > 0) {
            Bukkit.broadcast(KitUtils.format(String.valueOf(getUses(kitName, player.getUniqueId()))));
            if (getUses(kitName, player.getUniqueId()) >= meta.uses.uses) return KitResults.FAIL_USES;
        }

        if (meta.requiresEmptyInventory && !player.getInventory().isEmpty()) return KitResults.FAIL_REQUIRES_EMPTY_INVENTORY;

        addUse(kitName, player.getUniqueId());

        KitContents2 contents = KitsManager2.getKit(kitName, player.getUniqueId());
        Map<Integer, ItemStack> items = contents.getItems();
        for (int i : items.keySet()) {
            ItemStack item = items.get(i);
            player.getInventory().setItem(i, item);
        }
        return KitResults.SUCCESS;
    }
    public static void handleEvents(String kitName, Player player, KitResult result) {
        KitMeta2 meta = KitsManager2.loadKitMeta(kitName);
        if (meta != null) {
            KitEvents events = meta.events;
            if (result.success()) {
                for (String cmd : events.onSuccess) {
                    cmd = cmd.replaceAll("@player", player.getName());
                    executeConsoleCommand(cmd);
                }
            } else {
                for (String cmd : events.onFailure) {
                    cmd = cmd.replaceAll("@player", player.getName());
                    cmd = cmd.replaceAll("@reason", result.msg());
                    executeConsoleCommand(cmd);
                }
            }
        }
    }
    private static void executeConsoleCommand(String cmd) {
        Bukkit.dispatchCommand(
                Bukkit.getConsoleSender(),
                cmd
        );
    }

    public static KitContents2 getKit(String kitName, UUID player) {

        if (player == null) {
            return loadKitContents(kitName);
        }


        ResultSet result = DATABASE.query("""
        SELECT
            slot,
            item

        FROM player_layouts

        WHERE kitName=? AND uuid=?
    """,
                kitName,
                player.toString()
        );


        Map<Integer, ItemStack> items = new HashMap<>();


        try {

            while (result.next()) {

                ItemStack item = deserialize(
                        result.getString("item")
                );


                if (item != null) {

                    items.put(
                            result.getInt("slot"),
                            item
                    );

                }
            }


        } catch (SQLException e) {

            e.printStackTrace();

        }


        // No player layout = default kit
        if (items.isEmpty()) {

            return loadKitContents(kitName);

        }


        return new KitContents2(items);
    }
    public static void layout(String kitName, UUID player, @Nullable KitContents2 contents) {

        if (contents == null) contents = loadKitContents(kitName);


        DATABASE.execute("""
        DELETE FROM player_layouts

        WHERE kitName=? AND uuid=?
    """,
                kitName,
                player.toString()
        );


        for (Map.Entry<Integer, ItemStack> entry : contents.getItems().entrySet()) {


            DATABASE.execute("""
            INSERT INTO player_layouts
            (
                kitName,
                uuid,
                slot,
                item
            )

            VALUES (?, ?, ?, ?)

        """,

                    kitName,
                    player.toString(),
                    entry.getKey(),
                    serialize(entry.getValue())
            );
        }
    }
    public static void deletePlayerLayout(String kitName, UUID player) {


        DATABASE.execute("""
        DELETE FROM player_layouts

        WHERE kitName=? AND uuid=?
    """,
                kitName,
                player.toString()
        );
    }
    public static void deleteAllPlayerLayouts(String kitName) {

        DATABASE.execute("""
        DELETE FROM player_layouts
        WHERE kitName=?
    """,
                kitName
        );
    }

    public static void setCooldown(String kitName, UUID playerUUID) {

        DATABASE.execute("""
        INSERT INTO kit_cooldowns
        (
            kitName,
            uuid,
            cooldown
        )
        VALUES (?, ?, ?)

        ON CONFLICT(kitName, uuid)
        DO UPDATE SET
            cooldown = excluded.cooldown
    """,
                kitName,
                playerUUID.toString(),
                System.currentTimeMillis()
        );
    }
    public static long cooldown(String kitName, UUID playerUUID) {
        KitMeta2 meta = KitsManager2.loadKitMeta(kitName);
        long lastUse = getCooldown(kitName, playerUUID);
        long now = System.currentTimeMillis();
        return (now - lastUse) / 1000;
    }
    public static long remaining(String kitName, UUID playerUUID, KitMeta2 meta) {
        return Math.max(0, meta.cooldown.seconds - cooldown(kitName, playerUUID));
    }
    public static long getCooldown(String kitName, UUID playerUUID) {

        ResultSet result = DATABASE.query("""
        SELECT cooldown
        FROM kit_cooldowns
        WHERE kitName=? AND uuid=?
    """,
                kitName,
                playerUUID.toString()
        );

        try {

            if (result.next()) {
                return result.getLong("cooldown");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0L;
    }
    public static void removeCooldown(String kitName, UUID playerUUID) {

        DATABASE.execute("""
        DELETE FROM kit_cooldowns
        WHERE kitName=? AND uuid=?
    """,
                kitName,
                playerUUID.toString()
        );
    }

    public static int getUses(String kitName, UUID playerUUID) {

        ResultSet result = DATABASE.query("""
        SELECT uses
        FROM kit_uses
        WHERE kitName=? AND uuid=?
    """,
                kitName,
                playerUUID.toString()
        );

        try {

            if (result.next()) {
                return result.getInt("uses");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
    public static void addUse(String kitName, UUID playerUUID) {

        DATABASE.execute("""
        INSERT INTO kit_uses
        (
            kitName,
            uuid,
            uses
        )

        VALUES (?, ?, 1)

        ON CONFLICT(kitName, uuid)

        DO UPDATE SET

            uses = uses + 1
    """,
                kitName,
                playerUUID.toString()
        );
    }
    public static void removeUse(String kitName, UUID playerUUID) {

        int uses = Math.max(
                0,
                getUses(
                        kitName,
                        playerUUID
                ) - 1
        );

        setUses(
                kitName,
                playerUUID,
                uses
        );
    }
    public static void setUses(String kitName, UUID playerUUID, int uses) {

        DATABASE.execute("""
        INSERT INTO kit_uses
        (
            kitName,
            uuid,
            uses
        )

        VALUES (?, ?, ?)

        ON CONFLICT(kitName, uuid)

        DO UPDATE SET

            uses = excluded.uses
    """,
                kitName,
                playerUUID.toString(),
                Math.max(0, uses)
        );
    }
    public static void resetUses(String kitName, UUID playerUUID) {
        setUses(
                kitName,
                playerUUID,
                0
        );
    }

    private static String serialize(ItemStack item) {

        if (item == null) {
            return "";
        }


        YamlConfiguration config =
                new YamlConfiguration();


        config.set(
                "item",
                item
        );


        return config.saveToString();
    }
    private static ItemStack deserialize(String text) {

        if (text == null || text.isEmpty()) {
            return null;
        }


        YamlConfiguration config =
                new YamlConfiguration();


        try {

            config.loadFromString(text);

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }


        return config.getItemStack("item");
    }

    private static String serialize(KitMeta2 meta) {

        YamlConfiguration config =
                new YamlConfiguration();

        config.set(
                "meta",
                meta
        );

        return config.saveToString();
    }
    private static KitMeta2 deserializeMeta(String text) {

        if (text == null || text.isEmpty()) {
            return null;
        }

        YamlConfiguration config =
                new YamlConfiguration();

        try {

            config.loadFromString(text);

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }

        return (KitMeta2) config.get("meta");
    }
}