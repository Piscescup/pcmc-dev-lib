package io.github.piscescup.fabricmc.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.piscescup.fabricmc.constants.MCLanguage;
import io.github.piscescup.fabricmc.utils.command.CmdSuggestionBuilder;
import io.github.piscescup.fabricmc.utils.command.CommandFeedback;
import io.github.piscescup.fabricmc.utils.command.CommandSuggestible;
import io.github.piscescup.interfaces.Builder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public final class TestCommands {
    public static final LiteralArgumentBuilder<CommandSourceStack> TEST = Commands.literal("get")
        .then(Commands.argument("input", StringArgumentType.word())
            .suggests(CmdSuggestionBuilder.createByCommandSourceStack()
                .suggest(MCLanguage.class)
                .suggest(1)
                .suggest(2)
                .suggest(131431074871401L)
                .suggest('c')
                .suggest(1 > 2)
                .suggest(5.0F)
                .suggest(new Student("PC", 23, "Jilin_China", "Chemistry"))
                .suggest(Math.sin(Math.PI / 2))
                .suggestArray(new char[]{ 'a', 'b', 'c', 'd', 'e', 'f' })
                .suggestArray(new String[]{"abc", "bcd", "cde", "def", "efg", "fgh"})
                .suggest(Object.class
                    .getModule()
                    .getPackages()
                    .stream()
                )
                .suggest(Builder.class
                    .getModule()
                    .getPackages()
                )
                .suggestDynamic(context -> {
                    CommandSourceStack source = context.getSource();
                    MinecraftServer server = source.getServer();
                    return server.getGameRules().availableRules().toList();
                })
                .build()
            )
            .executes(context -> {
                String lang = StringArgumentType.getString(context, "lang");
                CommandSourceStack source = context.getSource();
                CommandFeedback.literalOnBroadcast(context, Component.literal(lang));
                return Command.SINGLE_SUCCESS;
            })
        );


    private record Student(
        String name, int age, String address, String collage
    )
        implements CommandSuggestible
    {

        @Override
        public @NotNull String toSuggestionString() {
            return String.format(
                "%s-%d_years_old-collage_%s-at_%s",
                    name, age, collage, address
                );
        }
    }
}
