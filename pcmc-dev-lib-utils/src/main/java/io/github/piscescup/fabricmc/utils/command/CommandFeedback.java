package io.github.piscescup.fabricmc.utils.command;

import com.mojang.brigadier.context.CommandContext;
import io.github.piscescup.util.validation.NullCheck;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

/**
 * Provides utility methods for sending localized feedback messages from a
 * command source.
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public final class CommandFeedback {
    private CommandFeedback() {}

    /**
     * Sends a success message to the command source and requests that it be
     * broadcast to administrators.
     *
     * @param context the command context
     * @param message the message to send
     * @throws NullPointerException if {@code context} is {@code null}
     */
    public static void literalOnBroadcast(
        final @NotNull CommandContext<CommandSourceStack> context,
        final Component message
    ) {
        NullCheck.requireNonNull(context, "context");
        context.getSource().sendSuccess(() -> message, true);
    }

    /**
     * Sends a success message to the command source without requesting
     * an administrator broadcast.
     *
     * @param context the command context
     * @param message the message to send
     * @throws NullPointerException if {@code context} is {@code null}
     */
    public static void literalWithoutBroadcast(
        final @NotNull CommandContext<CommandSourceStack> context,
        final Component message
    ) {
        NullCheck.requireNonNull(context, "context");
        context.getSource().sendSuccess(() -> message, false);
    }

    /**
     * Sends a successful command feedback message and requests that it also
     * be broadcast to eligible administrative recipients.
     *
     * @param context the context of the executed command
     * @param key     the translation key of the message
     * @param args    the arguments used by the translatable component
     *
     * @throws NullPointerException if {@code context} is {@code null}
     */
    public static void successOnBroadcast(
        final @NotNull CommandContext<CommandSourceStack> context,
        final String key,
        final Object... args
    ) {
        NullCheck.requireNonNull(context, "context");
        context.getSource().sendSuccess(
            () -> Component.translatable(key, args), true
        );
    }

    /**
     * Sends a successful command feedback message without broadcasting it to
     * administrative recipients.
     *
     * @param context the context of the executed command
     * @param key     the translation key of the message
     * @param args    the arguments used by the translatable component
     *
     * @throws NullPointerException if {@code context} is {@code null}
     */
    public static void successWithoutBroadcast(
        final @NotNull CommandContext<CommandSourceStack> context,
        final String key,
        final Object... args
    ) {
        NullCheck.requireNonNull(context, "context");
        context.getSource().sendSuccess(
            () -> Component.translatable(key, args), false
        );
    }

    /**
     * Sends a command failure message to the command source.
     *
     * @param context the context of the executed command
     * @param key     the translation key of the message
     * @param args    the arguments used by the translatable component
     *
     * @throws NullPointerException if {@code context} is {@code null}
     */
    public static void failure(
        final @NotNull CommandContext<CommandSourceStack> context,
        final String key,
        final Object... args
    ) {
        NullCheck.requireNonNull(context, "context");
        context.getSource().sendFailure(
            Component.translatable(key, args)
        );
    }

    /**
     * Sends a localized system message directly to the command source.
     *
     * @param context the context of the executed command
     * @param key     the translation key of the message
     * @param args    the arguments used by the translatable component
     *
     * @throws NullPointerException if {@code context} is {@code null}
     */
    public static void systemMessage(
        final @NotNull CommandContext<CommandSourceStack> context,
        final String key,
        final Object... args
    ) {
        NullCheck.requireNonNull(context, "context");
        context.getSource().sendSystemMessage(
            Component.translatable(key, args)
        );
    }
}
