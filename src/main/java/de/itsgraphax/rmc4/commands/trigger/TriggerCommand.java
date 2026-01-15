package de.itsgraphax.rmc4.commands.trigger;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import de.itsgraphax.rmc4.Token;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TriggerCommand {
    public static void register(Commands commands, JavaPlugin plugin) {
        LiteralArgumentBuilder<CommandSourceStack> giveCustomCommand = Commands.literal("trigger")
                .then(Commands.literal("left")
                        .executes(ctx -> executeSelf(ctx, plugin, 0)))
                .then(Commands.literal("right")
                        .executes(ctx -> executeSelf(ctx, plugin, 1)));

        commands.register(giveCustomCommand.build());
    }

    private static int executeSelf(CommandContext<CommandSourceStack> ctx, JavaPlugin plugin, int slot) {
        if (!(ctx.getSource().getSender() instanceof Player player)) {
            ctx.getSource().getSender().sendMessage("Only players can use this command");
            return 0;
        }

        Token.trigger(plugin, player, slot);

        return Command.SINGLE_SUCCESS;
    }
}
