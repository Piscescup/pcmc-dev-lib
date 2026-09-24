package io.github.piscescup.fabricmc.utils.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.piscescup.util.validation.NullCheck;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

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
        this.suggestions.add(suggestion);
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
        this.suggestions.add(suggestion.toString());
        return this;
    }

    /**
     * Adds a custom suggestion using
     * {@link CommandSuggestible#toSuggestionString()}.
     *
     * @param suggestion the value to convert and add
     * @param <T> the type of the custom suggestion
     * @return this builder
     * @throws NullPointerException if {@code suggestion} is {@code null}
     */
    public <T extends CommandSuggestible> CmdSuggestionBuilder<S> suggestCustom(
        @NotNull final T suggestion
    ) {
        NullCheck.requireNonNull(suggestion, "suggestion");
        this.suggestions.add(suggestion.toSuggestionString());
        return this;
    }

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
    public <E extends Enum<E>> CmdSuggestionBuilder<S> suggestEnum(
        @NotNull final E suggestion
    ) {
        NullCheck.requireNonNull(suggestion, "suggestion");
        this.suggestions.add(suggestion.name().toLowerCase(Locale.ROOT));
        return this;
    }

    /**
     * Adds an enum constant using
     * {@link CommandSuggestible#toSuggestionString()}.
     *
     * @param suggestion the enum constant to convert and add
     * @param <E> the enum type, which must implement {@link CommandSuggestible}
     * @return this builder
     * @throws NullPointerException if {@code suggestion} is {@code null}
     */
    public <E extends Enum<E> & CommandSuggestible> CmdSuggestionBuilder<S>
    suggestEnumCustom(@NotNull final E suggestion) {
        NullCheck.requireNonNull(suggestion, "suggestion");
        this.suggestions.add(suggestion.toSuggestionString());
        return this;
    }

    /**
     * Adds every constant of the specified enum type using its name converted
     * to lowercase.
     *
     * @param suggestion the enum class whose constants will be added
     * @param <E> the enum type
     * @return this builder
     * @throws NullPointerException if {@code suggestion} is {@code null}
     */
    public <E extends Enum<E>> CmdSuggestionBuilder<S> suggestEnums(
        @NotNull final Class<E> suggestion
    ) {
        NullCheck.requireNonNull(suggestion, "suggestion");
        E[] enumConstants = suggestion.getEnumConstants();
        Arrays.stream(enumConstants)
            .map(Enum::name)
            .map(s -> s.toLowerCase(Locale.ROOT))
            .forEach(this.suggestions::add);
        return this;
    }

    /**
     * Adds every constant of the specified enum type using
     * {@link CommandSuggestible#toSuggestionString()}.
     *
     * @param suggestion the enum class whose constants will be added
     * @param <E> the enum type, which must implement {@link CommandSuggestible}
     * @return this builder
     * @throws NullPointerException if {@code suggestion} is {@code null}
     */
    public <E extends Enum<E> & CommandSuggestible> CmdSuggestionBuilder<S> suggestEnumsCustom(
        @NotNull final Class<E> suggestion
    ) {
        NullCheck.requireNonNull(suggestion, "suggestion");
        E[] enumConstants = suggestion.getEnumConstants();
        Arrays.stream(enumConstants)
            .map(CommandSuggestible::toSuggestionString)
            .forEach(this.suggestions::add);
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
            this.suggestions.add(suggestion.toString());
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
        suggestions.forEach(this.suggestions::add);
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