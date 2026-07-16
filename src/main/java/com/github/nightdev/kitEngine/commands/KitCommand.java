package com.github.nightdev.kitEngine.commands;

import com.github.nightdev.kitEngine.manager.KitsManager;
import com.github.nightdev.kitEngine.manager.obj.Kit;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;

public class KitCommand implements BasicCommand {
    @Override
    public void execute(CommandSourceStack css, String[] args) {
        if (!(css.getSender() instanceof Player player)) {
            css.getSender().sendMessage("Only players can execute this command.");
            return;
        }

        if (args.length > 0) {

        }

    }
}
