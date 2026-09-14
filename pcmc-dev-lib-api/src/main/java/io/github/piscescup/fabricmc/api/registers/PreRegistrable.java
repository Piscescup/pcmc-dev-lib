package io.github.piscescup.fabricmc.api.registers;

import org.jetbrains.annotations.NotNull;

/**
 * Represents the pre-registration configuration stage of a registration operation.
 *
 * <p>This stage exposes the options that must be selected before an object is
 * registered. Calling {@link #register()} finalizes the configuration, performs
 * the actual registration, and returns the matching
 * {@link PostRegistrable post-registration stage} for further customization
 * (such as translation, etc.).</p>
 *
 * <p>Typical usage, taking item registration as an example:</p>
 * <pre>{@code
 * public static final Item ITEM1 = ITEMS.path("item1")
 *     // Pre-registration configuration
 *     .register() // Perform registration
 *     // Post-registration customization
 *     // Translation
 *     .translate(MCLanguage.EN_US, "Item1")
 *     // Retrieve the registered entry
 *     .get();
 * }</pre>
 *
 * <p>Implementations are expected to be stateful builders. In typical
 * implementations, the same instance flows from the pre-registration stage into
 * the post-registration stage. For example, a concrete implementation may
 * implement both {@code PRE} and {@code POST}, and its {@link #register()} method
 * may return {@code this} as the post-registration stage.</p>
 *
 * <p>Calling {@link #register()} more than once on the same instance is
 * discouraged, because it may attempt to register the same object repeatedly
 * with the underlying registry. After {@link #register()} has been called, the
 * instance should be treated as the returned post-registration stage rather than
 * being reused as a pre-registration builder.</p>
 *
 * @param <V>    the base type accepted by the target registry,
 *               such as {@code Item}
 * @param <T>    the concrete type being registered; must be a subtype of {@code V}
 * @param <PRE>  the concrete pre-registration stage type (self type),
 *               used to preserve the fluent API across intermediate calls,
 *               such as {@code ItemPreRegistrable<I>}
 * @param <POST> the matching post-registration stage type returned by
 *               {@link #register()}, such as {@code ItemPostRegistrable<I>}
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see PostRegistrable
 */
public interface PreRegistrable<
    V, T extends V,
    PRE extends PreRegistrable<V, T, PRE, POST>,
    POST extends PostRegistrable<V, T, POST>>
{

    /**
     * Completes the pre-registration configuration, registers the object into the
     * target registry, and returns the matching
     * {@link PostRegistrable post-registration stage}.
     *
     * <p>This method is the terminal operation of the pre-registration stage.
     * In typical implementations, the returned post-registration stage may be the
     * same object as this pre-registration stage, but it should be used through
     * the {@code POST} interface for subsequent customization.</p>
     *
     * @return the matching post-registration stage
     * @see PostRegistrable
     */
    @NotNull
    POST register();
}
