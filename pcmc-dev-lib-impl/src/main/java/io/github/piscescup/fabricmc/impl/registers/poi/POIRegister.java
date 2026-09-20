package io.github.piscescup.fabricmc.impl.registers.poi;

import io.github.piscescup.fabricmc.api.PostRegistrable;
import io.github.piscescup.fabricmc.api.poi.POIPostRegistrable;
import io.github.piscescup.fabricmc.api.poi.POIPreRegistrable;
import io.github.piscescup.fabricmc.impl.mixins.vanilla.PoiTypesMixin;
import io.github.piscescup.fabricmc.impl.registers.Register;
import io.github.piscescup.util.validation.ArgumentCheck;
import io.github.piscescup.util.validation.NullCheck;
import io.github.piscescup.util.validation.StateCheck;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 *
 * @author REN YuanTong
 * @since
 */
public class POIRegister
    extends Register<PoiType, PoiType, POIPreRegistrable, POIPostRegistrable>
    implements POIPreRegistrable, POIPostRegistrable,
                POIPreRegistrable.MatchingStates, POIPreRegistrable.MaxTickets,
                POIPreRegistrable.ValidRange
{
    private Set<BlockState> matchingStates;
    private int maxTickets;
    private int validRange;

    POIRegister(Identifier id) {
        super(BuiltInRegistries.POINT_OF_INTEREST_TYPE, id);
    }

    /**
     * Derives the translation key used for localized display text.
     *
     * <p>Implementations may obtain the key from the registered value, so this
     * method is used after registration has completed successfully.</p>
     *
     * @return the non-null, non-blank translation key
     */
    @Override
    protected String translateKey() {
        return "";
    }

    @Override
    public MaxTickets matchingStates(@NotNull Set<BlockState> matchingStates) {
        NullCheck.requireNonNull(matchingStates, "matchingStates");
        StateCheck.checkState(
            !matchingStates.isEmpty(), "matchingStates must not be empty."
        );

        this.matchingStates = matchingStates;
        return this;
    }

    @Override
    public ValidRange maxTickets(int maxTickets) {
        ArgumentCheck.requiresNonNegative(maxTickets);

        this.maxTickets = maxTickets;
        return this;
    }

    @Override
    public POIPreRegistrable validRange(int validRange) {
        ArgumentCheck.requiresNonNegative(validRange);
        this.validRange = validRange;
        return this;
    }

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
    @Override
    public @NotNull POIPostRegistrable register() {
        this.thingToBeRegistered = Registry.register(
            BuiltInRegistries.POINT_OF_INTEREST_TYPE,
            this.id,
            new PoiType(this.matchingStates, this.maxTickets, this.validRange)
        );

        PoiTypesMixin.pcmcDevLib$registerBlockStates(
            BuiltInRegistries.POINT_OF_INTEREST_TYPE.getOrThrow(this.resourceKey), this.matchingStates
        );

        return this;
    }
}
