package de.itsgraphax.rmc4.commands.give_token;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import de.itsgraphax.rmc4.Token;
import de.itsgraphax.rmc4.enums.TokenIdentifier;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class GiveTokenCommand {
    public static void register(Commands commands, JavaPlugin plugin) {
        LiteralArgumentBuilder<CommandSourceStack> giveCustomCommand = Commands.literal("give-token").then(Commands.argument("token", new CustomTokenArgument()).executes(ctx -> executeSelf(ctx, plugin)));

        commands.register(giveCustomCommand.build());
    }

    private static int executeSelf(CommandContext<CommandSourceStack> ctx, JavaPlugin plugin) {
        if (!(ctx.getSource().getSender() instanceof Player player)) {
            ctx.getSource().getSender().sendMessage("Only players can use this command");
            return 0;
        }

        TokenIdentifier tokenId = ctx.getArgument("token", TokenIdentifier.class);

        ItemStack item = new Token(tokenId, false, 0).asItem(plugin);
        player.getInventory().addItem(item);

        player.sendMessage("Gave " + tokenId + " token");
        return Command.SINGLE_SUCCESS;
    }
}
