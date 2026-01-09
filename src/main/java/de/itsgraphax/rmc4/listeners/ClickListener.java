package de.itsgraphax.rmc4.listeners;

import de.itsgraphax.rmc4.Token;
import de.itsgraphax.rmc4.Utils;
import de.itsgraphax.rmc4.enums.CustomItemMaterial;
import de.itsgraphax.rmc4.InteractionManager;
import de.itsgraphax.rmc4.enums.InteractionState;
import de.itsgraphax.rmc4.enums.MouseButton;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ClickListener implements Listener {
    private final JavaPlugin plugin;

    public ClickListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private void startTokenEquipCheck(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        Player player = event.getPlayer();

        if (!Utils.isCustomMaterial(plugin, item, CustomItemMaterial.TOKEN)) {
            return;
        }

        if (!InteractionManager.checkPlayerInteractionState(plugin, player, InteractionState.NONE)) {
            return;
        }

        if (!(MouseButton.simplifyActionEnum(event.getAction()) == MouseButton.RIGHT)) {
            return;
        }

        InteractionManager.setPlayerInteractionState(plugin, player, InteractionState.SELECTING_EQUIP_SLOT, item,
                event.getHand(), Utils.DEFAULT_TIMEOUT_NORMAL_S);
    }

    private void finishTokenEquipCheck(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        Player player = event.getPlayer();

        if (!InteractionManager.checkPlayerInteractionState(plugin, player, InteractionState.SELECTING_EQUIP_SLOT)) {
            return;
        }

        if (!InteractionManager.checkInteractionStateHand(plugin, player, event.getHand())) {
            return;
        }

        if (InteractionManager.resetInteractionStateIfItemChanged(plugin, player, item)) {
            return;
        }

        MouseButton mb = MouseButton.simplifyActionEnum(event.getAction());
        if (mb == MouseButton.NONE) {return;}

        int slot = -1;
        if (mb == MouseButton.LEFT) {slot = 0;}
        else if (mb == MouseButton.RIGHT) {slot = 1;}

        Token currentTokenAtPlayerSlot = Token.fromPlayer(plugin, player, slot);
        // if slot is equipped
        if (!currentTokenAtPlayerSlot.fromDefault) {
            InteractionManager.resetPlayerInteractionState(plugin, player);
            return;
        }

        assert item != null;
        Token newToken = Token.fromItem(plugin, item);
        newToken.intoPlayer(plugin, player, slot);

        item.setAmount(item.getAmount() - 1);

        InteractionManager.resetPlayerInteractionState(plugin, player);
    }

    @EventHandler
    public void playerInteractListener(PlayerInteractEvent event) {
        if (event.getHand() != null) {
            finishTokenEquipCheck(event);
            startTokenEquipCheck(event);
        }
    }
}
