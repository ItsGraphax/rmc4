package de.itsgraphax.rmc4;

import de.itsgraphax.rmc4.enums.CustomItemMaterial;
import de.itsgraphax.rmc4.utils.Namespaces;
import de.itsgraphax.rmc4.enums.ResourcePackLetter;
import de.itsgraphax.rmc4.enums.TokenIdentifier;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.persistence.PersistentDataContainerView;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Token {
    public TokenIdentifier identifier;
    public boolean broken;
    public int level;

    public boolean fromDefault = false;

    public Token(TokenIdentifier identifier, boolean broken, int level) {
        this.identifier = identifier;
        this.broken = broken;
        this.level = level;
    }


    public ItemStack asItem(JavaPlugin plugin) {
        ItemStack item = Utils.getCustomItem(plugin, CustomItemMaterial.TOKEN);

        // update name translation key
        Component customName = Component
                .translatable("ruggimc.itemname." + CustomItemMaterial.TOKEN + "." + identifier.toString());
        if (broken) {
            customName = customName.append(Component.text(" [BROKEN]"));
        }

        item.setData(DataComponentTypes.CUSTOM_NAME, customName);

        // update lore
        Component lore = Component.text("Level " + level + 1);

        item.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(lore)));

        // set stack size
        item.setData(DataComponentTypes.MAX_STACK_SIZE, 1);

        // block item from inventory
        Utils.setBlockInv(plugin, item, true);

        item.editPersistentDataContainer(pdc -> {
            // define which token it substitutes
            pdc.set(Namespaces.token(plugin), PersistentDataType.STRING, identifier.toString());

            // set values
            pdc.set(Namespaces.tokenBroken(plugin), PersistentDataType.BOOLEAN, broken);
            pdc.set(Namespaces.tokenLevel(plugin), PersistentDataType.INTEGER, level);
        });

        // update custom model data
        item.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData().addString(identifier.toString()).addFlag(broken).build());

        return item;
    }

    public static Token fromItem(JavaPlugin plugin, @Nullable ItemStack item) {
        if (item == null) {
            item = ItemStack.empty();
        }
        PersistentDataContainerView pdcv = item.getPersistentDataContainer();

        TokenIdentifier newIdentifier = TokenIdentifier.fromId(pdcv.get(Namespaces.token(plugin), PersistentDataType.STRING));

        Boolean newBroken = pdcv.get(Namespaces.tokenBroken(plugin), PersistentDataType.BOOLEAN);
        if (newBroken == null) {
            newBroken = false;
        }

        Integer newLevel = pdcv.get(Namespaces.tokenLevel(plugin), PersistentDataType.INTEGER);
        if (newLevel == null) {
            newLevel = 0;
        }

        Token newToken = new Token(newIdentifier, newBroken, newLevel);

        if (newIdentifier == TokenIdentifier.UNKNOWN) {
            newToken.fromDefault = true;
        }

        return newToken;
    }

    public void intoPlayer(JavaPlugin plugin, Player player, int slot) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();

        pdc.set(Namespaces.equippedToken(plugin, slot), PersistentDataType.STRING, identifier.toString());
        pdc.set(Namespaces.equippedTokenBroken(plugin, slot), PersistentDataType.BOOLEAN, broken);
        pdc.set(Namespaces.equippedTokenLevel(plugin, slot), PersistentDataType.INTEGER, level);
    }

    public static Token fromPlayer(JavaPlugin plugin, Player player, int slot) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();

        TokenIdentifier newIdentifier = TokenIdentifier.fromId(pdc.get(Namespaces.equippedToken(plugin, slot), PersistentDataType.STRING));

        Boolean newBroken = pdc.get(Namespaces.equippedTokenBroken(plugin, slot), PersistentDataType.BOOLEAN);
        if (newBroken == null) {
            newBroken = false;
        }

        Integer newLevel = pdc.get(Namespaces.equippedTokenLevel(plugin, slot), PersistentDataType.INTEGER);
        if (newLevel == null) {
            newLevel = 0;
        }

        Token newToken = new Token(newIdentifier, newBroken, newLevel);

        if (newIdentifier == TokenIdentifier.UNKNOWN) {
            newToken.fromDefault = true;
        }
        return newToken;
    }


    public static void removeTokenFromPlayer(JavaPlugin plugin, Player player, int slot) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();

        pdc.remove(Namespaces.equippedToken(plugin, slot));
        pdc.remove(Namespaces.equippedTokenBroken(plugin, slot));
        pdc.remove(Namespaces.equippedTokenLevel(plugin, slot));
    }


    public String toLetter() {
        return switch (identifier) {
            case DUPE -> (broken ? ResourcePackLetter.DUPE_TOKEN_BROKEN : ResourcePackLetter.DUPE_TOKEN).toString();
            default -> ResourcePackLetter.EMPTY_TOKEN.toString();
        };
    }

    /**
     * Returns the amount of a specific token equipped
     */
    public static Token[] getTokens(JavaPlugin plugin, Player player) {
        return new Token[]{
                Token.fromPlayer(plugin, player, 0),
                Token.fromPlayer(plugin, player, 1)
        };
    }
}
