package de.itsgraphax.rmc4.listeners;

import de.itsgraphax.rmc4.Token;
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

import java.util.Objects;

public class CraftingListener implements Listener {
    private final JavaPlugin plugin;
    private final NamespacedKey recipe_token_core;
    private final NamespacedKey recipe_dupe_token;
    private final NamespacedKey recipe_token_repairer;

    private final int RESULT = 0;
    private final int TOP_LEFT = 1;
    private final int TOP_MIDDLE = 2;
    private final int TOP_RIGHT = 3;
    private final int MIDDLE_LEFT = 4;
    private final int MIDDLE_MIDDLE = 5;
    private final int MIDDLE_RIGHT = 6;
    private final int BOTTOM_LEFT = 7;
    private final int BOTTOM_MIDDLE = 8;
    private final int BOTTOM_RIGHT = 9;

    public CraftingListener(JavaPlugin plugin) {
        this.plugin = plugin;
        this.recipe_token_core = new NamespacedKey(plugin, CustomItemMaterial.TOKEN_CORE.toString());
        this.recipe_dupe_token = new NamespacedKey(plugin, CustomItemMaterial.TOKEN.toString());
        this.recipe_token_repairer = new NamespacedKey(plugin, CustomItemMaterial.TOKEN_REPAIRER.toString());
    }

    private boolean forceCraftingCustomId(CraftingInventory inventory, Integer slot, CustomItemMaterial item_id) {
        /* Returns false if the item was not matching */
        if (inventory.getItem(slot) != null) {
            // Check if the item is not a diamond shard
            if (!Objects.equals(inventory.getItem(slot).getPersistentDataContainer()
                    .get(new NamespacedKey(plugin, "custom_item"), PersistentDataType.STRING), item_id.toString())) {
                inventory.setItem(RESULT, null);

                return false;
            }
        }
        return true;
    }

    private ItemStack getItemFromEvent(PrepareItemCraftEvent event, int slot) {
        return event.getInventory().getItem(slot);
    }

    private @Nullable ItemStack hasCustomMaterial(ItemStack[] items, CustomItemMaterial material) {
        for (ItemStack item : items) {
            if (item.getPersistentDataContainer()
                    .getOrDefault(Namespaces.customItem(plugin), PersistentDataType.STRING, "<null>") ==
                    material.toString()) {
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
                if (recipe.getKey().equals(recipe_token_core)) {
                    forceCraftingCustomId(event.getInventory(), MIDDLE_MIDDLE, CustomItemMaterial.DIAMOND_SHARD);

                } else if (recipe.getKey().equals(recipe_dupe_token)) {
                    forceCraftingCustomId(event.getInventory(), TOP_MIDDLE, CustomItemMaterial.DUPE_TOKEN_CORE);
                    forceCraftingCustomId(event.getInventory(), MIDDLE_MIDDLE, CustomItemMaterial.TOKEN_CORE);

                } else if (recipe.getKey().equals(recipe_token_repairer)) {
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
                    ItemStack tokenRepairer = hasCustomMaterial(items, CustomItemMaterial.TOKEN_REPAIRER);

                    if (!token.fromDefault && tokenRepairer != null && token.broken) {
                        token.broken = false;
                        event.getInventory().setItem(RESULT, token.asItem(plugin));
                    } else {
                        event.getInventory().setItem(RESULT, null);
                    }
                }
            }
        }
    }
}
