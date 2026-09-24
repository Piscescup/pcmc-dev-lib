package io.github.piscescup.fabricmc.utils.command;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.piscescup.util.validation.NullCheck;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see CmdSuggestionBuilder
 * @deprecated Use {@link CmdSuggestionBuilder} instead.
 */
@Deprecated
public final class CommandSuggestionUtils {
    private CommandSuggestionUtils() {}

    public static CompletableFuture<Suggestions> suggestionFrom(
        final @NotNull SuggestionsBuilder builder,
        final byte num
    ) {
        NullCheck.requireNonNull(builder, "builder");

        builder.suggest(String.valueOf(num));

        return builder.buildFuture();
    }

    public static CompletableFuture<Suggestions> suggestionFrom(
        final @NotNull SuggestionsBuilder builder,
        final short num
    ) {
        NullCheck.requireNonNull(builder, "builder");

        builder.suggest(String.valueOf(num));

        return builder.buildFuture();
    }

    public static CompletableFuture<Suggestions> suggestionFrom(
        final @NotNull SuggestionsBuilder builder,
        final int num
    ) {
        NullCheck.requireNonNull(builder, "builder");

        builder.suggest(String.valueOf(num));

        return builder.buildFuture();
    }

    public static CompletableFuture<Suggestions> suggestionFrom(
        final @NotNull SuggestionsBuilder builder,
        final long num
    ) {
        NullCheck.requireNonNull(builder, "builder");

        builder.suggest(String.valueOf(num));

        return builder.buildFuture();
    }

    public static CompletableFuture<Suggestions> suggestionFrom(
        final @NotNull SuggestionsBuilder builder,
        final char num
    ) {
        NullCheck.requireNonNull(builder, "builder");

        builder.suggest(String.valueOf(num));

        return builder.buildFuture();
    }

    public static CompletableFuture<Suggestions> suggestionFrom(
        final @NotNull SuggestionsBuilder builder,
        final float num
    ) {
        NullCheck.requireNonNull(builder, "builder");

        builder.suggest(String.valueOf(num));

        return builder.buildFuture();
    }

    public static CompletableFuture<Suggestions> suggestionFrom(
        final @NotNull SuggestionsBuilder builder,
        final double num
    ) {
        NullCheck.requireNonNull(builder, "builder");

        builder.suggest(String.valueOf(num));

        return builder.buildFuture();
    }

    public static CompletableFuture<Suggestions> suggestionFrom(
        final @NotNull SuggestionsBuilder builder,
        final boolean b
    ) {
        NullCheck.requireNonNull(builder, "builder");

        builder.suggest(String.valueOf(b));

        return builder.buildFuture();
    }

    public static <T> CompletableFuture<Suggestions> suggestionFrom(
        final @NotNull SuggestionsBuilder builder,
        final T b
    ) {
        NullCheck.requireNonNull(builder, "builder");

        builder.suggest(String.valueOf(b));

        return builder.buildFuture();
    }

    public static <T extends CommandSuggestible> CompletableFuture<Suggestions> customSuggestionFrom(
        final @NotNull SuggestionsBuilder builder,
        final T b
    ) {
        NullCheck.requireNonNull(builder, "builder");

        builder.suggest(b.toSuggestionString());

        return builder.buildFuture();
    }

    public static <E extends Enum<E>> CompletableFuture<Suggestions> suggestionFromEnum(
        final @NotNull SuggestionsBuilder builder,
        final @NotNull Class<E> clazz
    ) {
        NullCheck.requireNonNull(builder, "builder");
        NullCheck.requireNonNull(clazz, "clazz");

        Arrays.stream(clazz.getEnumConstants())
            .map(Enum::name)
            .map(String::toLowerCase)
            .forEach(builder::suggest);

        return builder.buildFuture();
    }

    public static <E extends Enum<E> & CommandSuggestible> CompletableFuture<Suggestions> customSuggestionFromEnumCustom(
        final @NotNull SuggestionsBuilder builder,
        final @NotNull Class<E> clazz
    ) {
        NullCheck.requireNonNull(builder, "builder");
        NullCheck.requireNonNull(clazz, "clazz");

        Arrays.stream(clazz.getEnumConstants())
            .map(CommandSuggestible::toSuggestionString)
            .forEach(builder::suggest);
        return builder.buildFuture();
    }

    public static <T> CompletableFuture<Suggestions> suggestionFromIterable(
        final @NotNull SuggestionsBuilder builder,
        final @NotNull Iterable<T> iterable
    ) {
        NullCheck.requireNonNull(builder, "builder");
        NullCheck.requireNonNull(iterable, "iterable");

        iterable.forEach(t -> builder.suggest(t.toString()));
        return builder.buildFuture();
    }

    public static <T extends CommandSuggestible> CompletableFuture<Suggestions> customSuggestionFromIterable(
        final @NotNull SuggestionsBuilder builder,
        final @NotNull Iterable<T> iterable
    ) {
        NullCheck.requireNonNull(builder, "builder");
        NullCheck.requireNonNull(iterable, "iterable");

        iterable.forEach(t -> builder.suggest(t.toSuggestionString()));
        return builder.buildFuture();
    }

    public static CompletableFuture<Suggestions> suggestionFromArray(
        final @NotNull SuggestionsBuilder builder,
        final byte @NotNull [] array
    ) {
        NullCheck.requireNonNull(builder, "builder");

        for (var b : array) {
            suggestionFrom(builder, b);
        }

        return builder.buildFuture();
    }

