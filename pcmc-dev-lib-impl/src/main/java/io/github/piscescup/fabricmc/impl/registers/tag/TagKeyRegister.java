package io.github.piscescup.fabricmc.impl.registers.tag;

import io.github.piscescup.fabricmc.api.tag.TagKeyPostRegistrable;
import io.github.piscescup.fabricmc.api.tag.TagKeyPreRegistrable;
import io.github.piscescup.fabricmc.constants.MCLanguage;
import io.github.piscescup.fabricmc.impl.store.lang.MutableTranslationsHolder;
import io.github.piscescup.fabricmc.impl.store.tag.MutableTagKeysHolder;
import io.github.piscescup.util.validation.NullCheck;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

/**
 * Implements the declaration and post-declaration stages for one {@link TagKey}.
 *
 * <p>Instances are created through {@link TagKeyRegisterFactory}. Construction
 * creates the {@link TagKey}; member additions immediately update
 * {@link MutableTagKeysHolder#INSTANCE}. {@link #register()} only returns this
 * same instance as the post-registration stage. It does not write tag data or
 * register a tag key as an ordinary registry entry.</p>
 *
 * <p>Identifier and resource-key members are collected as optional references,
 * nested tags as optional tag references, and registered values as required
 * members for the supplied data provider. Translation declarations are stored
 * separately in {@link MutableTranslationsHolder#INSTANCE}.</p>
 *
 * @param <T> the value type of the {@link Registry} targeted by the {@link TagKey}
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see TagKeyRegisterFactory
 * @see TagKeyPreRegistrable
 * @see TagKeyPostRegistrable
 */
