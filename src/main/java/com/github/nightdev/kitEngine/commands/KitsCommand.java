package com.github.nightdev.kitEngine.commands;

import com.github.nightdev.kitEngine.guis.KitsGui;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;

public class KitsCommand implements BasicCommand {
    @Override
    public void execute(CommandSourceStack css, String[] args) {

        if (!(css.getSender() instanceof Player player)) {
            css.getSender().sendMessage("Only players can do this.");
            return;
        }

        player.openInventory(new KitsGui(1, player).getInventory());

    }
}
