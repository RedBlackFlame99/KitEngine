package com.github.nightdev.kitEngine.guis.input;

import com.github.nightdev.kitEngine.KitEngine;
import com.github.nightdev.kitEngine.core.KitEngineConfig;
import com.github.nightdev.kitEngine.guis.KitAdminEditorGui;
import com.github.nightdev.kitEngine.manager.KitsManager;
import com.github.nightdev.kitEngine.manager.obj.Kit;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerInputListener implements Listener {

    public static final Map<UUID, String> PLAYER_INPUT_PENDING = new HashMap<>();
    public static final Map<UUID, String> PLAYER_INPUT_PENDING_KITS = new HashMap<>();

    public static void setRequestingInput(Player player, String type, String kitName) {
        PLAYER_INPUT_PENDING.put(player.getUniqueId(), type);
        PLAYER_INPUT_PENDING_KITS.put(player.getUniqueId(), kitName);
        player.closeInventory();
        player.sendMessage("Type what you want the " + type + " to be for " + kitName);
    }


    @EventHandler
    public void on(AsyncChatEvent event) {
        Player player = event.getPlayer();
        if (PLAYER_INPUT_PENDING.containsKey(player.getUniqueId())) {
            event.setCancelled(true);
            String pendingInput = PLAYER_INPUT_PENDING.get(player.getUniqueId());
            String kitName = PLAYER_INPUT_PENDING_KITS.get(player.getUniqueId());

            String input = compToString(event.message());
            boolean success = false;
            if (pendingInput.equalsIgnoreCase("COOLDOWN")) {
                try {
                    int seconds = Integer.parseInt(input);
                    if (seconds > 0) {
                        KitsManager.update(kitName, kit -> {
                            kit.setCooldown(seconds);
                        });
                        player.sendMessage("You have updated the cooldown to be " + seconds + " seconds.");
                    } else {
                        KitsManager.update(kitName, Kit::unsetCooldown);
                        player.sendMessage("You have removed the cooldown!");
                    }
                    success = true;
                } catch (Exception e) {
                    player.sendMessage(input + " is not a valid integer.");
                }
            }

            else if (pendingInput.equalsIgnoreCase("SLOT")) {
                try {
                    int slot = Integer.parseInt(input);
                    KitsManager.update(kitName, kit -> {
                        kit.setSlot(slot);
                    });
                    success = true;
                } catch (Exception e) {

                }
            }

            else if (pendingInput.equalsIgnoreCase("DISPLAY_NAME")) {
                KitsManager.update(kitName, kit -> {
                    kit.setDisplayName(input);
                });
                success = true;
            }


            else if (pendingInput.equalsIgnoreCase("PERMISSION")) {
                KitsManager.update(kitName, kit -> {
                    kit.setPermission(input);
                });
                success = true;
            }

            if (success) {
                PLAYER_INPUT_PENDING.remove(player.getUniqueId());
                PLAYER_INPUT_PENDING_KITS.remove(player.getUniqueId());
                Bukkit.getScheduler().runTaskLater(KitEngine.getInstance(), task -> {
                    player.openInventory(new KitAdminEditorGui(kitName).getInventory());
                }, 5);
            }

        }
    }

    public String compToString(Component comp) {
        return LegacyComponentSerializer.legacyAmpersand().serialize(comp);
    }
}
