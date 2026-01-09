package de.itsgraphax.rmc4;

import de.itsgraphax.rmc4.enums.CustomItemMaterial;
import de.itsgraphax.rmc4.utils.Namespaces;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public final class Utils {
    private Utils(){}
    // Setting
    public static final Material CUSTOM_ITEM_MATERIAL = Material.POISONOUS_POTATO;
    public static final int CURRENT_INITIALIZATION_VER = 2;
    public static final int DEFAULT_TIMEOUT_SHORT_S = 1;
    public static final int DEFAULT_TIMEOUT_NORMAL_S = 5;
    public static final char LIST_DELIMITER = '£';

    public static final Material[] DUPE_TOKEN_AFFECT = {
            Material.COAL_ORE,
            Material.DEEPSLATE_COAL_ORE,
            Material.COPPER_ORE,
            Material.DEEPSLATE_COPPER_ORE,
            Material.IRON_ORE,
            Material.DEEPSLATE_IRON_ORE,
            Material.GOLD_ORE,
            Material.DEEPSLATE_GOLD_ORE,
            Material.DIAMOND_ORE,
            Material.DEEPSLATE_DIAMOND_ORE,
            Material.LAPIS_ORE,
            Material.DEEPSLATE_LAPIS_ORE,
            Material.EMERALD_ORE,
            Material.DEEPSLATE_EMERALD_ORE,
            Material.REDSTONE_ORE,
            Material.DEEPSLATE_REDSTONE_ORE,
            Material.NETHER_GOLD_ORE,
            Material.NETHER_QUARTZ_ORE,
            Material.ANCIENT_DEBRIS
    };
    public static HashMap<Integer, Double> DUPE_TOKEN_CHANCES() {
        HashMap<Integer, Double> map = new HashMap<>();

        map.put(0, 0.1);
        map.put(1, 0.2);
        map.put(2, 0.3);

        return map;
    }

    // Items
        // ItemStack generation
    public static ItemStack getCustomItem(JavaPlugin plugin, CustomItemMaterial material) {
        String materialString = material.toString();
        ItemStack item = ItemStack.of(CUSTOM_ITEM_MATERIAL);
        // set model
        item.setData(DataComponentTypes.ITEM_MODEL, Key.key("ruggimc:" + materialString));
        // set name from translation
        item.setData(DataComponentTypes.CUSTOM_NAME, Component.translatable("ruggimc.itemname." + materialString));
        // remove poisonous potato properties
        item.unsetData(DataComponentTypes.CONSUMABLE);

        item.editPersistentDataContainer(pdc -> {
            // define custom item type
            pdc.set(Namespaces.customItem(plugin), PersistentDataType.STRING, materialString);
        });
        return item;
    }

        // Modification
    public static void setBlockInv(JavaPlugin plugin, ItemStack item, boolean value) {
        item.editPersistentDataContainer(pdc -> {
            pdc.set(Namespaces.blockInv(plugin), PersistentDataType.BOOLEAN, value);
        });
    }

    public static void generateUuidForItem(JavaPlugin plugin, @Nullable ItemStack item) {
        if (item == null) {
            return;
        }

        if (item.getPersistentDataContainer().get(Namespaces.itemUuid(plugin), PersistentDataType.STRING) == null) {
            item.editPersistentDataContainer(pdc -> {
                pdc.set(Namespaces.itemUuid(plugin), PersistentDataType.STRING, UUID.randomUUID().toString());
            });
        }
    }

        // Checking
    public static boolean isCustomMaterial(JavaPlugin plugin, ItemStack item, CustomItemMaterial material) {
        if (item == null) {
            return false;
        }
        String stored = item.getPersistentDataContainer().get(Namespaces.customItem(plugin), PersistentDataType.STRING);
        return (item.getType() == CUSTOM_ITEM_MATERIAL) && (Objects.equals(stored, material.toString()));
    }


    public static void resetPlayerTitleTimes(Player player) {
        player.sendTitlePart(TitlePart.TIMES, Title.Times.times(Duration.ZERO,
                Duration.ofSeconds(3),
                Duration.ofSeconds(1)));
    }
}
