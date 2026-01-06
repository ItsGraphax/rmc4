package de.itsgraphax.rmc4.listeners;

import io.papermc.paper.datacomponent.DataComponentTypes;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class InvBlockListeners implements Listener {
    private final JavaPlugin plugin;
    private final NamespacedKey BLOCK_INV_KEY;

    public InvBlockListeners(JavaPlugin plugin) {
        this.plugin = plugin;
        this.BLOCK_INV_KEY = new NamespacedKey(plugin, "block_inv");
    }

    // Hopper Item Pickup
    @EventHandler
    public void onInventoryPickupItem(InventoryPickupItemEvent event) {
        if (Boolean.TRUE.equals(event.getItem().getPersistentDataContainer().get(BLOCK_INV_KEY, PersistentDataType.BOOLEAN))) {
            event.setCancelled(true);
        }
    }

    private boolean checkItem(ItemStack item) {
        if (item != null) {
            if (Boolean.TRUE.equals(item.getPersistentDataContainer().get(BLOCK_INV_KEY, PersistentDataType.BOOLEAN))) {
                return true;
            }


            // bundle check
            if (item.getType() == Material.BUNDLE && item.getData(DataComponentTypes.BUNDLE_CONTENTS) != null) {
                for (ItemStack iter_item : item.getData(DataComponentTypes.BUNDLE_CONTENTS).contents()) {
                    boolean result = checkItem(iter_item);
                    if (result) {
                        return true;
                    }
                }
            }
        }
        // else
        return false;
    }

    private void cancelEventIfBlock(Cancellable event, Boolean inventoryCheck, ItemStack item) {
        if (inventoryCheck && checkItem(item)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) {
            return;
        }
        switch (event.getAction()) {
            case InventoryAction.MOVE_TO_OTHER_INVENTORY -> {
                ItemStack item = event.getCurrentItem();
                if (item != null) {
                    cancelEventIfBlock(event, (event.getClickedInventory().getType() == InventoryType.PLAYER), item);
                }
            }
            case InventoryAction.PLACE_ALL, InventoryAction.PLACE_ONE, InventoryAction.PLACE_SOME,
                 InventoryAction.PLACE_FROM_BUNDLE, InventoryAction.SWAP_WITH_CURSOR ->
                    cancelEventIfBlock(event, (event.getClickedInventory().getType() != InventoryType.PLAYER), (event.getCursor()));
            case InventoryAction.HOTBAR_SWAP -> {
                ItemStack item = event.getWhoClicked().getInventory().getItem(event.getSlot());
                if (item != null) {
                    cancelEventIfBlock(event, (event.getClickedInventory().getType() != InventoryType.PLAYER), (item));
                }
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        ItemStack item = event.getCursor();
        if (item != null) {
            cancelEventIfBlock(event, (event.getInventory().getType() != InventoryType.PLAYER), (item));
        }
    }
}
