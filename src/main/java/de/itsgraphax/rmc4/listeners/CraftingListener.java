package de.itsgraphax.rmc4.listeners;

import de.itsgraphax.rmc4.Token;
import de.itsgraphax.rmc4.Utils;
import de.itsgraphax.rmc4.utils.Namespaces;
import de.itsgraphax.rmc4.enums.CustomItemMaterial;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.*;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

public class CraftingListener implements Listener {
    private final JavaPlugin plugin;

    private final NamespacedKey tokenCoreRecipe;
    private final NamespacedKey dupeTokenRecipe;
    private final NamespacedKey tokenRepairerRecipe;
    private final NamespacedKey tokenUpgraderRecipe;

    private static final int TOP_LEFT = 1;
    private static final int TOP_MIDDLE = 2;
    private static final int TOP_RIGHT = 3;
    private static final int MIDDLE_LEFT = 4;
    private static final int MIDDLE_MIDDLE = 5;
    private static final int MIDDLE_RIGHT = 6;
    private static final int BOTTOM_LEFT = 7;
    private static final int BOTTOM_MIDDLE = 8;
    private static final int BOTTOM_RIGHT = 9;

    public CraftingListener(JavaPlugin plugin) {
        this.plugin = plugin;
        this.tokenCoreRecipe = new NamespacedKey(plugin, CustomItemMaterial.TOKEN_CORE.toString());
        this.dupeTokenRecipe = new NamespacedKey(plugin, CustomItemMaterial.TOKEN.toString());
        this.tokenRepairerRecipe = new NamespacedKey(plugin, CustomItemMaterial.REPAIRER.toString());
        this.tokenUpgraderRecipe = new NamespacedKey(plugin, CustomItemMaterial.UPGRADER.toString());
    }

    private boolean forceCraftingCustomId(CraftingInventory inventory, Integer slot, CustomItemMaterial itemMaterial) {
        // Returns false if the item was not matching
        if (inventory.getItem(slot) != null) {
            // Check if the item is not the custom item
            if (!Utils.isCustomMaterial(plugin, inventory.getItem(slot), itemMaterial)) {
                inventory.setResult(null);
                return false;
            }
        } else {
            inventory.setResult(null);
            return false;
        }
        return true;
    }

    private ItemStack getItemFromEvent(PrepareItemCraftEvent event, int slot) {
        return event.getInventory().getItem(slot);
    }

    private @Nullable ItemStack hasCustomMaterial(ItemStack[] items, CustomItemMaterial material) {
        for (ItemStack item : items) {
            if (item == null) {
                continue;
            }

            if (item.getPersistentDataContainer()
                    .getOrDefault(Namespaces.customItem(plugin), PersistentDataType.STRING, "<null>").equals(material.toString())) {
                return item;
            }
        }
        return null;
    }

    @EventHandler
    public void prepareItemCraftListener(PrepareItemCraftEvent event) {
        Recipe real_recipe = event.getRecipe();

        if (real_recipe != null) {
            if (real_recipe instanceof ShapedRecipe recipe) {

                // Core Crafting Recipe
                if (recipe.getKey().equals(tokenCoreRecipe)) {
                    forceCraftingCustomId(event.getInventory(), MIDDLE_MIDDLE, CustomItemMaterial.DIAMOND_SHARD);

                } else if (recipe.getKey().equals(dupeTokenRecipe)) {
                    forceCraftingCustomId(event.getInventory(), TOP_MIDDLE, CustomItemMaterial.DUPE_TOKEN_CORE);
                    forceCraftingCustomId(event.getInventory(), MIDDLE_MIDDLE, CustomItemMaterial.TOKEN_CORE);

                } else if (recipe.getKey().equals(tokenRepairerRecipe)) {
                    forceCraftingCustomId(event.getInventory(), MIDDLE_MIDDLE, CustomItemMaterial.DIAMOND_SHARD);
                } else if (recipe.getKey().equals(tokenUpgraderRecipe)) {
                    forceCraftingCustomId(event.getInventory(), MIDDLE_MIDDLE, CustomItemMaterial.DIAMOND_SHARD);
                }
            } else if (real_recipe instanceof ShapelessRecipe recipe) {
                ItemStack[] items = {
                        getItemFromEvent(event, TOP_LEFT),
                        getItemFromEvent(event, TOP_MIDDLE),
                        getItemFromEvent(event, TOP_RIGHT),
                        getItemFromEvent(event, MIDDLE_LEFT),
                        getItemFromEvent(event, MIDDLE_MIDDLE),
                        getItemFromEvent(event, MIDDLE_RIGHT),
                        getItemFromEvent(event, BOTTOM_LEFT),
                        getItemFromEvent(event, BOTTOM_MIDDLE),
                        getItemFromEvent(event, BOTTOM_RIGHT),
                };

                if (recipe.getKey().equals(new NamespacedKey(plugin, "doubleCustom"))) {
                    Token token = Token.fromItem(plugin, hasCustomMaterial(items, CustomItemMaterial.TOKEN));
                    ItemStack tokenRepairer = hasCustomMaterial(items, CustomItemMaterial.REPAIRER);
                    ItemStack tokenUpgrader = hasCustomMaterial(items, CustomItemMaterial.UPGRADER);

                    if (!token.fromDefault && tokenRepairer != null && token.broken) {
                        token.broken = false;
                        ItemStack item = token.asItem(plugin);
                        event.getInventory().setResult(item);
                    } else if (!token.fromDefault && tokenUpgrader != null && token.level < 3) {
                        token.level += 1;
                        ItemStack item = token.asItem(plugin);
                        event.getInventory().setResult(item);
                    } else {
                        event.getInventory().setResult(null);
                    }
                }
            }
        }
    }
}
