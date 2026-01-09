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
            // Dupe Token
            if (token.identifier == TokenIdentifier.DUPE && !token.broken) {
                // Check if the block is affected
                Material blockType = event.getBlockState().getType();
                List<Material> tmp = Arrays.asList(Utils.DUPE_TOKEN_AFFECT);

                if (tmp.contains(blockType) && !tmp.contains(event.getItems().getFirst().getItemStack().getType())) {
                    boolean random = true;
                    while (random) {
                        random = Math.random() < Utils.DUPE_TOKEN_CHANCES().get(token.level);

                        if (random) {
                            // Prevent ancient debris abuse
                            if (blockType == Material.ANCIENT_DEBRIS) {
                                Item item = event.getItems().getFirst();
                                item.setItemStack(ItemStack.of(Material.NETHERITE_SCRAP));
                                event.getItems().add(item);
                            } else {
                                event.getItems().add(event.getItems().getFirst());
                            }
                        }
                    }
                }
            }
        }
    }
}
