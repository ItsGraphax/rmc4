package de.itsgraphax.rmc4.commands.give_token;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import de.itsgraphax.rmc4.Utils;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public class CustomTokenArgument implements CustomArgumentType.Converted<Utils.TokenIdentifier, String> {

    private static final DynamicCommandExceptionType ERROR_INVALID_NAME = new DynamicCommandExceptionType(item_id -> {
        return MessageComponentSerializer.message().serialize(Component.text(item_id + " is not a valid item!"));
    });

    @Override
    public Utils.TokenIdentifier convert(String nativeType) throws CommandSyntaxException {
        try {
            return Utils.TokenIdentifier.fromId(nativeType);
        } catch (IllegalArgumentException ignored) {
            throw ERROR_INVALID_NAME.create(nativeType);
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (Utils.TokenIdentifier tokenId : Utils.TokenIdentifier.values()) {
            String name = tokenId.toString();

            if (name.startsWith(builder.getRemainingLowerCase())) {
                builder.suggest(tokenId.toString());
            }
        }

        return builder.buildFuture();
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }
}