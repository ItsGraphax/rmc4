package de.itsgraphax.rmc4.listeners;

import de.itsgraphax.rmc4.Utils;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class CraftingListener implements Listener {
    private final JavaPlugin plugin;
    private final NamespacedKey recipe_token_core;
    private final NamespacedKey recipe_dupe_token;

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
        this.recipe_token_core = new NamespacedKey(plugin, Utils.CustomItemMaterial.TOKEN_CORE.toString());
        this.recipe_dupe_token = new NamespacedKey(plugin, Utils.CustomItemMaterial.TOKEN.toString());
    }

    private boolean forceCraftingCustomId(CraftingInventory inventory, Integer slot, Utils.CustomItemMaterial item_id) {
        /* Returns false if the item was not matching */
        if (inventory.getItem(slot) != null) {
            // Check if the item is not a diamond shard
            if (!Objects.equals(inventory.getItem(slot).getPersistentDataContainer()
                    .get(new NamespacedKey(plugin, "custom_item"), PersistentDataType.STRING), item_id.toString())) {
                inventory.setItem(0, null);

                return false;
            }
        }
        return true;
    }

    @EventHandler
    public void prepareItemCraftListener(PrepareItemCraftEvent event) {
        Recipe real_recipe = event.getRecipe();

        if (real_recipe != null) {
            if (real_recipe instanceof ShapedRecipe recipe) {

                // Core Crafting Recipe
                if (recipe.getKey().equals(recipe_token_core)) {
                    forceCraftingCustomId(event.getInventory(), MIDDLE_MIDDLE, Utils.CustomItemMaterial.DIAMOND_SHARD);

                } else if (recipe.getKey().equals(recipe_dupe_token)) {
                    forceCraftingCustomId(event.getInventory(), TOP_MIDDLE, Utils.CustomItemMaterial.DUPE_TOKEN_CORE);
                    forceCraftingCustomId(event.getInventory(), MIDDLE_MIDDLE, Utils.CustomItemMaterial.TOKEN_CORE);
                }
            }
        }
    }
}
