package com.github.nightdev.kitEngine.commands;

import com.github.nightdev.kitEngine.guis.KitAdminEditorGui;
import com.github.nightdev.kitEngine.manager.KitsManager;
import com.github.nightdev.kitEngine.manager.obj.Kit;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KitAdminCommand implements BasicCommand {
    @Override
    public void execute(CommandSourceStack css, String[] args) {

        if (!(css.getSender() instanceof Player player)) {
            css.getSender().sendMessage("Only players can do this.");
            return;
        }

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("save") && args.length > 1) {
                String kitName = args[1];
                try {
                    KitsManager.create(kitName, Kit.from(player.getInventory()));
                    player.sendMessage("Successfully created kit named " + kitName);
                } catch (Exception e) {
                    player.sendMessage(e.getMessage());
                }
            }
            else if (args[0].equalsIgnoreCase("edit") && args.length > 1) {
                String kitName = args[1];
                player.openInventory(new KitAdminEditorGui(kitName).getInventory());
            }
        }

    }

    @Override
    public Collection<String> suggest(CommandSourceStack css, String[] args) {
        List<String> e = new ArrayList<>();

        if (args.length == 0) {
            e.add("save");
            e.add("edit");
            e.add("delete");
        }

        if (args.length == 1) {
            if ("save".toLowerCase().startsWith(args[0].toLowerCase())) e.add("save");
            if ("edit".toLowerCase().startsWith(args[0].toLowerCase())) e.add("edit");
            if ("delete".toLowerCase().startsWith(args[0].toLowerCase())) e.add("delete");
        }

        if (args.length == 2) {
            for (String name : KitsManager.kits()) {
                if (name.toLowerCase().startsWith(args[1].toLowerCase())) e.add(name);
            }
        }


        return e;
    }
}
