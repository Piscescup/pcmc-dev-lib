package io.github.piscescup.fabricmc.api.registers;

import org.jetbrains.annotations.NotNull;

/**
 * Represents the configuration stage before an object is registered.
 *
 * <p>Calling {@link #register()} completes the registration and returns
 * the corresponding post-registration stage.</p>
 *
 * @param <T>    the type of object being registered
 * @param <PRE>  the concrete pre-registration stage type
 * @param <POST> the corresponding post-registration stage type
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
     * Sets the registry path.
     *
     * @param path the path portion of the registry identifier
     * @return this pre-registration stage
     */
    @NotNull
    PRE path(@NotNull String path);

    /**
     * Registers the configured object and enters the
     * post-registration stage.
     *
     * @return the corresponding post-registration stage
     */
    @NotNull
    POST register();
}
