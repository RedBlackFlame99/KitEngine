package com.github.nightdev.kitEngine.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;

public class KitTestCommand implements BasicCommand {
    @Override
    public void execute(CommandSourceStack css, String[] args) {
        if (!(css.getSender() instanceof Player player)) {
            css.getSender().sendMessage("Only players can do this");
            return;
        }

        Sign sign = (Sign) Bukkit.createBlockData("");
        player.openSign(sign, Side.FRONT);

    }
}
