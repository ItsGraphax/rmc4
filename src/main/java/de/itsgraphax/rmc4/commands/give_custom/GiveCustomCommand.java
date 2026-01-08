package de.itsgraphax.rmc4.commands.give_custom;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import de.itsgraphax.rmc4.Utils;
import enums.CustomItemMaterial;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class GiveCustomCommand {
    public static void register(Commands commands, JavaPlugin plugin) {
        LiteralArgumentBuilder<CommandSourceStack> give_custom_command = Commands.literal("give-custom").then(Commands.argument("item", new CustomItemArgument()).executes(ctx -> executeSelf(ctx, plugin)));

        commands.register(give_custom_command.build());

        plugin.getComponentLogger().info("givecustom registered");
    }

    private static int executeSelf(CommandContext<CommandSourceStack> ctx, JavaPlugin plugin) {
        if (!(ctx.getSource().getSender() instanceof Player player)) {
            ctx.getSource().getSender().sendMessage("Only players can use this command");
            return 0;
        }

        CustomItemMaterial itemId = ctx.getArgument("item", CustomItemMaterial.class);

        player.getInventory().addItem(Utils.getCustomItem(plugin, itemId));

        player.sendMessage("Gave " + itemId);
        return Command.SINGLE_SUCCESS;
    }
}
