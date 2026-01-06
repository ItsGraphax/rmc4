package de.itsgraphax.rmc4.listeners;

import de.itsgraphax.rmc4.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class JoinListener implements Listener {
    private final JavaPlugin plugin;

    public JoinListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void playerJoinListener(PlayerJoinEvent event) {
        // Initialize player
        if (event.getPlayer().getPersistentDataContainer().get(Utils.initializationVerNamespace(plugin), PersistentDataType.INTEGER) != Utils.CURRENT_INITIALIZATION_VER) {

            event.getPlayer().getPersistentDataContainer()
                    .set(Utils.initializationVerNamespace(plugin), PersistentDataType.INTEGER, Utils.CURRENT_INITIALIZATION_VER);

            event.getPlayer().getPersistentDataContainer()
                    .set(Utils.interactionStateNamespace(plugin), PersistentDataType.INTEGER, Utils.InteractionState.NONE.getId());

        }
    }
}
