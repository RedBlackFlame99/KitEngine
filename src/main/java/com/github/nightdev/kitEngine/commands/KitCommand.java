package com.github.nightdev.kitEngine.commands;

import com.github.nightdev.kitEngine.core.KitEngineLang;
import com.github.nightdev.kitEngine.core.KitEnginePerms;
import com.github.nightdev.kitEngine.manager.KitsManager2;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class KitCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can execute this command.");
            return false;
        }

        if (args.length > 0) {
            if (!player.hasPermission(KitEnginePerms.KIT_USE)) {
                player.sendMessage(KitEngineLang.noPermission(KitEnginePerms.KIT_USE));
                return false;
            }

            String kitName = args[0];
            try {
                KitsManager2.claimKit(kitName, player);
            } catch (Exception e) {
                player.sendMessage((ComponentLike) KitEngineLang.kitDoesNotExist(kitName, true));
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        List<String> e = new ArrayList<>();

        if (args.length == 0) {
            e.addAll(KitsManager2.getKits());
        }

        if (args.length == 1) {
            for (String kitName : KitsManager2.getKits()) {
                if (kitName.toLowerCase().startsWith(args[0].toLowerCase())) e.add(kitName);
            }
        }

        return e;
    }
}
