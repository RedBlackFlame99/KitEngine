package com.github.nightdev.kitEngine.commands;

import com.github.nightdev.kitEngine.api.Menu;
import com.github.nightdev.kitEngine.kits.KitsManager;
import com.github.nightdev.kitEngine.kits.obj.meta.KitGroup;
import com.github.nightdev.kitEngine.utils.CommandUtils;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KitCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) return true;

        if (args.length > 0) {
            String kitName = args[0];
            KitsManager.claim(kitName, player);
        } else {
            Menu.openKitsMenu(player, KitGroup.global(), 1);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        List<String> e = new ArrayList<>();

        if (args.length == 0) {
            e.addAll(KitsManager.getKitNames());
        } else if (args.length == 1) {
            e.addAll(CommandUtils.suggestions(args[0], KitsManager.getKitNames()));
        }

        return e;
    }
}
