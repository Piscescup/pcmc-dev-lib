package io.github.piscescup.fabricmc.api.registers.creativetab;

import io.github.piscescup.fabricmc.api.registers.PostRegistrable;
import net.minecraft.world.item.CreativeModeTab;

/**
 * Provides post-registration operations for a {@link CreativeModeTab}.
 *
 * <p>This stage is returned by {@link CreativeModeTabPreRegistrable#register()}
 * after the configured tab has been built and registered. Use the inherited
 * methods to record localized titles for language data generation, inspect
 * the identifier and resource key, collect the tab, or retrieve it through
 * {@link #get()}.</p>
 *
 * <p>Typical usage:</p>
 * <pre>{@code
 * public static final CreativeModeTab TEST_TAB = CREATIVE_TABS.path("test_tab")
 *     .iconFromItem(Items.IRON_INGOT)
 *     .addItems(Items.IRON_INGOT, Items.GOLD_INGOT)
 *     .register()
 *     .translate(MCLanguage.EN_US, "Test Tab")
 *     .get();
 * }</pre>
 *
 * <p>The supplied implementation uses the translation key
 * {@code itemGroup.<namespace>.<path>} for the title. Icon, content, and layout
 * options are selected through the pre-registration stage before building the tab.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see CreativeModeTabPreRegistrable
 * @see PostRegistrable
 */
public interface CreativeModeTabPostRegistrable
    extends PostRegistrable<CreativeModeTab, CreativeModeTab, CreativeModeTabPostRegistrable>
{

}
