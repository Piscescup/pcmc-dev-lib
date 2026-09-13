package io.github.piscescup.fabricmc.impl.registers.tag;

import io.github.piscescup.fabricmc.api.registers.tag.TagKeyPostRegistrable;
import io.github.piscescup.fabricmc.api.registers.tag.TagKeyPreRegistrable;
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
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class TagKeyRegister<T>
    implements TagKeyPreRegistrable<T>, TagKeyPostRegistrable<T>
{
    private final ResourceKey<? extends Registry<T>> registryKey;
    private final TagKey<T> tagKey;
    private final Identifier id;
    
    protected TagKeyRegister(
        ResourceKey<? extends Registry<T>> registryKey,
        Identifier id
    ) {
        this.registryKey = registryKey;
        this.id = id;
        this.tagKey = TagKey.create(
            registryKey, this.id
        );
    }
    
    String getTranslationKey() {
        return this.tagKey.getTranslationKey();
    }

    @Override
    public @NonNull TagKeyPostRegistrable<T> translate(@NotNull MCLanguage lang, @NotNull String translation) {
        NullCheck.requireNonNull(lang, "lang");
        NullCheck.requireNonNull(translation, "translation");
        MutableTranslationsHolder.INSTANCE.add(
            lang, getTranslationKey(), translation
        );
        return this;
    }

    @Override
    public @NonNull TagKey<T> get() {
        return tagKey;
    }

    @Override
    public @NotNull Identifier identifier() {
        return id;
    }


    @Override
    public TagKeyPreRegistrable<T> add(@NotNull Identifier id) {
        NullCheck.requireNonNull(id, "id");
        MutableTagKeysHolder.INSTANCE.addEntry(this.tagKey, id);
        return this;
    }

    @Override
    public TagKeyPreRegistrable<T> addEntry(@NotNull T entry) {
        NullCheck.requireNonNull(entry, "entry");
        MutableTagKeysHolder.INSTANCE.addEntry(this.tagKey, entry);
        return this;
    }

    @Override
    public TagKeyPreRegistrable<T> addTag(@NotNull TagKey<T> tagKey) {
        NullCheck.requireNonNull(tagKey, "tagKey");
        MutableTagKeysHolder.INSTANCE.addTag(this.tagKey, tagKey);
        return this;
    }

    @Override
    public TagKeyPreRegistrable<T> addRegistryKey(@NotNull ResourceKey<T> key) {
        NullCheck.requireNonNull(key, "key");
        MutableTagKeysHolder.INSTANCE.addEntry(this.tagKey, key);
        return null;
    }

    /**
     * Registers the configured object and enters the
     * post-registration stage.
     *
     * @return the corresponding post-registration stage
     */
    @Override
    public @NotNull TagKeyPostRegistrable<T> register() {

        return this;
    }

    @Override
    public @NotNull ResourceKey<TagKey<T>> resourceKey() {
        String info = null;
        try {
            info = "Use %s for TagKeyRegister to get the resource key"
                .formatted(TagKeyPostRegistrable.class.getMethod("tagResourceKey"));
        } catch (NoSuchMethodException _) {}

        throw new UnsupportedOperationException(info);
    }

    @Override
    public ResourceKey<? extends Registry<T>> tagResourceKey() {
        return registryKey;
    }
}
