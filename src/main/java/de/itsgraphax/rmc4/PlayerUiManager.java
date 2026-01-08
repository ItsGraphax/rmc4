package de.itsgraphax.rmc4;

import de.itsgraphax.rmc4.utils.Namespaces;
import enums.InteractionState;
import enums.ResourcePackLetter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

public class PlayerUiManager {
    /**
     * Generate action bar
     */
    private static Component generateActionBarForPlayer(JavaPlugin plugin, Player player) {
        Integer tmp = player.getPersistentDataContainer().get(Namespaces.interactionState(plugin), PersistentDataType.INTEGER);
        String result;
        if (tmp == null) {
            result = "<null>";
        } else {
            result = tmp.toString();
        }
        return Component.text(result);
    }

    private static @Nullable Component generateSubtitleForPlayer(JavaPlugin plugin, Player player) {
        if (InteractionManager.checkPlayerInteractionState(plugin, player, InteractionState.NONE)) {
            return null;
        }

        Integer remainingTime = InteractionManager.getInteractionStateRemainingTime(plugin, player);
        Integer totalTime = player.getPersistentDataContainer().get(Namespaces.interactionStateTimeout(plugin), PersistentDataType.INTEGER);

        if (remainingTime == null || totalTime == null) {
            return null;
        }

        int percent = Math.round((float) totalTime / remainingTime);

        String result = ResourcePackLetter.COOLDOWN_BAR.toString().repeat(percent / 10 * 2); // /10 results in getting one cdb per 10% and *2 for making it double sided

        return Component.text(result);
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

        @Nullable Component title = null; // TODO: Make function
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
