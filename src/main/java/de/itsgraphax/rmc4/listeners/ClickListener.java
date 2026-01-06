package de.itsgraphax.rmc4.listeners;

import de.itsgraphax.rmc4.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ClickListener implements Listener {
    private final JavaPlugin plugin;

    public ClickListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private void tokenEquipCheck(PlayerInteractEvent event) {
        ItemStack item = event.getItem();

        if (!Utils.isCustomMaterial(plugin, event.getItem(), Utils.CustomItemMaterial.TOKEN)) {
            return;
        }
    }

    @EventHandler
    public void playerInteractListener(PlayerInteractEvent event) {
        tokenEquipCheck(event);
    }
}
