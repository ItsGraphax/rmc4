package de.itsgraphax.rmc4.tokens;

import de.itsgraphax.rmc4.Token;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Dash {
    private static void launchForward(Player player, double strength, double yBoost) {
        Vector dir = player.getLocation().getDirection().normalize();
        dir.multiply(strength);
        dir.setY(dir.getY() + yBoost); // so one dosent get stuck to a 1block wall
        player.setVelocity(dir);
    }

    public static void trigger(JavaPlugin plugin, Player player, Token token) {
        launchForward(player, plugin.getConfig().getDouble("token_levels.dash.strength." + token.level), 1);
    }
}
