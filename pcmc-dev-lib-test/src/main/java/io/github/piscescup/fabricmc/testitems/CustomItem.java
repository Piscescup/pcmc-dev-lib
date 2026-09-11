package io.github.piscescup.fabricmc.testitems;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import static io.github.piscescup.fabricmc.Refs.MOD_LOGGER;

/**
 *
 * @author REN YuanTong
 * @since
 */
public class CustomItem extends Item {

    public CustomItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        super.use(level, player, hand);

        MOD_LOGGER.info("{} used {} in {}", player, this, hand);

        return InteractionResult.SUCCESS;
    }
}
