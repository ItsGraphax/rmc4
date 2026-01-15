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

// TODO: Show token cooldown
public class PlayerUiManager {
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
                return null;
            }
            float percent = (float) remainingTime / totalTime;

            result = Component.text(ResourcePackLetter.COOLDOWN_BAR.toString().repeat((int) Math.ceil(percent * 10)));
        }
        else {
            Token slot0 = Token.fromPlayer(plugin, player, 0);
            Token slot1 = Token.fromPlayer(plugin, player, 1);

            result = Component.text(slot0.toLetter() + " " + slot1.toLetter());
        }

        return result;
    }

    private static @Nullable Component generateSubtitleForPlayer(JavaPlugin plugin, Player player) {
        return null;
    }

    private static @Nullable Component generateTitleForPlayer(JavaPlugin plugin, Player player) {
        return null;
    }

    /**
     * Updates the ui for the specified player
     */
    public static void updateForPlayer(JavaPlugin plugin, Player player) {
        player.sendActionBar(generateActionBarForPlayer(plugin, player));

        @Nullable Component subtitle = generateSubtitleForPlayer(plugin, player);
        if (subtitle == null) {
            subtitle = Component.empty();
        }
        player.sendTitlePart(TitlePart.SUBTITLE, subtitle);

        @Nullable Component title = generateTitleForPlayer(plugin, player);
        if (title == null) {
            title = Component.empty();
        }
        player.sendTitlePart(TitlePart.TITLE, title);
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
