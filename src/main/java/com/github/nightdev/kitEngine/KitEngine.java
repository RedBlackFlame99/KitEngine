package com.github.nightdev.kitEngine;

import com.github.nightdev.kitEngine.commands.KitCommand;
import com.github.nightdev.kitEngine.commands.KitAdminCommand;
import com.github.nightdev.kitEngine.commands.KitsCommand;
import com.github.nightdev.kitEngine.core.KitEngineConfig;
import com.github.nightdev.kitEngine.core.KitEngineItems;
import com.github.nightdev.kitEngine.guis.KitAdminEditorGui;
import com.github.nightdev.kitEngine.guis.KitEditorGui;
import com.github.nightdev.kitEngine.guis.KitsGui;
import com.github.nightdev.kitEngine.guis.input.PlayerInputListener;
import com.github.nightdev.kitEngine.manager.KitsManager;
import com.github.nightdev.kitEngine.manager.obj.*;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class KitEngine extends JavaPlugin {

    private static KitEngine instance;

    @Override
    public void onEnable() {
        instance = this;

        KitEngineConfig.reload(this);
        saveDefaultConfig();

        ConfigurationSerialization.registerClass(Kit.class);
        ConfigurationSerialization.registerClass(KitMeta.class);
        ConfigurationSerialization.registerClass(KitContents.class);
        ConfigurationSerialization.registerClass(KitCooldown.class);
        ConfigurationSerialization.registerClass(KitPermission.class);

        KitEngineItems.init(this);
        KitsManager.init(this);

        PluginManager p = getServer().getPluginManager();
        p.registerEvents(new KitsGui(0, null), this);
        p.registerEvents(new KitEditorGui(null, ""), this);
        p.registerEvents(new KitAdminEditorGui(), this);

        p.registerEvents(new PlayerInputListener(), this);


        registerCommand("kit", new KitCommand());
        registerCommand("kits", new KitsCommand());
        registerCommand("kitadmin", new KitAdminCommand());

        int pluginId = 32670;
        Metrics metrics = new Metrics(this, pluginId);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static KitEngine getInstance() {
        return instance;
    }
}