    public static CompletableFuture<Suggestions> suggestionFromArray(
        final @NotNull SuggestionsBuilder builder,
        final short @NotNull [] array
    ) {
        NullCheck.requireNonNull(builder, "builder");

        for (var b : array) {
            suggestionFrom(builder, b);
        }

        return builder.buildFuture();
    }

    public static CompletableFuture<Suggestions> suggestionFromArray(
        final @NotNull SuggestionsBuilder builder,
        final int @NotNull [] array
    ) {
        NullCheck.requireNonNull(builder, "builder");

        for (var b : array) {
            suggestionFrom(builder, b);
        }

        return builder.buildFuture();
    }

    public static CompletableFuture<Suggestions> suggestionFromArray(
        final @NotNull SuggestionsBuilder builder,
        final long @NotNull [] array
    ) {
        NullCheck.requireNonNull(builder, "builder");

        for (var b : array) {
            suggestionFrom(builder, b);
        }

        return builder.buildFuture();
    }

    public static CompletableFuture<Suggestions> suggestionFromArray(
        final @NotNull SuggestionsBuilder builder,
        final char @NotNull [] array
    ) {
        NullCheck.requireNonNull(builder, "builder");

        for (var b : array) {
            suggestionFrom(builder, b);
        }

        return builder.buildFuture();
    }

    public static CompletableFuture<Suggestions> suggestionFromArray(
        final @NotNull SuggestionsBuilder builder,
        final float @NotNull [] array
    ) {
        NullCheck.requireNonNull(builder, "builder");

        for (var b : array) {
            suggestionFrom(builder, b);
        }

        return builder.buildFuture();
    }

    public static CompletableFuture<Suggestions> suggestionFromArray(
        final @NotNull SuggestionsBuilder builder,
        final double @NotNull [] array
    ) {
        NullCheck.requireNonNull(builder, "builder");

        for (var b : array) {
            suggestionFrom(builder, b);
        }

        return builder.buildFuture();
    }

    public static <T> CompletableFuture<Suggestions> suggestionFromArray(
        final @NotNull SuggestionsBuilder builder,
        final @NotNull T[] array
    ) {
        NullCheck.requireNonNull(builder, "builder");
        NullCheck.requireAllNonNull(array, "array");

        Arrays.stream(array)
            .map(Object::toString)
            .forEach(builder::suggest);

        return builder.buildFuture();
    }

    public static <T extends CommandSuggestible> CompletableFuture<Suggestions> customSuggestionFromArray(
        final @NotNull SuggestionsBuilder builder,
        final @NotNull T[] array
    ) {
        NullCheck.requireNonNull(builder, "builder");
        NullCheck.requireAllNonNull(array, "array");

        Arrays.stream(array)
            .map(CommandSuggestible::toSuggestionString)
            .forEach(builder::suggest);

        return builder.buildFuture();
    }

    public static CompletableFuture<Suggestions> suggestionFrom(
        final @NotNull SuggestionsBuilder builder,
        final byte... nums
    ) {
        NullCheck.requireNonNull(builder, "builder");
        NullCheck.requireNonNull(nums, "nums");
        return suggestionFromArray(builder, nums);
    }

    public static CompletableFuture<Suggestions> suggestionFrom(
        final @NotNull SuggestionsBuilder builder,
        final short ... nums
    ) {
        NullCheck.requireNonNull(builder, "builder");
        NullCheck.requireNonNull(nums, "nums");
        return suggestionFromArray(builder, nums);
    }

    public static CompletableFuture<Suggestions> suggestionFrom(
        final @NotNull SuggestionsBuilder builder,
        final int ... nums
    ) {
        NullCheck.requireNonNull(builder, "builder");
        NullCheck.requireNonNull(nums, "nums");
        return suggestionFromArray(builder, nums);
    }

    public static CompletableFuture<Suggestions> suggestionFrom(
        final @NotNull SuggestionsBuilder builder,
        final long ... nums
    ) {
        NullCheck.requireNonNull(builder, "builder");
        NullCheck.requireNonNull(nums, "nums");
        return suggestionFromArray(builder, nums);
    }

    public static CompletableFuture<Suggestions> suggestionFrom(
        final @NotNull SuggestionsBuilder builder,
        final char ... nums
    ) {
        NullCheck.requireNonNull(builder, "builder");
        NullCheck.requireNonNull(nums, "nums");
        return suggestionFromArray(builder, nums);
    }

    public static CompletableFuture<Suggestions> suggestionFrom(
        final @NotNull SuggestionsBuilder builder,
        final float ... nums
    ) {
        NullCheck.requireNonNull(builder, "builder");
        NullCheck.requireNonNull(nums, "nums");
        return suggestionFromArray(builder, nums);
    }

    public static CompletableFuture<Suggestions> suggestionFrom(
        final @NotNull SuggestionsBuilder builder,
        final double ... nums
    ) {
        NullCheck.requireNonNull(builder, "builder");
        NullCheck.requireNonNull(nums, "nums");
        return suggestionFromArray(builder, nums);
    }

    @SafeVarargs
    public static <T> CompletableFuture<Suggestions> suggestionFrom(
        final @NotNull SuggestionsBuilder builder,
        final T ... nums
    ) {
        NullCheck.requireNonNull(builder, "builder");
        NullCheck.requireNonNull(nums, "nums");
        return suggestionFromArray(builder, nums);
    }

    @SafeVarargs
    public static <T extends CommandSuggestible> CompletableFuture<Suggestions> customSuggestionFrom(
        final @NotNull SuggestionsBuilder builder,
        final T ... nums
    ) {
        NullCheck.requireNonNull(builder, "builder");
        NullCheck.requireNonNull(nums, "nums");
        return customSuggestionFromArray(builder, nums);
    }
    
}
