package de.itsgraphax.rmc4.managers;

import de.itsgraphax.rmc4.enums.InteractionState;
import de.itsgraphax.rmc4.utils.Namespaces;
import de.itsgraphax.rmc4.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

// TODO: Make tokens triggerable

public class InteractionManager {
    /** Resets the player's interaction state to NONE */
    public  static void resetPlayerInteractionState(JavaPlugin plugin, Player player) {
        setPlayerInteractionState(plugin, player, InteractionState.NONE, null, null, null);
    }

    /**
     * Sets the player's interaction state
     */
    public static void setPlayerInteractionState(JavaPlugin plugin, Player player, InteractionState state, @Nullable ItemStack interactionItem, @Nullable EquipmentSlot hand, @Nullable Integer timeoutS) {
        // Update item uuid if needed
        Utils.generateUuidForItem(plugin, interactionItem);

        String itemUuid;
        if (interactionItem != null) {
            itemUuid = interactionItem.getPersistentDataContainer()
                    .get(Namespaces.itemUuid(plugin), PersistentDataType.STRING);
        } else {
            itemUuid = "<null>";
        }

        PersistentDataContainer pdc = player.getPersistentDataContainer();

        // Set Interaction State
        pdc.set(Namespaces.interactionState(plugin), PersistentDataType.INTEGER, state.getId());

        // Set Interaction State Item
        if (itemUuid != null) {
            pdc.set(Namespaces.interactionStateItemUuid(plugin), PersistentDataType.STRING, itemUuid);
        } else {
            pdc.remove(Namespaces.interactionStateItemUuid(plugin));
        }

        // Set Interaction State Timestamp
        pdc.set(Namespaces.interactionStateTimestamp(plugin), PersistentDataType.STRING, LocalDateTime.now().toString());

        // Set Interaction State Hand
        if (hand != null) {
            pdc.set(Namespaces.interactionStateHand(plugin), PersistentDataType.INTEGER, hand.ordinal());
        } else {
            pdc.remove(Namespaces.interactionStateHand(plugin));
        }

        // Schedule timeout
        if (state != InteractionState.NONE && timeoutS != null) {
            pdc.set(Namespaces.interactionStateTimeout(plugin), PersistentDataType.INTEGER, timeoutS);

            plugin.getServer().getScheduler().runTaskLater(plugin, task -> {
                setPlayerInteractionState(plugin, player, InteractionState.NONE, null, null, null);
            }, timeoutS * 20);
        } else {
            pdc.remove(Namespaces.interactionStateTimestamp(plugin));
        }
    }

    /**
     * Resets the player's interaction state if the item they are currently holding is different to the one when it was last modified
     * Returns true if it gets reset, false elsewise
     */
    public static boolean resetInteractionStateIfItemChanged(JavaPlugin plugin, Player player, @Nullable ItemStack handItem) {
        String handUuid;
        if (handItem != null) {
            handUuid = handItem.getPersistentDataContainer().get(Namespaces.itemUuid(plugin), PersistentDataType.STRING);
        } else {
            handUuid = "<null>";
        }
        String storedUuid = player.getPersistentDataContainer().get(Namespaces.interactionStateItemUuid(plugin), PersistentDataType.STRING);
        if (!Objects.equals(handUuid, storedUuid)) {
            resetPlayerInteractionState(plugin, player);
            return true;
        }
        return false;
    }

    /**
     * Gets the remaining time until the player's interaction state times out
     */
    public static Float getInteractionStateRemainingTime(JavaPlugin plugin, Player player) {
        // Now
        LocalDateTime now = LocalDateTime.now();

        // Before
        String storedString = player.getPersistentDataContainer()
                .get(Namespaces.interactionStateTimestamp(plugin), PersistentDataType.STRING);
        if (storedString == null) {
            return null;
        }
        LocalDateTime before = LocalDateTime.parse(storedString);

        // Delta
        Duration delta = Duration.between(before, now);
        float between = delta.toSeconds();

        Integer totalTime = player.getPersistentDataContainer().get(Namespaces.interactionStateTimeout(plugin), PersistentDataType.INTEGER);

        if (totalTime == null) {return null;}

        return totalTime - between;
    }

    /**
     * Returns true if the hand is same as used in the last interaction
     */
    public static boolean checkInteractionStateHand(JavaPlugin plugin, Player player, EquipmentSlot hand) {
        Integer beforeHand = player.getPersistentDataContainer().get(Namespaces.interactionStateHand(plugin), PersistentDataType.INTEGER);
        if (beforeHand == null) {
            return false;
        }
        return hand.ordinal() == beforeHand;
    }

    /**
     * Returns if the interaction state the player is in is same as the provided state
     */
    public static boolean checkPlayerInteractionState(JavaPlugin plugin, Player player, InteractionState state) {

        Integer stored = player.getPersistentDataContainer().get(Namespaces.interactionState(plugin), PersistentDataType.INTEGER);
        return Objects.equals(stored, state.getId());
    }
}
