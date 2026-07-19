package com.github.nightdev.kitEngine.hooks.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KitEngineExpansion extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "kitengine";
    }

    @Override
    public @NotNull String getAuthor() {
        return "NightDevMC";
    }

    @Override
    public @NotNull String getVersion() {
        return "v1.0";
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        String[] args = params.split("_");
        if (args.length > 0) {

        }
        return "N/A";
    }
}
