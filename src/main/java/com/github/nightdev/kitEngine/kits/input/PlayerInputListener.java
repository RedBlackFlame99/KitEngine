package com.github.nightdev.kitEngine.kits.input;

import com.github.nightdev.kitEngine.KitEngine;
import com.github.nightdev.kitEngine.api.Menu;
import com.github.nightdev.kitEngine.kits.KitsManager;
import com.github.nightdev.kitEngine.kits.obj.Kit;
import com.github.nightdev.kitEngine.kits.obj.meta.KitGroup;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.lang.ref.ReferenceQueue;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerInputListener {
    private static final Map<UUID, PlayerInput> REQUESTS = new HashMap<>();
    private static final Map<UUID, String> KIT_NAMES = new HashMap<>();

    public static void setRequestingInput(Player player, PlayerInput input, String kitName) {
        REQUESTS.put(player.getUniqueId(), input);
        KIT_NAMES.put(player.getUniqueId(),kitName);
        player.closeInventory();
    }

    public static void handle(AsyncChatEvent event, Player player, String input) {
        if (!REQUESTS.containsKey(player.getUniqueId())) return;
        event.setCancelled(true);

        PlayerInput playerInput = REQUESTS.get(player.getUniqueId());
        String kitName = KIT_NAMES.get(player.getUniqueId());

        boolean success = false;
        if (playerInput == PlayerInput.DISPLAY_NAME) {
            KitsManager.editKit(kitName, kit -> {
                kit.meta.displayName = input;
            });
            success = true;
        }
        else if (playerInput == PlayerInput.GROUP) {
            KitsManager.editKit(kitName, kit -> {
                kit.meta.group = new KitGroup(input);
            });
            success = true;
        }
        else if (playerInput == PlayerInput.SLOT) {
            try {
                int i = Integer.parseInt(input);
                KitsManager.editKit(kitName, kit -> {
                    kit.meta.slot = i;
                });
                success = true;
            } catch (Exception e) {}
        }
        else if (playerInput == PlayerInput.PERMISSION) {
            KitsManager.editKit(kitName, kit -> {
                kit.meta.permission = input;
            });
            success = true;
        }
        else if (playerInput == PlayerInput.COOLDOWN) {
            try {
                KitsManager.editKit(kitName, kit -> {
                    kit.meta.cooldown = Integer.parseInt(input);
                });
                success = true;
            } catch (Exception e) {}
        }
        else if (playerInput == PlayerInput.COST) {
            try {
                KitsManager.editKit(kitName, kit -> {
                    kit.meta.cost = Integer.parseInt(input);
                });
                success = true;
            } catch (Exception e) {}
        }
        else if (playerInput == PlayerInput.USES) {
            try {
                KitsManager.editKit(kitName, kit -> {
                    kit.meta.uses = Integer.parseInt(input);
                });
                success = true;
            } catch (Exception e) {}
        }
        else if (playerInput == PlayerInput.ON_SUCCESS_ADD) {
            KitsManager.editKit(kitName, kit -> {
                kit.meta.onSuccess.add(input);
            });
            success = true;
        }
        else if (playerInput == PlayerInput.ON_SUCCESS_REMOVE) {
            try {
                int index = Integer.parseInt(input);
                KitsManager.editKit(kitName, kit -> {
                    if (index >= 0 && index < kit.meta.onSuccess.size()) {
                        kit.meta.onSuccess.remove(index);
                    }
                });
                success = true;
            } catch (Exception e) {}
        }
        else if (playerInput == PlayerInput.ON_FAILURE_ADD) {
            KitsManager.editKit(kitName, kit -> kit.meta.onFailure.add(input));
            success = true;
        }
        else if (playerInput == PlayerInput.ON_FAILURE_REMOVE) {
            try {
                int index = Integer.parseInt(input);
                KitsManager.editKit(kitName, kit -> {
                    if (index >= 0 && index < kit.meta.onFailure.size()) {
                        kit.meta.onFailure.remove(index);
                    }
                });
                success = true;
            } catch (Exception e) {}
        }

        if (success) {
            REQUESTS.remove(player.getUniqueId());
            KIT_NAMES.remove(player.getUniqueId());

            // Only because this is an async event
            Bukkit.getScheduler().runTask(KitEngine.getInstance(), task -> {
                Menu.openKitAdminMenu(player, KitsManager.retrieveKit(kitName));
            });
        }

    }
}
