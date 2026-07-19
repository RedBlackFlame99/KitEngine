package com.github.nightdev.kitEngine.database;

import com.github.nightdev.kitEngine.manager.KitsManager2;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DatabaseCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length > 0) {
            KitsManager2.DATABASE.execute("DROP TABLE IF EXISTS kit_cooldowns");
            KitsManager2.DATABASE.execute("DROP TABLE IF EXISTS player_layouts");
            KitsManager2.DATABASE.execute("DROP TABLE IF EXISTS kit_meta");
            KitsManager2.DATABASE.execute("DROP TABLE IF EXISTS kit_contents");
            KitsManager2.DATABASE.execute("DROP TABLE IF EXISTS kits");
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return List.of();
    }
}
