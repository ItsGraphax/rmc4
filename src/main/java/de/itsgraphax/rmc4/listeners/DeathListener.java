package de.itsgraphax.rmc4.listeners;

import de.itsgraphax.rmc4.Token;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class DeathListener implements Listener {
    private final JavaPlugin plugin;

    public DeathListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private void checkTokenSlot(PlayerDeathEvent event, int slot) {
        Token token = Token.fromPlayer(plugin, event.getPlayer(), slot);

        if (token.fromDefault) {
            return;
        }

        Token.removeTokenFromPlayer(plugin, event.getPlayer(), slot);

        token.broken = true;
        ItemStack item = token.asItem(plugin);

        event.getDrops().add(item);
    }

    @EventHandler
    private void playerDeathListener(PlayerDeathEvent event) {
        checkTokenSlot(event, 0);
        checkTokenSlot(event, 1);
    }
}
