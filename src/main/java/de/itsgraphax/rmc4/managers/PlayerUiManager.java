package de.itsgraphax.rmc4.managers;

import de.itsgraphax.rmc4.Token;
import de.itsgraphax.rmc4.utils.Namespaces;
import de.itsgraphax.rmc4.enums.InteractionState;
import de.itsgraphax.rmc4.enums.ResourcePackLetter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

public class PlayerUiManager {
    private static @NotNull String generateSlotStringForPlayer(JavaPlugin plugin, Player player, int slot, String pattern) {
        Token token = Token.fromPlayer(plugin, player, slot);

        StringBuilder formattedTimeStr = new StringBuilder();

        if (token.identifier.triggerable()) {
            double time = token.getRemainingCooldownTime(plugin, player);

            String timeStr;
            if (time > 0) {
                int minutes = (int) Math.floor(time / 60);
                int seconds = (int) time % 60;

                if (minutes > 0) {
                    timeStr = "%sM %sS".formatted(minutes, seconds);
                } else {
                    timeStr = "%sS".formatted(seconds);
                }

            } else {
                timeStr = "READY";
            }

            for (char c : timeStr.toCharArray()) {
                formattedTimeStr.append(ResourcePackLetter.fromChar(c));
            }
        } else {
            formattedTimeStr.append("-");
        }

        String result = pattern;
        result = result.replace("token", token.toLetter());
        result = result.replace("time", formattedTimeStr);

        return result;
    }

    /**
     * Generate action bar
     */
    private static @NotNull Component generateActionBarForPlayer(JavaPlugin plugin, Player player) {
        Component result;

        // Interaction Cooldown
        if (!InteractionManager.checkPlayerInteractionState(plugin, player, InteractionState.NONE)) {
            // Calculate Percent
            Float remainingTime = InteractionManager.getInteractionStateRemainingTime(plugin, player);
            Integer totalTime = player.getPersistentDataContainer().get(Namespaces.interactionStateTimeout(plugin), PersistentDataType.INTEGER);
            if (remainingTime == null || totalTime == null) {
                remainingTime = 1.0F;
                totalTime = 1;
            }
            float percent = (float) remainingTime / totalTime;

            result = Component.text(ResourcePackLetter.COOLDOWN_BAR.toString().repeat((int) Math.ceil(percent * 10)));
        }
        else {
            String slot0 = generateSlotStringForPlayer(plugin, player, 0, "time token");
            String slot1 = generateSlotStringForPlayer(plugin, player, 1, "token time");

            int s0l = slot0.length();
            int s1l = slot1.length();

            if (s0l < s1l) {
                slot0 = ResourcePackLetter.EMPTY.toString().repeat(s1l - s0l) + slot0;
            } else {
                slot1 = slot1 + ResourcePackLetter.EMPTY.toString().repeat(s0l - s1l);
            }

            result = Component.text("%s %s".formatted(slot0, slot1));
        }

        return result;
    }

    /**
     * Updates the ui for the specified player
     */
    public static void updateForPlayer(JavaPlugin plugin, Player player) {
        player.sendActionBar(generateActionBarForPlayer(plugin, player));
    }

    /**
     * Updates the ui for every player
     */
    public static void updateForAll(JavaPlugin plugin) {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            updateForPlayer(plugin, player);
        }
    }

    public static void updateForAllTask(JavaPlugin plugin) {
        updateForAll(plugin);

        plugin.getServer().getScheduler().runTaskLater(plugin, task -> updateForAllTask((JavaPlugin) task.getOwner()), 5);
    }
}
