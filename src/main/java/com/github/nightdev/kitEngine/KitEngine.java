package com.github.nightdev.kitEngine;

import com.github.nightdev.kitEngine.kits.obj.Kit;
import com.github.nightdev.kitEngine.kits.obj.KitContents;
import com.github.nightdev.kitEngine.kits.obj.KitMeta;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public final class KitEngine extends JavaPlugin {

    private static KitEngine instance;

    @Override
    public void onEnable() {
        instance = this;

        ConfigurationSerialization.registerClass(Kit.class);
        ConfigurationSerialization.registerClass(KitMeta.class);
        ConfigurationSerialization.registerClass(KitContents.class);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static KitEngine getInstance() {
        return instance;
    }
}
