package com.github.nightdev.kitEngine;

import com.github.nightdev.kitEngine.api.KitEngineItems;
import com.github.nightdev.kitEngine.commands.KitAdminCommand;
import com.github.nightdev.kitEngine.commands.KitCommand;
import com.github.nightdev.kitEngine.commands.KitsCommand;
import com.github.nightdev.kitEngine.kits.KitsListener;
import com.github.nightdev.kitEngine.kits.KitsManager;
import com.github.nightdev.kitEngine.kits.LayoutManager;
import com.github.nightdev.kitEngine.kits.obj.Kit;
import com.github.nightdev.kitEngine.kits.obj.KitContents;
import com.github.nightdev.kitEngine.kits.obj.KitMeta;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class KitEngine extends JavaPlugin {

    private static KitEngine instance;

    @Override
    public void onEnable() {
        instance = this;

        ConfigurationSerialization.registerClass(Kit.class);
        ConfigurationSerialization.registerClass(KitMeta.class);
        ConfigurationSerialization.registerClass(KitContents.class);

        KitsManager.reload();
        LayoutManager.reload();
        KitEngineItems.register();

        getCommand("kit").setExecutor(new KitCommand());
        getCommand("kits").setExecutor(new KitsCommand());
        getCommand("kitadmin").setExecutor(new KitAdminCommand());

        PluginManager p = getServer().getPluginManager();
        p.registerEvents(new KitsListener(), this);
    }

    @Override
    public void onDisable() {
        LayoutManager.shutdown();
    }

    public static KitEngine getInstance() {
        return instance;
    }
}
