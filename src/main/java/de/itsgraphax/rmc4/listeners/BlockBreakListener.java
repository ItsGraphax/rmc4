package de.itsgraphax.rmc4.listeners;

import de.itsgraphax.rmc4.Token;
import de.itsgraphax.rmc4.enums.TokenIdentifier;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class BlockBreakListener implements Listener {
    private final JavaPlugin plugin;

    public BlockBreakListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void BlockDropItemListener(BlockDropItemEvent event) {
        Token dupeToken = Token.hasToken(plugin, event.getPlayer(), TokenIdentifier.DUPE);

        if (dupeToken != null && !dupeToken.broken) {
            Material blockType = event.getBlockState().getType();
            List<String> affected = plugin.getConfig().getStringList("token_config.dupe.affects");

            Item first = event.getItems().getFirst();
            Material firstDropType = first.getItemStack().getType();

            // the mined block is contained in affected, but the dropped item does not (to disable "real" duping)
            if (affected.contains(blockType.name()) &&
                    (!affected.contains(firstDropType.name()) || firstDropType == Material.ANCIENT_DEBRIS)
            ) {
                while (Math.random() < plugin.getConfig().getInt("token_levels.dupe.trigger_perc." + dupeToken.level)) {
                    if (blockType == Material.ANCIENT_DEBRIS) {
                        ItemStack scrap = new ItemStack(Material.NETHERITE_SCRAP, 1);
                        event.getItems().add(event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), scrap));
                    } else {
                        ItemStack copy = first.getItemStack().clone();
                        copy.setAmount(1);
                        event.getItems().add(event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), copy));
                    }
                }
            }
        }
    }
}
