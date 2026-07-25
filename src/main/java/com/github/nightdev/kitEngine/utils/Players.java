package com.github.nightdev.kitEngine.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Players {
    public static List<String> get() {
        List<String> e = new ArrayList<>();
        e.add("*");
        for (Player player : Bukkit.getOnlinePlayers()) {
            e.add(player.getName());
        }
        return e;
    }
    public static List<Player> get(String input) {
        if (input.equalsIgnoreCase("*")) {
            return new ArrayList<>(Bukkit.getOnlinePlayers());
        }
        Player player = Bukkit.getPlayer(input);
        if (player != null) {
            return List.of(player);
        } else {
            return new ArrayList<>();
        }
    }

    public static void apply(String input, Consumer<Player> consumer) {
        List<Player> players = get(input);
        for (Player p : players) {
            consumer.accept(p);
        }
    }
}
