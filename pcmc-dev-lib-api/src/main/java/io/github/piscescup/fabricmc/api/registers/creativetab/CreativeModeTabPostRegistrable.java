package io.github.piscescup.fabricmc.api.registers.creativetab;

import io.github.piscescup.fabricmc.api.registers.PostRegistrable;
import net.minecraft.world.item.CreativeModeTab;

/**
 * Provides post-registration operations for a {@link CreativeModeTab}.
 *
 * <p>Use this stage to add localized tab titles, inspect the registration
 * identity, collect the tab, or obtain the registered tab instance.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface CreativeModeTabPostRegistrable
    extends PostRegistrable<CreativeModeTab, CreativeModeTab, CreativeModeTabPostRegistrable>
{

}
