package com.github.nightdev.kitEngine.commands;

import com.github.nightdev.kitEngine.core.KitEngineConfig;
import com.github.nightdev.kitEngine.core.KitEngineLang;
import com.github.nightdev.kitEngine.core.KitEnginePerms;
import com.github.nightdev.kitEngine.guis.KitsGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class KitsCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("");
            return true;
        }

        if (!player.hasPermission(KitEnginePerms.KITS_USE)) {
            player.sendMessage(KitEngineLang.noPermission(KitEnginePerms.KITS_USE));
            return true;
        }


        int page = 1;
        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (Exception e) {
                player.sendMessage(e.getMessage());
            }
        }

        player.openInventory(new KitsGui(page, player).getInventory());


        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        List<String> e = new ArrayList<>();

        if (args.length == 0) {
            for (int i = 1; i < KitEngineConfig.getMaxPages(); i++) {
                e.add(String.valueOf(i));
            }
        }

        if (args.length == 1) {
            for (int i = 1; i < KitEngineConfig.getMaxPages(); i++) {
                if (String.valueOf(i).startsWith(args[0].toLowerCase())) e.add(String.valueOf(i));
            }
        }

        return e;
    }
}
