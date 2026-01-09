package de.itsgraphax.rmc4.listeners;

import de.itsgraphax.rmc4.Token;
import de.itsgraphax.rmc4.Utils;
import de.itsgraphax.rmc4.InteractionManager;
import de.itsgraphax.rmc4.enums.InteractionState;
import de.itsgraphax.rmc4.utils.Namespaces;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class JoinListener implements Listener {
    private final JavaPlugin plugin;

    public JoinListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private void initializePlayer(Player player) {
        // TODO: Move into helper func
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        Integer initializationVer = pdc.get(Namespaces.initializationVer(plugin), PersistentDataType.INTEGER);
        // Initialize player
        if (initializationVer == null || initializationVer != Utils.CURRENT_INITIALIZATION_VER) {

            pdc.set(Namespaces.initializationVer(plugin), PersistentDataType.INTEGER, Utils.CURRENT_INITIALIZATION_VER);

            InteractionManager.setPlayerInteractionState(plugin, player, InteractionState.NONE, null, null, null);

            Token.removeTokenFromPlayer(plugin, player, 0);
            Token.removeTokenFromPlayer(plugin, player, 1);
        }
        Utils.resetPlayerTitleTimes(player);
    }

    @EventHandler
    private void playerJoinListener(PlayerJoinEvent event) {
        initializePlayer(event.getPlayer());
        InteractionManager.setPlayerInteractionState(plugin, event.getPlayer(), InteractionState.NONE, null, null, null);
    }
}
