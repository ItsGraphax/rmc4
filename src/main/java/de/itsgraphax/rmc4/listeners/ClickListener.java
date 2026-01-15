package de.itsgraphax.rmc4.listeners;

import de.itsgraphax.rmc4.managers.InteractionManager;
import de.itsgraphax.rmc4.Token;
import de.itsgraphax.rmc4.utils.Utils;
import de.itsgraphax.rmc4.enums.CustomItemMaterial;
import de.itsgraphax.rmc4.enums.InteractionState;
import de.itsgraphax.rmc4.enums.MouseButton;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ClickListener implements Listener {

    private static final int SLOT_LEFT = 0;
    private static final int SLOT_RIGHT = 1;

    private final JavaPlugin plugin;

    public ClickListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getHand() == null) return;

        finishTokenEquipSelection(event);
        startTokenEquipSelection(event);
    }

    private void startTokenEquipSelection(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        Player player = event.getPlayer();

        if (isUsingBlock(event)) return;
        if (!isToken(item)) return;
        if (!isInState(player, InteractionState.NONE)) return;
        if (!isRightClick(event)) return;

        InteractionManager.setPlayerInteractionState(
                plugin,
                player,
                InteractionState.SELECTING_EQUIP_SLOT,
                item,
                event.getHand(),
                Utils.DEFAULT_TIMEOUT_NORMAL_S
        );
    }

    private void finishTokenEquipSelection(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        Player player = event.getPlayer();

        if (isUsingBlock(event)) return;
        if (!isInState(player, InteractionState.SELECTING_EQUIP_SLOT)) return;
        if (!isSameHand(player, event.getHand())) return;
        if (InteractionManager.resetInteractionStateIfItemChanged(plugin, player, item)) return;

        int slot = slotFromClick(event);
        if (slot == -1) return; // not left/right click

        Token equipped = Token.fromPlayer(plugin, player, slot);
        if (!equipped.fromDefault) { // there is already a token in this slot
            InteractionManager.resetPlayerInteractionState(plugin, player);
            return;
        }

        if (item == null) { // safety check
            InteractionManager.resetPlayerInteractionState(plugin, player);
            return;
        }


        Token newToken = Token.fromItem(plugin, item);
        if (Token.hasToken(plugin, player, newToken.identifier) != null) {
            InteractionManager.resetPlayerInteractionState(plugin, player);
            player.sendMessage(Component.translatable("ruggimc.error.token_of_kind_already_equipped"));
            return;
        }
        newToken.intoPlayer(plugin, player, slot);

        item.setAmount(item.getAmount() - 1);
        InteractionManager.resetPlayerInteractionState(plugin, player);
    }


    private boolean isToken(ItemStack item) {
        return Utils.isCustomMaterial(plugin, item, CustomItemMaterial.TOKEN);
    }

    private boolean isInState(Player player, InteractionState state) {
        return InteractionManager.checkPlayerInteractionState(plugin, player, state);
    }

    private boolean isSameHand(Player player, EquipmentSlot hand) {
        return InteractionManager.checkInteractionStateHand(plugin, player, hand);
    }

    private boolean isRightClick(PlayerInteractEvent event) {
        return MouseButton.simplifyActionEnum(event.getAction()) == MouseButton.RIGHT;
    }

    private boolean isUsingBlock(PlayerInteractEvent event) {
        return event.useInteractedBlock() == Event.Result.ALLOW;
    }

    private int slotFromClick(PlayerInteractEvent event) {
        MouseButton mb = MouseButton.simplifyActionEnum(event.getAction());
        return switch (mb) {
            case LEFT -> SLOT_LEFT;
            case RIGHT -> SLOT_RIGHT;
            default -> -1;
        };
    }
}
