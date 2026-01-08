package de.itsgraphax.rmc4;

import enums.CustomItemMaterial;
import de.itsgraphax.rmc4.utils.Namespaces;
import enums.TokenIdentifier;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.persistence.PersistentDataContainerView;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

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
        item.setData(DataComponentTypes.CUSTOM_NAME, Component.translatable("ruggimc.itemname." + CustomItemMaterial.TOKEN + "." + identifier.toString()));

        // set stack size
        item.setData(DataComponentTypes.MAX_STACK_SIZE, 1);

        item.editPersistentDataContainer(pdc -> {
            // define which token it substitutes
            pdc.set(Namespaces.token(plugin), PersistentDataType.STRING, identifier.toString());

            // set values
            pdc.set(Namespaces.tokenBroken(plugin), PersistentDataType.BOOLEAN, broken);
            pdc.set(Namespaces.tokenLevel(plugin), PersistentDataType.INTEGER, level);
        });

        // update custom model data
        item.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData().addString(identifier.toString()).build());

        return item;
    }

    public static Token fromItem(JavaPlugin plugin, ItemStack item) {
        PersistentDataContainerView pdcv = item.getPersistentDataContainer();

        TokenIdentifier newIdentifier = TokenIdentifier.fromId(pdcv.get(Namespaces.token(plugin), PersistentDataType.STRING));

        Boolean newBroken = pdcv.get(Namespaces.tokenBroken(plugin), PersistentDataType.BOOLEAN);
        if (newBroken == null) {newBroken = false;}

        Integer newLevel = pdcv.get(Namespaces.tokenLevel(plugin), PersistentDataType.INTEGER);
        if (newLevel == null) {newLevel = 0;}

        Token newToken = new Token(newIdentifier, newBroken, newLevel);

        if (newIdentifier == TokenIdentifier.UNKNOWN) {newToken.fromDefault = true;}

        return newToken;
    }

    public void intoPlayer(JavaPlugin plugin, Player player, int slot) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();

        pdc.set(Namespaces.equippedToken(plugin, slot), PersistentDataType.STRING, identifier.toString());
        pdc.set(Namespaces.equippedTokenLevel(plugin, slot), PersistentDataType.BOOLEAN, broken);
        pdc.set(Namespaces.equippedTokenLevel(plugin, slot), PersistentDataType.INTEGER, level);
    }

    public static Token fromPlayer(JavaPlugin plugin, Player player, int slot) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();

        TokenIdentifier newIdentifier = TokenIdentifier.fromId(pdc.get(Namespaces.equippedToken(plugin, slot), PersistentDataType.STRING));

        Boolean newBroken = pdc.get(Namespaces.equippedTokenBroken(plugin, slot), PersistentDataType.BOOLEAN);
        if (newBroken == null) {newBroken = false;}

        Integer newLevel = pdc.get(Namespaces.equippedTokenLevel(plugin, slot), PersistentDataType.INTEGER);
        if (newLevel == null) {newLevel = 0;}

        Token newToken = new Token(newIdentifier, newBroken, newLevel);

        if (newIdentifier == TokenIdentifier.UNKNOWN) {newToken.fromDefault = true;}
        return newToken;
    }

    public static void removeTokenFromPlayer(JavaPlugin plugin, Player player, int slot) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();

        pdc.remove(Namespaces.equippedToken(plugin, slot));
        pdc.remove(Namespaces.equippedTokenBroken(plugin, slot));
        pdc.remove(Namespaces.equippedTokenLevel(plugin, slot));
    }
}
