package io.github.piscescup.fabricmc.api.trade;

import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.trading.TradeCost;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Configures the available trades and selection settings for one career level.
 *
 * <p>Use {@link #add(String, VillagerTrade)} to declare new trades,
 * {@link #include(ResourceKey)} to reference existing trades, and
 * {@link #includeTag(TagKey)} to include an existing trade tag. The supplied
 * implementation defaults to selecting two trades, disallows duplicates, and
 * leaves the random-sequence identifier unset.</p>
 *
 * <p>Instances are supplied to the callbacks configured through
 * {@link VillagerProfessionTradesPreRegistrable}. Resource identifiers are
 * resolved when the declaration is attached to a profession.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see VillagerLevelTradeMetadata
 */
public interface VillagerLevelTradeBuilder {
    /**
     * Declares a new trade using a path local to this profession and level.
     *
     * <p>For profession {@code example:trader}, level 1, and path
     * {@code coal_for_emerald}, the supplied implementation produces the trade
     * identifier {@code example:trader1/coal_for_emerald}. The declaration also
     * becomes a member of the generated level tag.</p>
     *
     * @param path the local trade path without a namespace prefix
     * @param trade the trade definition to generate
     * @return this level builder
     * @throws NullPointerException if {@code path} is {@code null}
     * @throws IllegalStateException if this path already has a trade definition
     */
    VillagerLevelTradeBuilder add(
        String path,
        VillagerTrade trade
    );

    /**
     * Adds an existing trade key to the generated level tag.
     *
     * <p>The referenced trade must be supplied by the loaded registries or data
     * packs. Repeated inclusions of the same key are coalesced.</p>
     *
     * @param trade the existing trade key; must not be {@code null}
     * @return this level builder
     * @throws NullPointerException if {@code trade} is {@code null}
     */
    VillagerLevelTradeBuilder include(
        ResourceKey<VillagerTrade> trade
    );

    /**
     * Nests an existing trade tag in the generated level tag.
     *
     * @param tag the trade tag to include; must not be {@code null}
     * @return this level builder
     * @throws NullPointerException if {@code tag} is {@code null}
     */
    VillagerLevelTradeBuilder includeTag(
        TagKey<VillagerTrade> tag
    );

    /**
     * Sets a constant number of trades to select from this level's trade set.
     *
     * @param amount the number of trades to select; defaults to 2
     * @return this level builder
     * @see #amount(NumberProvider)
     */
    default VillagerLevelTradeBuilder amount(int amount) {
        return amount(ConstantValue.exactly(amount));
    }

    /**
     * Sets the number provider used to select how many trades this level offers.
     *
     * <p>The provider is retained for the generated trade set and is not
     * evaluated during declaration.</p>
     *
     * @param amount the selection-count provider; must not be {@code null}
     * @return this level builder
     * @throws NullPointerException if {@code amount} is {@code null}
     */
    VillagerLevelTradeBuilder amount(
        NumberProvider amount
    );

    /**
     * Sets whether the trade set may select the same trade more than once.
     *
     * @param allowDuplicates whether duplicate selections are allowed; defaults to {@code false}
     * @return this level builder
     */
    VillagerLevelTradeBuilder allowDuplicates(
        boolean allowDuplicates
    );

    /**
     * Selects an explicit random-sequence identifier for the generated trade set.
     *
     * @param randomSequence the sequence identifier; must not be {@code null}
     * @return this level builder
     * @throws NullPointerException if {@code randomSequence} is {@code null}
     */
    VillagerLevelTradeBuilder randomSequence(
        Identifier randomSequence
    );

    /**
     * Creates a trade with one input item and one output item.
     *
     * <p>The reputation discount is {@code 0.05F}. No merchant predicate,
     * output-item modifiers, or double-price enchantment set is configured.</p>
     *
     * @param wantedItem the item paid by the player
     * @param wantedCount the required input count
     * @param givenItem the item received by the player
     * @param givenCount the output count
     * @param maxUses the maximum uses before restocking is required
     * @param villagerXp the experience awarded to the villager per trade
     * @return a new trade definition
     */
    @Contract("_, _, _, _, _, _ -> new")
    @NotNull
    static VillagerTrade trade(@NotNull ItemLike wantedItem, int wantedCount, @NotNull ItemLike givenItem, int givenCount, int maxUses, int villagerXp) {
        return new VillagerTrade(
            new TradeCost(wantedItem, wantedCount),
            new ItemStackTemplate(givenItem.asItem(), givenCount),
            maxUses,
            villagerXp,
            0.05F,
            Optional.empty(),
            List.of()
        );
    }

    /**
     * Creates a trade requiring two input items and producing one output item.
     *
     * <p>Uses the same {@code 0.05F} reputation discount as the single-input
     * overload, with no merchant predicate, output-item modifiers, or
     * double-price enchantment set.</p>
     *
     * @param wantedItem the first item paid by the player
     * @param wantedCount the required first-input count
     * @param additionWantItem the second item paid by the player
     * @param additionWantCount the required second-input count
     * @param givenItem the item received by the player
     * @param givenCount the output count
     * @param maxUses the maximum uses before restocking is required
     * @param villagerXp the experience awarded to the villager per trade
     * @return a new trade definition with a second input cost
     * @see #trade(ItemLike, int, ItemLike, int, int, int)
     */
    static VillagerTrade trade(
        @NotNull ItemLike wantedItem, int wantedCount,
        @NotNull ItemLike additionWantItem, int additionWantCount,
        @NotNull ItemLike givenItem, int givenCount,
        int maxUses, int villagerXp
    ) {
        return new VillagerTrade(
            new TradeCost(wantedItem, wantedCount),
            Optional.of(new TradeCost(additionWantItem, additionWantCount)),
            new ItemStackTemplate(givenItem.asItem(), givenCount),
            maxUses,
            villagerXp,
            0.05F,
            Optional.empty(),
            List.of()
        );
    }

}
