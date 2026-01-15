package de.itsgraphax.rmc4.utils;

import de.itsgraphax.rmc4.enums.CustomItemMaterial;
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
import java.util.Objects;
import java.util.UUID;

public final class Utils {
    private Utils(){}
    // Setting
    public static final Material CUSTOM_ITEM_MATERIAL = Material.POISONOUS_POTATO;
    public static final int CURRENT_INITIALIZATION_VER = 2;
    public static final int DEFAULT_TIMEOUT_SHORT_S = 1;
    public static final int DEFAULT_TIMEOUT_NORMAL_S = 5;

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
