package de.itsgraphax.rmc4;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.UUID;

public final class Utils {
    private Utils(){}
    // Setting
    public static final Material CUSTOM_ITEM_MATERIAL = Material.POISONOUS_POTATO;
    public static final int CURRENT_INITIALIZATION_VER = 0;

    // Namespaces
        // Helpers
    private static NamespacedKey numberedNamespace(JavaPlugin plugin, String key, int number) {return new NamespacedKey(plugin, key + number);}
        // Items
            // UUID
    public static NamespacedKey itemUuidNamespace(JavaPlugin plugin) {return new NamespacedKey(plugin, "itemUuid");}
            // Token
    public static NamespacedKey customItemNamespace(JavaPlugin plugin) {return new NamespacedKey(plugin, "custom_item");}
    public static NamespacedKey tokenNamespace(JavaPlugin plugin) {return new NamespacedKey(plugin, "token");}
    public static NamespacedKey tokenBrokenNamespace(JavaPlugin plugin) {return new NamespacedKey(plugin, "tokenBroken");}
    public static NamespacedKey tokenLevelNamespace(JavaPlugin plugin) {return new NamespacedKey(plugin, "tokenLevel");}
    public static NamespacedKey blockInvNamespace(JavaPlugin plugin) {return new NamespacedKey(plugin, "blockInv");}
        // Players
            // Player Init
    public static NamespacedKey initializationVerNamespace(JavaPlugin plugin) {return new NamespacedKey(plugin, "initializationVer");}
            // Interaction State
    public static NamespacedKey interactionStateNamespace(JavaPlugin plugin) {return new NamespacedKey(plugin, "interactionState");}
    public static NamespacedKey interactionStateItemUuidNamespace(JavaPlugin plugin) {return new NamespacedKey(plugin, "interactionStateItemUuid");}
            // Token
    public static NamespacedKey equippedTokenNamespace(JavaPlugin plugin, int slot) {return numberedNamespace(plugin, "equippedToken", slot);}
    public static NamespacedKey equippedTokenLevelNamespace(JavaPlugin plugin, int slot) {return numberedNamespace(plugin, "equippedTokenLevel", slot);}


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
            pdc.set(customItemNamespace(plugin), PersistentDataType.STRING, materialString);
        });
        return item;
    }

    public static ItemStack getTokenItem(JavaPlugin plugin, TokenIdentifier identifier, boolean broken, int level) {
        String identifierString = identifier.toString();
        ItemStack item = getCustomItem(plugin, CustomItemMaterial.TOKEN);

        // update name translation key
        item.setData(DataComponentTypes.CUSTOM_NAME, Component.translatable("ruggimc.itemname." + CustomItemMaterial.TOKEN + "." + identifier));

        item.editPersistentDataContainer(pdc -> {
            // define which token it substitutes
            pdc.set(tokenNamespace(plugin), PersistentDataType.STRING, identifierString);

            // set values
            pdc.set(tokenBrokenNamespace(plugin), PersistentDataType.BOOLEAN, broken);
            pdc.set(tokenLevelNamespace(plugin), PersistentDataType.INTEGER, level);
        });

        // update custom model data
        item.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData().addString(identifierString).build());

        return item;
    }

        // Modification
    public static void setBlockInv(JavaPlugin plugin, ItemStack item, boolean value) {
        item.editPersistentDataContainer(pdc -> {
            pdc.set(blockInvNamespace(plugin), PersistentDataType.BOOLEAN, value);
        });
    }

    public static void generateUuidForItem(JavaPlugin plugin, ItemStack item) {
        if (item.getPersistentDataContainer().get(itemUuidNamespace(plugin), PersistentDataType.STRING) == null) {
            item.editPersistentDataContainer(pdc -> {
                pdc.set(itemUuidNamespace(plugin), PersistentDataType.STRING, UUID.randomUUID().toString());
            });
        };
    }

        // Checking
    public static boolean isCustomMaterial(JavaPlugin plugin, ItemStack item, CustomItemMaterial material) {
        if (item == null) {
            return false;
        }
        String stored = item.getPersistentDataContainer().get(customItemNamespace(plugin), PersistentDataType.STRING);
        return (item.getType() == CUSTOM_ITEM_MATERIAL) && (Objects.equals(stored, material.toString()));
    }

    // InteractionState
        // Player Modification
    public static void setPlayerInteractionState(JavaPlugin plugin, Player player, InteractionState state) {
        ItemStack mainHandItem = player.getInventory().getItemInMainHand();

        generateUuidForItem(plugin, mainHandItem);

        String itemUuid = mainHandItem.getPersistentDataContainer()
                .get(itemUuidNamespace(plugin), PersistentDataType.STRING);

        // Set Interaction State
        player.getPersistentDataContainer()
                .set(interactionStateNamespace(plugin), PersistentDataType.INTEGER, state.getId());

        // Set Interaction State Item
        if (itemUuid != null) {
            player.getPersistentDataContainer()
                    .set(interactionStateItemUuidNamespace(plugin), PersistentDataType.STRING, itemUuid);
        } else {
            player.getPersistentDataContainer()
                    .remove(interactionStateItemUuidNamespace(plugin));
        }
    }

        // Resetting
    public static boolean resetInteractionStateIfItemChanged(JavaPlugin plugin, Player player) {
        ItemStack hand = player.getInventory().getItemInMainHand();
        String handUuid = hand.getPersistentDataContainer().get(itemUuidNamespace(plugin), PersistentDataType.STRING);
        String storedUuid = player.getPersistentDataContainer().get(interactionStateItemUuidNamespace(plugin), PersistentDataType.STRING);

        if (!Objects.equals(handUuid, storedUuid)) {
            setPlayerInteractionState(plugin, player, InteractionState.NONE);
            return true;
        }
        return false;
    }

        // Checking
    public static boolean checkPlayerInteractionState(JavaPlugin plugin, Player player, InteractionState state) {
        Integer stored = player.getPersistentDataContainer().get(interactionStateNamespace(plugin), PersistentDataType.INTEGER);
        return Objects.equals(stored, state.getId());
    }


    // Enums
    public enum CustomItemMaterial {
        DIAMOND_SHARD("diamond_shard"), TOKEN_CORE("core"), TOKEN("token"), DUPE_TOKEN_CORE("dupe_token_core");

        private final String text;

        CustomItemMaterial(final String text) {
            this.text = text;
        }


        public static CustomItemMaterial fromId(String input) {
            for (CustomItemMaterial v : values()) {
                if (v.text.equalsIgnoreCase(input)) {
                    return v;
                }
            }
            throw new IllegalArgumentException("Unknown custom item id:" + input);
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum TokenIdentifier {
        DUPE("dupe");

        private final String text;

        TokenIdentifier(final String text) {
            this.text = text;
        }


        public static TokenIdentifier fromId(String input) {
            for (TokenIdentifier v : values()) {
                if (v.text.equalsIgnoreCase(input)) {
                    return v;
                }
            }
            throw new IllegalArgumentException("Unknown token id:" + input);
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum InteractionState {
        NONE(0),
        SELECTING_EQUIP_SLOT(1);

        private final int id;

        InteractionState(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

}
