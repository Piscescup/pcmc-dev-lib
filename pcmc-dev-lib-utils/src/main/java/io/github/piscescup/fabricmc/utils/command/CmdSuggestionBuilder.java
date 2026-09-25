package io.github.piscescup.fabricmc.utils.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.piscescup.util.validation.NullCheck;
import net.minecraft.commands.CommandSourceStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Collects command suggestions and adds them to a Brigadier
 * {@link SuggestionsBuilder}.
 *
 * <p>Suggestions are stored as strings in insertion order. D</p>
 *
 * <p>Use the {@code suggest} methods for individual values and the
 * {@code suggestArray} methods for arrays. Call {@link #build()}
 * to build a related {@link SuggestionProvider}.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public final class CmdSuggestionBuilder<S> {
    private final Set<String> suggestions;
    private final List<Function<CommandContext<S>, ? extends Iterable<?>>> dynamicSuggestions;

    private CmdSuggestionBuilder() {
        this.suggestions = new LinkedHashSet<>();
        this.dynamicSuggestions = new ArrayList<>();
    }

    @Contract(" -> new")
    @NotNull
    public static <S> CmdSuggestionBuilder<S> create() {
        return new CmdSuggestionBuilder<>();
    }

    @Contract(" -> new")
    @NotNull
    public static CmdSuggestionBuilder<CommandSourceStack> createByCommandSourceStack() {
        return new CmdSuggestionBuilder<>();
    }

    /**
     * Adds a byte value as a suggestion.
     *
     * @param suggestion the value to add
     * @return this builder
     */
    public CmdSuggestionBuilder<S> suggest(final byte suggestion) {
        this.suggestions.add(String.valueOf(suggestion));
        return this;
    }

    /**
     * Adds a short value as a suggestion.
     *
     * @param suggestion the value to add
     * @return this builder
     */
    public CmdSuggestionBuilder<S> suggest(final short suggestion) {
        this.suggestions.add(String.valueOf(suggestion));
        return this;
    }

    /**
     * Adds an integer value as a suggestion.
     *
     * @param suggestion the value to add
     * @return this builder
     */
    public CmdSuggestionBuilder<S> suggest(final int suggestion) {
        this.suggestions.add(String.valueOf(suggestion));
        return this;
    }

    /**
     * Adds a long value as a suggestion.
     *
     * @param suggestion the value to add
     * @return this builder
     */
    public CmdSuggestionBuilder<S> suggest(final long suggestion) {
        this.suggestions.add(String.valueOf(suggestion));
        return this;
    }

    /**
     * Adds a character as a suggestion.
     *
     * @param suggestion the character to add
     * @return this builder
     */
    public CmdSuggestionBuilder<S> suggest(final char suggestion) {
        this.suggestions.add(String.valueOf(suggestion));
        return this;
    }

    /**
     * Adds a boolean value as a suggestion.
     *
     * @param suggestion the value to add
     * @return this builder
     */
    public CmdSuggestionBuilder<S> suggest(final boolean suggestion) {
        this.suggestions.add(String.valueOf(suggestion));
        return this;
    }

    /**
     * Adds a float value as a suggestion.
     *
     * @param suggestion the value to add
     * @return this builder
     */
    public CmdSuggestionBuilder<S> suggest(final float suggestion) {
        this.suggestions.add(String.valueOf(suggestion));
        return this;
    }

    /**
     * Adds a double value as a suggestion.
     *
     * @param suggestion the value to add
     * @return this builder
     */
    public CmdSuggestionBuilder<S> suggest(final double suggestion) {
        this.suggestions.add(String.valueOf(suggestion));
        return this;
    }

    /**
     * Adds a string as a suggestion.
     *
     * @param suggestion the string to add
     * @return this builder
     * @throws NullPointerException if {@code suggestion} is {@code null}
     */
    public CmdSuggestionBuilder<S> suggest(@NotNull final String suggestion) {
        NullCheck.requireNonNull(suggestion, "suggestion");
        this.suggestions.add(suggestion.replace(' ', '_'));
        return this;
    }

    /**
     * Adds an object's string representation as a suggestion.
     *
     * <p>The object's {@link Object#toString()} method is used to produce
     * the suggestion.</p>
     *
     * @param suggestion the object to convert and add
     * @param <T> the object's type
     * @return this builder
     * @throws NullPointerException if {@code suggestion} is {@code null}
     */
    public <T> CmdSuggestionBuilder<S> suggest(@NotNull final T suggestion) {
        NullCheck.requireNonNull(suggestion, "suggestion");

        String result = suggestion instanceof CommandSuggestible suggestible?
            suggestible.toSuggestionString() :
            suggestion.toString();

        this.suggest(result);
        return this;
    }

    /**
     * Adds each element from the given stream as a suggestion.
     * Each element is converted using {@link #suggest(Object)}.
     * This operation consumes the stream.
     *
     * @param suggestions the stream of values to suggest
     * @param <T> the type of values in the stream
     * @return this builder
     * @throws NullPointerException if {@code suggestions} is {@code null}
     */
    public <T> CmdSuggestionBuilder<S> suggest(
        @NotNull final Stream<T> suggestions
    ) {
        NullCheck.requireNonNull(suggestions, "suggestions");
        suggestions
            .forEach(this::suggest);

        return this;
    }

    /**
     * Adds a provider that generates suggestions from the {@link CommandContext}.
     * The provider is called when suggestions are requested.
     *
     * @param suggestionProvider a function that returns values to suggest
     *                           for the given command context
     * @return this builder
     * @throws NullPointerException if {@code suggestionProvider} is {@code null}
     */
    public CmdSuggestionBuilder<S> suggestDynamic(
        @NotNull final Function<CommandContext<S>, ? extends Iterable<?>> suggestionProvider
    ) {
        NullCheck.requireNonNull(
            suggestionProvider,
            "suggestionProvider"
        );

        this.dynamicSuggestions.add(suggestionProvider);
        return this;
    }

    /**
     * Adds an enum constant using its name converted to lowercase with
     * {@link Locale#ROOT}.
     *
     * @param suggestion the enum constant to add
     * @param <E> the enum type
     * @return this builder
     * @throws NullPointerException if {@code suggestion} is {@code null}
     */
    public <E extends Enum<E>> CmdSuggestionBuilder<S> suggest(
        @NotNull final E suggestion
    ) {
        NullCheck.requireNonNull(suggestion, "suggestion");
        String result = suggestion instanceof CommandSuggestible suggestible?
            suggestible.toSuggestionString() :
            suggestion.name().toLowerCase(Locale.ROOT);

        this.suggest(result);
        return this;
    }

    /**
     * Adds every constant of the specified enum type using its name converted
     * to lowercase.
     *
     * @param suggestions the enum class whose constants will be added
     * @param <E> the enum type
     * @return this builder
     * @throws NullPointerException if {@code suggestions} is {@code null}
     */
    public <E extends Enum<E>> CmdSuggestionBuilder<S> suggest(
        @NotNull final Class<E> suggestions
    ) {
        NullCheck.requireNonNull(suggestions, "suggestions");
        E[] enumConstants = suggestions.getEnumConstants();
        for (E constant : enumConstants) {
            suggest(constant);
        }
        return this;
    }


    /**
     * Adds each element's string representation as a suggestion.
     *
     * @param suggestions the values to convert and add
     * @param <T> the element type
     * @return this builder
     * @throws NullPointerException if {@code suggestions}, or an element
     *                              checked by {@code requireAllNonNull}, is
     *                              {@code null}
     */
    public <T> CmdSuggestionBuilder<S> suggest(
        @NotNull final Iterable<T> suggestions
    ) {
        NullCheck.requireAllNonNull(suggestions, "suggestions");
        for (final T suggestion : suggestions) {
            suggest(suggestion);
        }
        return this;
    }

    /**
     * Adds each string in the iterable as a suggestion.
     *
     * @param suggestions the strings to add
     * @return this builder
     * @throws NullPointerException if {@code suggestions}, or an element
     *                              checked by {@code requireAllNonNull}, is
     *                              {@code null}
     */
    public CmdSuggestionBuilder<S> suggestStrings(
        @NotNull final Iterable<String> suggestions
    ) {
        NullCheck.requireAllNonNull(suggestions, "suggestions");
        suggestions.forEach(this::suggest);
        return this;
    }

    /**
     * Adds each byte in the array as a suggestion.
     *
     * @param suggestions the values to add
     * @return this builder
     */
    @Contract("_ -> this")
    public CmdSuggestionBuilder<S> suggestArray(
        final byte @NotNull [] suggestions
    ) {
        for (var suggestion : suggestions) {
            suggest(suggestion);
        }
        return this;
    }

    /**
     * Adds each short in the array as a suggestion.
     *
     * @param suggestions the values to add
     * @return this builder
     */
    @Contract("_ -> this")
    public CmdSuggestionBuilder<S> suggestArray(
        final short @NotNull [] suggestions
    ) {
        for (var suggestion : suggestions) {
            suggest(suggestion);
        }
        return this;
    }

    /**
     * Adds each integer in the array as a suggestion.
     *
     * @param suggestions the values to add
     * @return this builder
     */
    @Contract("_ -> this")
    public CmdSuggestionBuilder<S> suggestArray(
        final int @NotNull [] suggestions
    ) {
        for (var suggestion : suggestions) {
            suggest(suggestion);
        }
        return this;
    }

    /**
     * Adds each long in the array as a suggestion.
     *
     * @param suggestions the values to add
     * @return this builder
     */
    @Contract("_ -> this")
    public CmdSuggestionBuilder<S> suggestArray(
        final long @NotNull [] suggestions
    ) {
        for (var suggestion : suggestions) {
            suggest(suggestion);
        }
        return this;
    }

    /**
     * Adds each character in the array as a suggestion.
     *
     * @param suggestions the characters to add
     * @return this builder
     */
    @Contract("_ -> this")
    public CmdSuggestionBuilder<S> suggestArray(
        final char @NotNull [] suggestions
    ) {
        for (var suggestion : suggestions) {
            suggest(suggestion);
        }
        return this;
    }

    /**
     * Adds each float in the array as a suggestion.
     *
     * @param suggestions the values to add
     * @return this builder
     */
    @Contract("_ -> this")
    public CmdSuggestionBuilder<S> suggestArray(
        final float @NotNull [] suggestions
    ) {
        for (var suggestion : suggestions) {
            suggest(suggestion);
        }
        return this;
    }

    /**
     * Adds each double in the array as a suggestion.
     *
     * @param suggestions the values to add
     * @return this builder
     */
    @Contract("_ -> this")
    public CmdSuggestionBuilder<S> suggestArray(
        final double @NotNull [] suggestions
    ) {
        for (var suggestion : suggestions) {
            suggest(suggestion);
        }
        return this;
    }

    /**
     * Adds each string in the array as a suggestion.
     *
     * @param suggestions the strings to add
     * @return this builder
     * @throws NullPointerException if {@code suggestions}, or an element
     *                              checked by {@code requireAllNonNull}, is
     *                              {@code null}
     */
    public CmdSuggestionBuilder<S> suggestArray(
        @NotNull final String[] suggestions
    ) {
        NullCheck.requireAllNonNull(suggestions, "suggestions");
        this.suggestions.addAll(Arrays.asList(suggestions));
        return this;
    }

    /**
     * Adds each object's string representation as a suggestion.
     *
     * @param suggestions the objects to convert and add
     * @param <T> the element type
     * @return this builder
     * @throws NullPointerException if {@code suggestions}, or an element
     *                              checked by {@code requireAllNonNull}, is
     *                              {@code null}
     */
    @Contract("_ -> this")
    public <T> CmdSuggestionBuilder<S> suggestArray(
        @NotNull final T @NotNull [] suggestions
    ) {
        NullCheck.requireAllNonNull(suggestions, "suggestions");
        for (var suggestion : suggestions) {
            suggest(suggestion);
        }
        return this;
    }

    /**
     * Build the {@link CmdSuggestionBuilder} to {@link SuggestionProvider}
     *
     * @return a {@link SuggestionProvider}.
     */
    public @NotNull SuggestionProvider<S> build() {
        final List<String> fixedSuggestions =
            List.copyOf(this.suggestions);

        final List<Function<CommandContext<S>, ? extends Iterable<?>>> dynamicProviders =
            List.copyOf(this.dynamicSuggestions);

        return (context, builder) -> {
            Set<String> resolvedSuggestions =
                new LinkedHashSet<>(fixedSuggestions);

            for (var provider : dynamicProviders) {
                Iterable<?> values = NullCheck.requireNonNull(
                    provider.apply(context),
                    "dynamic suggestions"
                );

                for (Object value : values) {
                    NullCheck.requireNonNull(
                        value,
                        "dynamic suggestion"
                    );

                    String suggestion = value instanceof CommandSuggestible suggestible
                            ? suggestible.toSuggestionString()
                            : value.toString();

                    resolvedSuggestions.add(suggestion);
                }
            }

            String remaining = builder
                .getRemaining()
                .toLowerCase(Locale.ROOT);

            resolvedSuggestions.stream()
                .filter(suggestion ->
                    suggestion
                        .toLowerCase(Locale.ROOT)
                        .startsWith(remaining)
                )
                .forEach(builder::suggest);

            return builder.buildFuture();
        };
    }
}