package com.github.nightdev.kitEngine;

import com.github.nightdev.kitEngine.api.KitEngineItems;
import com.github.nightdev.kitEngine.api.KitEngineLang;
import com.github.nightdev.kitEngine.commands.KitAdminCommand;
import com.github.nightdev.kitEngine.commands.KitCommand;
import com.github.nightdev.kitEngine.commands.KitsCommand;
import com.github.nightdev.kitEngine.kits.KitsListener;
import com.github.nightdev.kitEngine.kits.KitsManager;
import com.github.nightdev.kitEngine.kits.LayoutManager;
import com.github.nightdev.kitEngine.kits.obj.Kit;
import com.github.nightdev.kitEngine.kits.obj.KitContents;
import com.github.nightdev.kitEngine.kits.obj.KitMeta;
import com.github.nightdev.kitEngine.kits.obj.meta.KitGroup;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.economy.Economy;
import java.io.File;

public final class KitEngine extends JavaPlugin {

    private static KitEngine instance;
    private static Economy economy;

    @Override
    public void onEnable() {
        instance = this;

        // Kit Engine Lang
        saveResource("lang.yml", true);
        KitEngineLang.reload(this);

        ConfigurationSerialization.registerClass(Kit.class, "Kit");
        ConfigurationSerialization.registerClass(KitMeta.class, "KitMeta");
        ConfigurationSerialization.registerClass(KitContents.class, "KitContents");
        ConfigurationSerialization.registerClass(KitGroup.class, "KitGroup");

        KitsManager.reload();
        LayoutManager.reload();
        KitEngineItems.register();

        getCommand("kit").setExecutor(new KitCommand());
        getCommand("kits").setExecutor(new KitsCommand());
        getCommand("kitadmin").setExecutor(new KitAdminCommand());

        PluginManager p = getServer().getPluginManager();
        p.registerEvents(new KitsListener(), this);

        if (setupEconomy()) {
            getLogger().info("Vault dependency has been found!");
        } else {
            getLogger().warning("Vault dependency has not been found!");
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        economy = rsp.getProvider();
        return true;
    }

    @Override
    public void onDisable() {
        LayoutManager.shutdown();
    }

    public static KitEngine getInstance() {
        return instance;
    }
}
