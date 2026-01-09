package de.itsgraphax.rmc4.recipes;

import de.itsgraphax.rmc4.Token;
import de.itsgraphax.rmc4.Utils;
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
        ItemStack diamondShardRecipeItem = Utils.getCustomItem(plugin, CustomItemMaterial.DIAMOND_SHARD);

        ShapedRecipe diamondShardRecipe = new ShapedRecipe(new NamespacedKey(plugin, CustomItemMaterial.DIAMOND_SHARD.toString()), diamondShardRecipeItem);
        diamondShardRecipe.shape("pdp", "dad", "pdp");
        diamondShardRecipe.setIngredient('d', Material.DIAMOND);
        diamondShardRecipe.setIngredient('a', Material.AMETHYST_SHARD);
        diamondShardRecipe.setIngredient('p', Material.PRISMARINE_SHARD);
        plugin.getServer().addRecipe(diamondShardRecipe);
    }

    private static void registerCore(JavaPlugin plugin) {
        ItemStack coreRecipeItem = Utils.getCustomItem(plugin, CustomItemMaterial.TOKEN_CORE);

        ShapedRecipe coreRecipe = new ShapedRecipe(new NamespacedKey(plugin, CustomItemMaterial.TOKEN_CORE.toString()), coreRecipeItem);
        coreRecipe.shape(" i ", "pdp", " i ");
        coreRecipe.setIngredient('i', Material.IRON_BLOCK);
        coreRecipe.setIngredient('p', Material.PHANTOM_MEMBRANE);
        coreRecipe.setIngredient('d', Utils.CUSTOM_ITEM_MATERIAL); // diamond shard
        plugin.getServer().addRecipe(coreRecipe);
    }

    private static void registerDupeToken(JavaPlugin plugin) {
        ItemStack dupe_token_recipe_item = new Token(TokenIdentifier.DUPE, false, 0).asItem(plugin);
        Utils.setBlockInv(plugin, dupe_token_recipe_item, true);

        ShapedRecipe dupe_token_recipe = new ShapedRecipe(new NamespacedKey(plugin, CustomItemMaterial.TOKEN.toString()), dupe_token_recipe_item);
        dupe_token_recipe.shape("idi", "kck", "bxb");
        dupe_token_recipe.setIngredient('i', Material.IRON_BLOCK);
        dupe_token_recipe.setIngredient('d', Utils.CUSTOM_ITEM_MATERIAL); // dupe token core
        dupe_token_recipe.setIngredient('k', Material.COAL);
        dupe_token_recipe.setIngredient('b', Material.DIAMOND);
        dupe_token_recipe.setIngredient('x', Material.DIAMOND_BLOCK);
        dupe_token_recipe.setIngredient('c', Utils.CUSTOM_ITEM_MATERIAL); // token core
        plugin.getServer().addRecipe(dupe_token_recipe);
    }

    private static void registerTokenRepairer(JavaPlugin plugin) {
        ItemStack repairerRecipeItem = Utils.getCustomItem(plugin, CustomItemMaterial.TOKEN_REPAIRER);

        ShapedRecipe repairerRecipe = new ShapedRecipe(new NamespacedKey(plugin, CustomItemMaterial.TOKEN_REPAIRER.toString()), repairerRecipeItem);
        repairerRecipe.shape("iii", "idp", "iii");
        repairerRecipe.setIngredient('i', Material.IRON_INGOT);
        repairerRecipe.setIngredient('d', Utils.CUSTOM_ITEM_MATERIAL); // diamond shard
        plugin.getServer().addRecipe(repairerRecipe);
    }

    private static void registerDoubleCustom(JavaPlugin plugin) {
        ItemStack doubleCustomRecipeItem = ItemStack.of(Material.STONE);
        Utils.setBlockInv(plugin, doubleCustomRecipeItem, true);

        ShapelessRecipe doubleCustomRecipe = new ShapelessRecipe(new NamespacedKey(plugin, "doubleCustom"), doubleCustomRecipeItem);
        doubleCustomRecipe.addIngredient(Utils.CUSTOM_ITEM_MATERIAL);
        doubleCustomRecipe.addIngredient(Utils.CUSTOM_ITEM_MATERIAL);
        plugin.getServer().addRecipe(doubleCustomRecipe);
    }

    public static void registerAll(JavaPlugin plugin) {
        registerDiamondShard(plugin);
        registerCore(plugin);
        registerDupeToken(plugin);
        registerTokenRepairer(plugin);
        registerDoubleCustom(plugin);
    }
}