public class TagKeyRegister<T>
    implements TagKeyPreRegistrable<T>, TagKeyPostRegistrable<T>
{
    /**
     * The target {@link Registry} key supplied at construction time.
     * Used to create {@link #tagKey} and exposed by {@link #tagResourceKey()}.
     */
    private final ResourceKey<? extends Registry<T>> registryKey;

    /**
     * The {@link TagKey} created at construction time and returned by {@link #get()}.
     * Member declarations are collected under this key in {@link MutableTagKeysHolder}.
     */
    private final TagKey<T> tagKey;

    /**
     * The namespaced {@link Identifier} supplied for the tag at construction time.
     * Exposed by {@link #identifier()} without performing a registry lookup.
     */
    private final Identifier id;
    
    /**
     * Creates the {@link TagKey} used by this declaration.
     *
     * <p>Both references are retained as-is and passed to
     * {@link TagKey#create(ResourceKey, Identifier)}. No member collector is
     * created until a member is added.</p>
     *
     * @param registryKey the {@link Registry} whose values may belong to the tag; must not be {@code null}
     * @param id          the namespaced {@link TagKey} identifier; must not be {@code null}
     */
    TagKeyRegister(
        ResourceKey<? extends Registry<T>> registryKey,
        Identifier id
    ) {
        this.registryKey = registryKey;
        this.id = id;
        this.tagKey = TagKey.create(
            registryKey, this.id
        );
    }
    
    /**
     * Returns the translation key derived by {@link TagKey#getTranslationKey()}.
     *
     * @return the tag's translation key
     */
    String getTranslationKey() {
        return this.tagKey.getTranslationKey();
    }

    /**
     * Records localized text for the {@link TagKey} in {@link MutableTranslationsHolder#INSTANCE}.
     *
     * @param lang        the target {@link MCLanguage}; must not be {@code null}
     * @param translation the localized tag name; must not be {@code null}
     * @return this instance as its post-registration stage
     * @throws NullPointerException if {@code lang} or {@code translation} is {@code null}
     * @throws IllegalStateException if different text already exists for the language and key
     */
    @Override
    public @NonNull TagKeyPostRegistrable<T> translate(@NotNull MCLanguage lang, @NotNull String translation) {
        NullCheck.requireNonNull(lang, "lang");
        NullCheck.requireNonNull(translation, "translation");
        MutableTranslationsHolder.INSTANCE.add(
            lang, getTranslationKey(), translation
        );
        return this;
    }

    /**
     * Returns the {@link TagKey} created when this declaration was constructed.
     *
     * @return the declared tag key
     */
    @Override
    public @NonNull TagKey<T> get() {
        return tagKey;
    }

    /**
     * Returns the namespaced {@link Identifier} of the declared {@link TagKey}.
     *
     * @return the tag identifier
     */
    @Override
    public @NotNull Identifier identifier() {
        return id;
    }


    /**
     * Records an optional member {@link Identifier} in {@link MutableTagKeysHolder#INSTANCE}.
     *
     * @param id the member identifier within this tag's {@link Registry}; must not be {@code null}
     * @return this instance as its pre-registration stage
     * @throws NullPointerException if {@code id} is {@code null}
     */
    @Override
    public TagKeyPreRegistrable<T> add(@NotNull Identifier id) {
        NullCheck.requireNonNull(id, "id");
        MutableTagKeysHolder.INSTANCE.addEntry(this.tagKey, id);
        return this;
    }

    /**
     * Records a registered instance as a required member of this {@link TagKey}.
     *
     * <p>The supplied data provider resolves the instance in the target registry
     * during data generation; this method only retains the instance.</p>
     *
     * @param entry the registered value to include; must not be {@code null}
     * @return this instance as its pre-registration stage
     * @throws NullPointerException if {@code entry} is {@code null}
     */
    @Override
    public TagKeyPreRegistrable<T> addEntry(@NotNull T entry) {
        NullCheck.requireNonNull(entry, "entry");
        MutableTagKeysHolder.INSTANCE.addEntry(this.tagKey, entry);
        return this;
    }

    /**
     * Records an optional reference to another {@link TagKey} in the same {@link Registry}.
     *
     * @param tagKey the nested {@link TagKey} to include; must not be {@code null}
     * @return this instance as its pre-registration stage
     * @throws NullPointerException if {@code tagKey} is {@code null}
     * @throws IllegalArgumentException if the nested tag targets a different registry
     */
    @Override
    public TagKeyPreRegistrable<T> addTag(@NotNull TagKey<T> tagKey) {
        NullCheck.requireNonNull(tagKey, "tagKey");
        MutableTagKeysHolder.INSTANCE.addTag(this.tagKey, tagKey);
        return this;
    }

    /**
     * Records an optional member {@link ResourceKey} in {@link MutableTagKeysHolder#INSTANCE}.
     *
     * <p>The current implementation returns {@code null} after recording the
     * member, so callers must retain their original stage reference instead
     * of chaining from this method's result.</p>
     *
     * @param key the member key from this tag's target {@link Registry}; must not be {@code null}
     * @return {@code null} in the current implementation
     * @throws NullPointerException if {@code key} is {@code null}
     * @throws IllegalArgumentException if the key targets a different registry
     */
    @Override
    public TagKeyPreRegistrable<T> addRegistryKey(@NotNull ResourceKey<T> key) {
        NullCheck.requireNonNull(key, "key");
        MutableTagKeysHolder.INSTANCE.addEntry(this.tagKey, key);
        return this;
    }

    /**
     * Completes the declaration stage and exposes post-registration operations.
     *
     * <p>The tag key already exists, and member additions have already been
     * collected. This method performs no additional mutation and does not create
     * an empty collector for a declaration with no members.</p>
     *
     * @return this instance as the tag post-registration stage
     */
    @Override
    public @NotNull TagKeyPostRegistrable<T> register() {

        return this;
    }

    /**
     * Rejects requests for an ordinary registry-entry key for this tag.
     *
     * @return never returns normally
     * @throws UnsupportedOperationException always; use {@link #tagResourceKey()}
     *                                       to obtain the tag's target registry key
     */
    @Override
    public @NotNull ResourceKey<TagKey<T>> resourceKey() {
        String info = null;
        try {
            info = "Use %s for TagKeyRegister to get the resource key"
                .formatted(TagKeyPostRegistrable.class.getMethod("tagResourceKey"));
        } catch (NoSuchMethodException _) {}

        throw new UnsupportedOperationException(info);
    }

    /**
     * Returns the key of the {@link Registry} whose values this {@link TagKey} groups.
     *
     * @return the target registry key, such as the item or block registry key
     */
    @Override
    public ResourceKey<? extends Registry<T>> tagResourceKey() {
        return registryKey;
    }
}
