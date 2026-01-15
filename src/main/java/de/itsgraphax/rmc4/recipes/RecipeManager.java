package de.itsgraphax.rmc4.recipes;

import de.itsgraphax.rmc4.Token;
import de.itsgraphax.rmc4.utils.Utils;
import de.itsgraphax.rmc4.enums.CustomItemMaterial;
import de.itsgraphax.rmc4.enums.TokenIdentifier;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public final class RecipeManager {
    private RecipeManager() {
    }

    private static void registerDiamondShard(JavaPlugin plugin) {
        ItemStack resultItem = Utils.getCustomItem(plugin, CustomItemMaterial.DIAMOND_SHARD);

        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, CustomItemMaterial.DIAMOND_SHARD.toString()), resultItem);
        recipe.shape("pdp", "dad", "pdp");
        recipe.setIngredient('d', Material.DIAMOND);
        recipe.setIngredient('a', Material.AMETHYST_SHARD);
        recipe.setIngredient('p', Material.PRISMARINE_SHARD);
        plugin.getServer().addRecipe(recipe);
    }

    private static void registerCore(JavaPlugin plugin) {
        ItemStack resultItem = Utils.getCustomItem(plugin, CustomItemMaterial.TOKEN_CORE);

        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, CustomItemMaterial.TOKEN_CORE.toString()), resultItem);
        recipe.shape(" i ", "pdp", " i ");
        recipe.setIngredient('i', Material.IRON_BLOCK);
        recipe.setIngredient('p', Material.PHANTOM_MEMBRANE);
        recipe.setIngredient('d', Utils.CUSTOM_ITEM_MATERIAL); // diamond shard
        plugin.getServer().addRecipe(recipe);
    }

    private static void registerDupeToken(JavaPlugin plugin) {
        ItemStack resultItem = new Token(TokenIdentifier.DUPE, false, 0).asItem(plugin);

        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, CustomItemMaterial.TOKEN.toString()), resultItem);
        recipe.shape("idi", "kck", "bxb");
        recipe.setIngredient('i', Material.IRON_BLOCK);
        recipe.setIngredient('d', Utils.CUSTOM_ITEM_MATERIAL); // dupe token core
        recipe.setIngredient('k', Material.COAL);
        recipe.setIngredient('b', Material.DIAMOND);
        recipe.setIngredient('x', Material.DIAMOND_BLOCK);
        recipe.setIngredient('c', Utils.CUSTOM_ITEM_MATERIAL); // token core
        plugin.getServer().addRecipe(recipe);
    }

    private static void registerTokenRepairer(JavaPlugin plugin) {
        ItemStack resultItem = Utils.getCustomItem(plugin, CustomItemMaterial.REPAIRER);

        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, CustomItemMaterial.REPAIRER.toString()), resultItem);
        recipe.shape("iii", "idi", "iii");
        recipe.setIngredient('i', Material.IRON_INGOT);
        recipe.setIngredient('d', Utils.CUSTOM_ITEM_MATERIAL); // diamond shard
        plugin.getServer().addRecipe(recipe);
    }

    private static void registerTokenUpgrader(JavaPlugin plugin) {
        ItemStack resultItem = Utils.getCustomItem(plugin, CustomItemMaterial.UPGRADER);

        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, CustomItemMaterial.UPGRADER.toString()), resultItem);
        recipe.shape("   ", " d ", "   ");
        recipe.setIngredient('d', Utils.CUSTOM_ITEM_MATERIAL); // diamond shard

        plugin.getServer().addRecipe(recipe);
    }

    private static void registerDoubleCustom(JavaPlugin plugin) {
        ItemStack resultItem = ItemStack.of(Material.WOODEN_HOE);

        ShapelessRecipe recipe = new ShapelessRecipe(new NamespacedKey(plugin, "doubleCustom"), resultItem);
        recipe.addIngredient(Utils.CUSTOM_ITEM_MATERIAL);
        recipe.addIngredient(Utils.CUSTOM_ITEM_MATERIAL);
        plugin.getServer().addRecipe(recipe);
    }

    public static void registerAll(JavaPlugin plugin) {
        registerDiamondShard(plugin);
        registerCore(plugin);
        registerDupeToken(plugin);
        registerTokenRepairer(plugin);
        registerDoubleCustom(plugin);
    }
}
