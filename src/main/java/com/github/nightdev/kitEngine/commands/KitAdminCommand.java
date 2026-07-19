package com.github.nightdev.kitEngine.commands;

import com.github.nightdev.kitEngine.KitEngine;
import com.github.nightdev.kitEngine.core.KitEngineConfig;
import com.github.nightdev.kitEngine.core.KitEngineLang;
import com.github.nightdev.kitEngine.core.KitEnginePerms;
import com.github.nightdev.kitEngine.guis.KitAdminEditorGui;
import com.github.nightdev.kitEngine.manager.KitContents2;
import com.github.nightdev.kitEngine.manager.KitsManager2;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KitAdminCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can do this.");
            return false;
        }

        if (!player.hasPermission(KitEnginePerms.ADMIN_USE)) {
            player.sendMessage(KitEngineLang.noPermission(KitEnginePerms.ADMIN_USE));
            return false;
        }

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("save") && args.length > 1) {
                if (!player.hasPermission(KitEnginePerms.ADMIN_SAVE)) {
                    player.sendMessage(KitEngineLang.noPermission(KitEnginePerms.ADMIN_SAVE));
                    return false;
                }

                String kitName = args[1];
                try {
                    KitsManager2.createKit(kitName, KitContents2.fromInventory(player.getInventory()));
                    player.sendMessage("Successfully created kit named " + kitName);
                } catch (Exception e) {
                    player.sendMessage(e.getMessage());
                }
            }
            else if (args[0].equalsIgnoreCase("edit") && args.length > 1) {
                if (!player.hasPermission(KitEnginePerms.ADMIN_EDIT)) {
                    player.sendMessage(KitEngineLang.noPermission(KitEnginePerms.ADMIN_EDIT));
                    return false;
                }

                String kitName = args[1];
                if (KitsManager2.kitExists(kitName)) {
                    player.openInventory(new KitAdminEditorGui(kitName).getInventory());
                } else {
                    player.sendMessage("Kit with that name does not exist!");
                }
            }
            else if (args[0].equalsIgnoreCase("delete") && args.length > 1) {
                if (!player.hasPermission(KitEnginePerms.ADMIN_DELETE)) {
                    player.sendMessage(KitEngineLang.noPermission(KitEnginePerms.ADMIN_DELETE));
                    return false;
                }

                String kitName = args[1];
                try {
                    KitsManager2.deleteKit(kitName);
                } catch (Exception e) {
                    player.sendMessage(e.getMessage());
                }
            }
            else if (args[0].equalsIgnoreCase("reset") && args.length > 1) {
                if (!player.hasPermission(KitEnginePerms.ADMIN_RESET)) {
                    player.sendMessage(KitEngineLang.noPermission(KitEnginePerms.ADMIN_RESET));
                    return false;
                }
                String kitName = args[1];
                UUID targetUUID = player.getUniqueId();
                if (args.length > 2) {
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[2]);
                    targetUUID = target.getUniqueId();
                }
                KitsManager2.removeCooldown(kitName, targetUUID);
            }
            else if (args[0].equalsIgnoreCase("reload")) {
                if (!player.hasPermission(KitEnginePerms.ADMIN_RELOAD)) {
                    player.sendMessage(KitEngineLang.noPermission(KitEnginePerms.ADMIN_RELOAD));
                    return false;
                }

                KitEngineConfig.reload(KitEngine.getInstance());
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        List<String> e = new ArrayList<>();

        if (args.length == 0) {
            e.add("save");
            e.add("edit");
            e.add("delete");
            e.add("reset");
            e.add("reload");
        }

        if (args.length == 1) {
            if ("save".toLowerCase().startsWith(args[0].toLowerCase())) e.add("save");
            if ("edit".toLowerCase().startsWith(args[0].toLowerCase())) e.add("edit");
            if ("delete".toLowerCase().startsWith(args[0].toLowerCase())) e.add("delete");
            if ("reset".toLowerCase().startsWith(args[0].toLowerCase())) e.add("reset");
            if ("reload".toLowerCase().startsWith(args[0].toLowerCase())) e.add("reload");
        }

        if (args.length == 2) {
            for (String name : KitsManager2.getKits()) {
                if (name.toLowerCase().startsWith(args[1].toLowerCase())) e.add(name);
            }
        }

        if (args.length == 3) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                String name = player.getName();
                if (name.toLowerCase().startsWith(args[2].toLowerCase())) e.add(name);
            }
        }


        return e;
    }
}
