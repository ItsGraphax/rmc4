package de.itsgraphax.rmc4.tokens;

import de.itsgraphax.rmc4.Token;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@FunctionalInterface
public interface TokenDefinition {
    void trigger(JavaPlugin plugin, Player player, Token token);
}
