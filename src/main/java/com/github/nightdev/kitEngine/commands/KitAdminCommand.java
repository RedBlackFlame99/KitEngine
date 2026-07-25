package com.github.nightdev.kitEngine.commands;

import com.github.nightdev.kitEngine.api.KitEngineLang;
import com.github.nightdev.kitEngine.api.KitEnginePerms;
import com.github.nightdev.kitEngine.api.Menu;
import com.github.nightdev.kitEngine.kits.KitsManager;
import com.github.nightdev.kitEngine.kits.LayoutManager;
import com.github.nightdev.kitEngine.kits.obj.Kit;
import com.github.nightdev.kitEngine.utils.CommandUtils;
import com.github.nightdev.kitEngine.utils.KitUtils;
import com.github.nightdev.kitEngine.utils.Players;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
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

public class KitAdminCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) return true;

        if (!KitEnginePerms.KIT_ADMIN_USE.hasPermission(player)) {
            KitEngineLang.ERROR_NO_PERMISSION.send(player);
            return true;
        }

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("create") && args.length > 1) {
                String kitName = args[1];
                if (KitsManager.kitExists(kitName)) {
                    KitEngineLang.ERROR_KIT_ALREADY_EXISTS.send(player, kitName);
                    return true;
                }
                KitsManager.createKit(Kit.fromInventory(kitName, player.getInventory()));
                KitEngineLang.SUCCESS_KIT_CREATE.send(player, kitName);
            }

            else if (args[0].equalsIgnoreCase("edit") && args.length > 1) {
                String kitName = args[1];
                if (!KitsManager.kitExists(kitName)) {
                    KitEngineLang.ERROR_KIT_DOES_NOT_EXIST.send(player, kitName);
                    return true;
                }
                Menu.openKitAdminMenu(player, KitsManager.retrieveKit(kitName));
                KitEngineLang.SUCCESS_KIT_EDIT.send(player, kitName);
            }
            else if (args[0].equalsIgnoreCase("delete") && args.length > 1) {
                String kitName = args[1];
                if (!KitsManager.kitExists(kitName)) {
                    KitEngineLang.ERROR_KIT_DOES_NOT_EXIST.send(player, kitName);
                    return true;
                }
                KitsManager.deleteKit(kitName);
                KitEngineLang.SUCCESS_KIT_DELETE.send(player, kitName);
            }
            else if (args[0].equalsIgnoreCase("give") && args.length > 2) {
                String targetInput = args[1];
                String kitName = args[2];

                Players.apply(targetInput, target -> {
                    KitsManager.force(kitName, player);
                });
            }
            else if (args[0].equalsIgnoreCase("deleteKitLayouts") && args.length > 1) {
                String kitName = args[1];
                if (!KitsManager.kitExists(kitName)) {
                    KitEngineLang.ERROR_KIT_DOES_NOT_EXIST.send(player, kitName);
                    return true;
                }
                Kit kit = KitsManager.retrieveKit(kitName);
                LayoutManager.deleteLayouts(kit);
            }
            else if (args[0].equalsIgnoreCase("deletePlayerLayouts") && args.length > 1) {
                String targetInput = args[1];
                Players.apply(targetInput, target -> {
                    LayoutManager.deleteLayouts(target);
                    target.sendMessage("All of your layouts have been reset!");
                });
            }
            else if (args[0].equalsIgnoreCase("reload")) {
                KitsManager.reload();
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        List<String> e = new ArrayList<>();

        List<String> firstArgs = List.of("create", "edit", "delete", "give", "reload", "deleteKitLayouts", "deletePlayerLayouts");
        if (args.length == 0) {
            e.addAll(firstArgs);
        }
        else if (args.length == 1) {
            e.addAll(CommandUtils.suggestions(args[0], firstArgs));
        }

        else if (args.length == 2 && args[0].equalsIgnoreCase("create")) {
            e.addAll(CommandUtils.suggestions(args[1], KitsManager.getKitNames()));
        }

        else if (args.length == 2 && args[0].equalsIgnoreCase("edit")) {
            e.addAll(CommandUtils.suggestions(args[1], KitsManager.getKitNames()));
        }

        else if (args.length == 2 && args[0].equalsIgnoreCase("delete")) {
            e.addAll(CommandUtils.suggestions(args[1], KitsManager.getKitNames()));
        }

        else if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            e.addAll(CommandUtils.suggestions(args[1], Players.get()));
        }

        else if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            e.addAll(CommandUtils.suggestions(args[2], KitsManager.getKitNames()));
        }

        else if (args.length == 2 && args[0].equalsIgnoreCase("deleteKitLayouts")) {
            e.addAll(CommandUtils.suggestions(args[1], KitsManager.getKitNames()));
        }

        else if (args.length == 2 && args[0].equalsIgnoreCase("deletePlayerLayouts")) {
            e.addAll(CommandUtils.suggestions(args[1], Players.get()));
        }

        return e;
    }
}
