package de.itsgraphax.rmc4.recipes;

import de.itsgraphax.rmc4.Utils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public final class RecipeManager {
    private RecipeManager() {
    }

    private static void registerDiamondShard(JavaPlugin plugin) {
        ItemStack diamond_shard_recipe_item = Utils.getCustomItem(plugin, Utils.CustomItemMaterial.DIAMOND_SHARD);

        ShapedRecipe diamond_shard_recipe = new ShapedRecipe(new NamespacedKey(plugin, Utils.CustomItemMaterial.DIAMOND_SHARD.toString()), diamond_shard_recipe_item);
        diamond_shard_recipe.shape("pdp", "dad", "pdp");
        diamond_shard_recipe.setIngredient('d', Material.DIAMOND);
        diamond_shard_recipe.setIngredient('a', Material.AMETHYST_SHARD);
        diamond_shard_recipe.setIngredient('p', Material.PRISMARINE_SHARD);
        plugin.getServer().addRecipe(diamond_shard_recipe);
    }

    private static void registerCore(JavaPlugin plugin) {
        ItemStack core_recipe_item = Utils.getCustomItem(plugin, Utils.CustomItemMaterial.TOKEN_CORE);

        ShapedRecipe core_recipe = new ShapedRecipe(new NamespacedKey(plugin, Utils.CustomItemMaterial.TOKEN_CORE.toString()), core_recipe_item);
        core_recipe.shape(" i ", "pdp", " i ");
        core_recipe.setIngredient('i', Material.IRON_BLOCK);
        core_recipe.setIngredient('p', Material.PHANTOM_MEMBRANE);
        core_recipe.setIngredient('d', Utils.CUSTOM_ITEM_MATERIAL); // diamond shard
        plugin.getServer().addRecipe(core_recipe);
    }

    private static void registerDupeToken(JavaPlugin plugin) {
        ItemStack dupe_token_recipe_item = Utils.getTokenItem(plugin, Utils.TokenIdentifier.DUPE, false, 0);
        Utils.setBlockInv(plugin, dupe_token_recipe_item, true);

        ShapedRecipe dupe_token_recipe = new ShapedRecipe(new NamespacedKey(plugin, Utils.CustomItemMaterial.TOKEN.toString()), dupe_token_recipe_item);
        dupe_token_recipe.shape("idi", "kck", "bxb");
        dupe_token_recipe.setIngredient('i', Material.IRON_BLOCK);
        dupe_token_recipe.setIngredient('d', Utils.CUSTOM_ITEM_MATERIAL); // dupe token core
        dupe_token_recipe.setIngredient('k', Material.COAL);
        dupe_token_recipe.setIngredient('b', Material.DIAMOND);
        dupe_token_recipe.setIngredient('x', Material.DIAMOND_BLOCK);
        dupe_token_recipe.setIngredient('c', Utils.CUSTOM_ITEM_MATERIAL); // token core
        plugin.getServer().addRecipe(dupe_token_recipe);
    }

    public static void registerAll(JavaPlugin plugin) {
        registerDiamondShard(plugin);
        registerCore(plugin);
        registerDupeToken(plugin);
    }
}
