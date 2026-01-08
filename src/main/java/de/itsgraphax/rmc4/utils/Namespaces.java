package de.itsgraphax.rmc4.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public class Namespaces {
    // Namespaces
        // Helpers
    private static NamespacedKey numberedNamespace(JavaPlugin plugin, String key, int number) {return new NamespacedKey(plugin, key + number);}

    // Items
        // UUID
    public static NamespacedKey itemUuid(JavaPlugin plugin) {return new NamespacedKey(plugin, "itemUuid");}

        // Token
    public static NamespacedKey customItem(JavaPlugin plugin) {return new NamespacedKey(plugin, "custom_item");}

    public static NamespacedKey token(JavaPlugin plugin) {return new NamespacedKey(plugin, "token");}

    public static NamespacedKey tokenBroken(JavaPlugin plugin) {return new NamespacedKey(plugin, "tokenBroken");}

    public static NamespacedKey tokenLevel(JavaPlugin plugin) {return new NamespacedKey(plugin, "tokenLevel");}

    public static NamespacedKey blockInv(JavaPlugin plugin) {return new NamespacedKey(plugin, "blockInv");}

    // Players
        // Player Init
    public static NamespacedKey initializationVer(JavaPlugin plugin) {return new NamespacedKey(plugin, "initializationVer");}

        // Interaction State
    public static NamespacedKey interactionState(JavaPlugin plugin) {return new NamespacedKey(plugin, "interactionState");}

    public static NamespacedKey interactionStateItemUuid(JavaPlugin plugin) {return new NamespacedKey(plugin, "interactionStateItemUuid");}

    public static NamespacedKey interactionStateTimestamp(JavaPlugin plugin) {return new NamespacedKey(plugin, "interactionStateTimestamp");}

    public static NamespacedKey interactionStateHand(JavaPlugin plugin) {return new NamespacedKey(plugin, "interactionStateHand");}

    public static NamespacedKey interactionStateTimeout(JavaPlugin plugin) {return new NamespacedKey(plugin, "interactionStateTimeout");}

        // Token
    public static NamespacedKey equippedToken(JavaPlugin plugin, int slot) {return numberedNamespace(plugin, "equippedToken", slot);}

    public static NamespacedKey equippedTokenBroken(JavaPlugin plugin, int slot) {return numberedNamespace(plugin, "equippedTokenBroken", slot);}

    public static NamespacedKey equippedTokenLevel(JavaPlugin plugin, int slot) {return numberedNamespace(plugin, "equippedTokenLevel", slot);}
}
