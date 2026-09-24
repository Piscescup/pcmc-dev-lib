package io.github.piscescup.fabricmc.utils.command;

import org.jetbrains.annotations.NotNull;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
@FunctionalInterface
public interface CommandSuggestible {
    @NotNull
    String toSuggestionString();
}
