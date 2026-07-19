package com.github.nightdev.kitEngine.guis.input;

import com.github.nightdev.kitEngine.KitEngine;
import com.github.nightdev.kitEngine.guis.KitAdminEditorGui;
import com.github.nightdev.kitEngine.manager.KitsManager2;
import com.github.nightdev.kitEngine.utils.KitUtils;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerInputListener implements Listener {

    public static final Map<UUID, String> PLAYER_INPUT_PENDING = new HashMap<>();
    public static final Map<UUID, String> PLAYER_INPUT_PENDING_KITS = new HashMap<>();

    public static void setRequestingInput(Player player, String type, String kitName, InputType inputType) {
        PLAYER_INPUT_PENDING.put(player.getUniqueId(), type);
        PLAYER_INPUT_PENDING_KITS.put(player.getUniqueId(), kitName);
        player.closeInventory();
        player.sendMessage("Type what you want the " + type + " to be for " + kitName);
        Bukkit.getScheduler().runTaskTimer(KitEngine.getInstance(), task -> {
            if (PLAYER_INPUT_PENDING.containsKey(player.getUniqueId())) {
                player.sendTitlePart(TitlePart.TITLE, KitUtils.format("&e&lIN CHAT"));
                player.sendTitlePart(TitlePart.SUBTITLE, KitUtils.format("&7This input requires a &n" + inputType));
                player.sendTitlePart(TitlePart.TIMES, Title.Times.times(
                        Duration.ofSeconds(0),
                        Duration.ofSeconds(1),
                        Duration.ofSeconds(0)
                ));
            } else {
                PLAYER_INPUT_PENDING_KITS.remove(player.getUniqueId());
                player.sendTitlePart(TitlePart.TITLE, Component.empty());
                task.cancel();
            }
        }, 0, 19);
    }

    @EventHandler
    public void on(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (PLAYER_INPUT_PENDING.containsKey(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(KitUtils.format("&cYou cannot execute commands while entering an input! If you wish to leave, type @quit"));
        }
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
            if (!input.equalsIgnoreCase("@quit")) {
                if (pendingInput.equalsIgnoreCase("COOLDOWN")) {
                    try {
                        int seconds = Integer.parseInt(input);
                        if (seconds > 0) {
                            KitsManager2.updateMeta(kitName, meta -> {
                                meta.cooldown.seconds = seconds;
                            });
                            player.sendMessage("You have updated the cooldown to be " + seconds + " seconds.");
                        } else {
                            KitsManager2.updateMeta(kitName, meta -> meta.cooldown.seconds = -1);
                            player.sendMessage("You have removed the cooldown!");
                        }
                        success = true;
                    } catch (Exception e) {
                        player.sendMessage(input + " is not a valid integer.");
                    }
                }
                else if (pendingInput.equalsIgnoreCase("DISPLAY_NAME")) {
                    KitsManager2.updateMeta(kitName, meta -> {
                        meta.displayName = input;
                    });
                    success = true;
                }
                else if (pendingInput.equalsIgnoreCase("PERMISSION")) {
                    KitsManager2.updateMeta(kitName, meta -> {
                        meta.permission.permission = input;
                    });
                    success = true;
                }
                else if (pendingInput.equalsIgnoreCase("COST")) {
                    try {
                        double cost = Double.parseDouble(input);
                        KitsManager2.updateMeta(kitName, meta -> {
                            meta.cost.value = cost;
                        });
                        success = true;
                    } catch (Exception e) {
                        player.sendMessage(e.getMessage());
                    }
                }

                else if (pendingInput.equalsIgnoreCase("REQUIREMENTS_ADD")) {
                    KitsManager2.updateMeta(kitName, meta -> {
                        meta.requirements.list().add(input);
                    });
                    success = true;
                }
                else if (pendingInput.equalsIgnoreCase("REQUIREMENTS_REMOVE")) {
                    try {
                        int id = Integer.parseInt(input);
                        KitsManager2.updateMeta(kitName, meta -> {
                            if (id >= 0 && id < meta.requirements.list().size()) {
                                meta.requirements.list().remove(id);
                            }
                        });
                        success = true;
                    } catch (Exception e) {

                    }
                }
                else if (pendingInput.equalsIgnoreCase("ON_SUCCESS_ADD")) {
                    KitsManager2.updateMeta(kitName, meta -> {
                        meta.events.onSuccess.add(input);
                    });
                    success = true;
                }
                else if (pendingInput.equalsIgnoreCase("ON_SUCCESS_REMOVE")) {
                    try {
                        int id = Integer.parseInt(input);
                        KitsManager2.updateMeta(kitName, meta -> {
                            if (id >= 0 && id < meta.events.onSuccess.size()) {
                                meta.events.onSuccess.remove(id);
                            }
                        });
                        success = true;
                    } catch (Exception e) {
                        player.sendMessage(e.getMessage());
                    }
                }
                else if (pendingInput.equalsIgnoreCase("ON_FAILURE_ADD")) {
                    KitsManager2.updateMeta(kitName, meta -> {
                        meta.events.onFailure.add(input);
                    });
                    success = true;
                }
                else if (pendingInput.equalsIgnoreCase("ON_FAILURE_REMOVE")) {
                    try {
                        int id = Integer.parseInt(input);
                        KitsManager2.updateMeta(kitName, meta -> {
                            if (id >= 0 && id < meta.events.onFailure.size()) {
                                meta.events.onFailure.remove(id);
                            }
                        });
                        success = true;
                    } catch (Exception e) {
                        player.sendMessage(e.getMessage());
                    }
                }
                else if (pendingInput.equalsIgnoreCase("USES")) {
                    try {
                        int value = Integer.parseInt(input);
                        KitsManager2.updateMeta(kitName, meta -> {
                            meta.uses.uses = value;
                        });
                        success = true;
                    } catch (Exception e) {
                        player.sendMessage(e.getMessage());
                    }
                }
            }
             else {
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
