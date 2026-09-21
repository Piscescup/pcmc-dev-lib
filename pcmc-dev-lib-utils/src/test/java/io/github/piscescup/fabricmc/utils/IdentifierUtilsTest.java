package io.github.piscescup.fabricmc.utils;

import net.minecraft.resources.Identifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IdentifierUtilsTest {
    @Test
    public void testIdentifierResolveSubPath() {
        Identifier id = Identifier.fromNamespaceAndPath("pcmc-dev-lib-utils", "villager_trade");

        Identifier fireworkMan = IdentifierUtils.resolveSubPath(id, "firework_man");

        Assertions.assertEquals(
            "pcmc-dev-lib-utils:villager_trade/firework_man", fireworkMan.toString()
        );
    }
}