package com.github.nightdev.kitEngine;

import com.github.nightdev.kitEngine.commands.KitAdminCommand;
import com.github.nightdev.kitEngine.commands.KitCommand;
import com.github.nightdev.kitEngine.commands.KitsCommand;
import com.github.nightdev.kitEngine.core.*;
import com.github.nightdev.kitEngine.database.DatabaseCommand;
import com.github.nightdev.kitEngine.guis.KitAdminEditorGui;
import com.github.nightdev.kitEngine.guis.KitEditorGui;
import com.github.nightdev.kitEngine.guis.KitsGui;
import com.github.nightdev.kitEngine.guis.input.PlayerInputListener;
import com.github.nightdev.kitEngine.guis.input.SlotInputListener;
import com.github.nightdev.kitEngine.hooks.papi.KitEngineExpansion;
import com.github.nightdev.kitEngine.manager.KitMeta2;
import com.github.nightdev.kitEngine.manager.KitsManager2;
import com.github.nightdev.kitEngine.manager.obj.meta.*;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class KitEngine extends JavaPlugin {
    /*

    Add a duels system with kits
    Max/limited uses
    Favorites system
    Sort
    Search

     */

    private static KitEngine instance;

    private static Economy economy;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        KitEngineConfig.reload(this);

        KitsManager2.initialize(this);

        ConfigurationSerialization.registerClass(KitMeta2.class);

        ConfigurationSerialization.registerClass(KitCooldown.class);
        ConfigurationSerialization.registerClass(KitCost.class);
        ConfigurationSerialization.registerClass(KitEvents.class);
        ConfigurationSerialization.registerClass(KitPermission.class);
        ConfigurationSerialization.registerClass(KitRequirements.class);
        ConfigurationSerialization.registerClass(KitUses.class);

        KitEngineItems.init(this);

        PluginManager p = getServer().getPluginManager();
        p.registerEvents(new KitsGui(0, null), this);
        p.registerEvents(new KitEditorGui(null, ""), this);
        p.registerEvents(new KitAdminEditorGui(), this);

        p.registerEvents(new PlayerInputListener(), this);
        p.registerEvents(new SlotInputListener("", 1), this);


        getCommand("kit").setExecutor(new KitCommand());
        getCommand("kits").setExecutor(new KitsCommand());
        getCommand("kitadmin").setExecutor(new KitAdminCommand());
        getCommand("db").setExecutor(new DatabaseCommand());


        int pluginId = 32670;
        Metrics metrics = new Metrics(this, pluginId);

        if (p.isPluginEnabled("PlaceholderAPI")) {
            getLogger().info("Hooking into PlaceholderAPI...");
            boolean success = new KitEngineExpansion().register();
            if (success) {
                getLogger().info("Successfully hooked into PlaceholderAPI!");
            } else {
                getLogger().warning("Failed to hook into PlaceholderAPI!");
            }
        } else {
            getLogger().info("Unable to hook into PlaceholderAPI.");
        }

        setupEconomy();
        if (isEconomyHooked()) {
            getLogger().info("Successfully hooked into Vault Economy!");
        } else {
            getLogger().warning("Failed to hook into Vault Economy!");
        }
    }

    @Override
    public void onDisable() {
        KitsManager2.uninitialize();
    }
    public void setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        economy = rsp.getProvider();
    }

    public static boolean isEconomyHooked() {
        return economy != null;
    }

    public static KitEngine getInstance() {
        return instance;
    }
    public static Economy getEconomy() {
        return economy;
    }
}
