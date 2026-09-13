package io.github.piscescup.fabricmc.api.registers;

import org.jetbrains.annotations.NotNull;

/**
 * Represents the configuration stage of a registration operation.
 *
 * <p>This stage exposes the options that must be selected before an object is
 * registered. Calling {@link #register()} completes the operation and returns
 * the matching {@link PostRegistrable post-registration stage}.</p>
 *
 * @param <V> the base type accepted by the target registry
 * @param <T> the concrete type being registered
 * @param <PRE> the concrete pre-registration stage type
 * @param <POST> the matching post-registration stage type
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface PreRegistrable<
    V, T extends V,
    PRE extends PreRegistrable<V, T, PRE, POST>,
    POST extends PostRegistrable<V, T, POST>>
{

    /**
     * Registers the configured object and enters the matching
     * {@link PostRegistrable post-registration stage}.
     *
     * @return the matching post-registration stage
     */
    @NotNull
    POST register();
}
