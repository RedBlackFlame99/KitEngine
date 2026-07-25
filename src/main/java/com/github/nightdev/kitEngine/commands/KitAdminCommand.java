package com.github.nightdev.kitEngine.commands;

import com.github.nightdev.kitEngine.api.KitEnginePerms;
import com.github.nightdev.kitEngine.api.Menu;
import com.github.nightdev.kitEngine.kits.KitsManager;
import com.github.nightdev.kitEngine.kits.LayoutManager;
import com.github.nightdev.kitEngine.kits.obj.Kit;
import com.github.nightdev.kitEngine.utils.KitUtils;
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
            player.sendMessage("No Permission!");
            return true;
        }

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("save") && args.length > 1) {
                String kitName = args[1];
                if (KitsManager.kitExists(kitName)) {
                    player.sendMessage("Kit with that name already exists!");
                    return true;
                }
                KitsManager.createKit(Kit.fromInventory(kitName, player.getInventory()));
            }

            else if (args[0].equalsIgnoreCase("edit") && args.length > 1) {
                String kitName = args[1];
                if (!KitsManager.kitExists(kitName)) {
                    player.sendMessage("Kit with that name doesnt exist!");
                    return true;
                }
                Menu.openKitAdminMenu(player, KitsManager.retrieveKit(kitName));
            }

            else if (args[0].equalsIgnoreCase("delete") && args.length > 1) {
                String kitName = args[1];
                if (!KitsManager.kitExists(kitName)) {
                    player.sendMessage("Kit with that name does not exist!");
                    return true;
                }
                KitsManager.deleteKit(kitName);
            }

            else if (args[0].equalsIgnoreCase("deleteKitLayouts") && args.length > 1) {
                String kitName = args[1];
                if (KitsManager.kitExists(kitName)) {
                    Kit kit = KitsManager.retrieveKit(kitName);
                    LayoutManager.deleteLayouts(kit);
                }
            }
            else if (args[0].equalsIgnoreCase("deletePlayerLayouts") && args.length > 1) {
                String targetName = args[1];
                Player target = Bukkit.getPlayer(targetName);
                if (target != null) {
                    LayoutManager.deleteLayouts(target);
                }
            }
            else if (args[0].equalsIgnoreCase("reload")) {
                KitsManager.reload();
            }
            else if (args[0].equalsIgnoreCase("test") && args.length > 1) {
                String txt = args[1];
                Component kitUtilsComp = KitUtils.format(txt);
                player.sendMessage(LegacyComponentSerializer.legacyAmpersand().serialize(kitUtilsComp));
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        List<String> e = new ArrayList<>();

        List<String> firstArgs = List.of("save", "edit", "delete", "reload", "deleteKitLayouts", "deletePlayerLayouts");
        if (args.length == 0) {
            e.addAll(firstArgs);
        }
        else if (args.length == 1) {
            for (String arg : firstArgs) {
                if (arg.toLowerCase().startsWith(args[0].toLowerCase())) e.add(arg);
            }
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("save")) {
            e.addAll(kitNames(args[1]));
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("edit")) {
            e.addAll(kitNames(args[1]));
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("delete")) {
            e.addAll(kitNames(args[1]));
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("deleteKitLayouts")) {
            e.addAll(kitNames(args[1]));
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("deletePlayerLayouts")) {
            e.addAll(kitNames(args[1]));
        }

        return e;
    }

    public Collection<String> kitNames(String before) {
        List<String> e = new ArrayList<>();
        for (String kitName : KitsManager.getKitNames()) {
            if (kitName.toLowerCase().startsWith(before.toLowerCase())) e.add(kitName);
        }
        return e;
    }
}
