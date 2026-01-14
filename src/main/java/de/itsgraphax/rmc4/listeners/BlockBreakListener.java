package de.itsgraphax.rmc4.listeners;

import de.itsgraphax.rmc4.Token;
import de.itsgraphax.rmc4.Utils;
import de.itsgraphax.rmc4.enums.TokenIdentifier;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public class BlockBreakListener implements Listener {
    private final JavaPlugin plugin;

    public BlockBreakListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void BlockDropItemListener(BlockDropItemEvent event) {
        for (Token token : Token.getTokens(plugin, event.getPlayer())) {
            if (token.broken) continue;

            if (token.identifier == TokenIdentifier.DUPE) {
                Material blockType = event.getBlockState().getType();
                List<Material> affected = Arrays.asList(Utils.DUPE_TOKEN_AFFECT);

                Item first = event.getItems().getFirst();
                Material firstDropType = first.getItemStack().getType();

                if (affected.contains(blockType) && !affected.contains(firstDropType)) {
                    while (Math.random() < Utils.DUPE_TOKEN_CHANCES().get(token.level)) {
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
}
