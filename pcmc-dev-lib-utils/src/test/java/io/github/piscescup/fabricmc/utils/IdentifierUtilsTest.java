package io.github.piscescup.fabricmc.utils;

import net.minecraft.resources.Identifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdentifierUtilsTest {
    private static final String NAMESPACE = "example";
    private static final String BASE_PATH = "oak_table";

    private static Identifier base() {
        return Identifier.fromNamespaceAndPath(NAMESPACE, BASE_PATH);
    }

    private static void assertPath(String expectedPath, Identifier actual) {
        assertNotNull(actual, "identifier should not be null");
        assertEquals(NAMESPACE, actual.getNamespace(), "namespace should be preserved");
        assertEquals(expectedPath, actual.getPath(), "path mismatch");
        assertEquals(NAMESPACE + ":" + expectedPath, actual.toString(), "toString mismatch");
    }


    @Test
    @DisplayName("resolveParentPath: String simple and args")
    void resolveParentPath_stringOverloads() {
        Identifier base = base();

        assertPath(
            "furniture/oak_table",
            IdentifierUtils.resolveParentPath(base, "furniture")
        );

        assertPath(
            "furniture/wooden/oak_table",
            IdentifierUtils.resolveParentPath(base, "furniture", "wooden")
        );
    }

    @Test
    @DisplayName("resolveParentPath: byte simple and args")
    void resolveParentPath_byteOverloads() {
        Identifier base = base();

        assertPath(
            "1/oak_table",
            IdentifierUtils.resolveParentPath(base, (byte) 1)
        );

        assertPath(
            "1/2/oak_table",
            IdentifierUtils.resolveParentPath(base, (byte) 1, (byte) 2)
        );
    }

    @Test
    @DisplayName("resolveParentPath: short simple and args")
    void resolveParentPath_shortOverloads() {
        Identifier base = base();

        assertPath(
            "1/oak_table",
            IdentifierUtils.resolveParentPath(base, (short) 1)
        );

        assertPath(
            "1/2/oak_table",
            IdentifierUtils.resolveParentPath(base, (short) 1, (short) 2)
        );
    }

    @Test
    @DisplayName("resolveParentPath: int simple and args")
    void resolveParentPath_intOverloads() {
        Identifier base = base();

        assertPath(
            "1/oak_table",
            IdentifierUtils.resolveParentPath(base, 1)
        );

        assertPath(
            "1/2/oak_table",
            IdentifierUtils.resolveParentPath(base, 1, 2)
        );
    }

    @Test
    @DisplayName("resolveParentPath: long simple and args")
    void resolveParentPath_longOverloads() {
        Identifier base = base();

        assertPath(
            "1/oak_table",
            IdentifierUtils.resolveParentPath(base, 1L)
        );

        assertPath(
            "1/2/oak_table",
            IdentifierUtils.resolveParentPath(base, 1L, 2L)
        );
    }

    @Test
    @DisplayName("resolveParentPath: float simple and args")
    void resolveParentPath_floatOverloads() {
        Identifier base = base();

        assertPath(
            "1.5/oak_table",
            IdentifierUtils.resolveParentPath(base, 1.5F)
        );

        assertPath(
            "1.5/2.5/oak_table",
            IdentifierUtils.resolveParentPath(base, 1.5F, 2.5F)
        );
    }

    @Test
    @DisplayName("resolveParentPath: double simple and args")
    void resolveParentPath_doubleOverloads() {
        Identifier base = base();

        assertPath(
            "1.5/oak_table",
            IdentifierUtils.resolveParentPath(base, 1.5D)
        );

        assertPath(
            "1.5/2.5/oak_table",
            IdentifierUtils.resolveParentPath(base, 1.5D, 2.5D)
        );
    }

    @Test
    @DisplayName("resolveParentPath: boolean simple and args")
    void resolveParentPath_booleanOverloads() {
        Identifier base = base();

        assertPath(
            "true/oak_table",
            IdentifierUtils.resolveParentPath(base, true)
        );

        assertPath(
            "true/false/oak_table",
            IdentifierUtils.resolveParentPath(base, true, false)
        );
    }

    @Test
    @DisplayName("resolveParentPath: char simple and args")
    void resolveParentPath_charOverloads() {
        Identifier base = base();

        assertPath(
            "a/oak_table",
            IdentifierUtils.resolveParentPath(base, 'a')
        );

        assertPath(
            "a/b/oak_table",
            IdentifierUtils.resolveParentPath(base, 'a', 'b')
        );
    }

    @Test
    @DisplayName("resolveSubPath: String simple and args")
    void resolveSubPath_stringOverloads() {
        Identifier base = base();

        assertPath(
            "oak_table/leg",
            IdentifierUtils.resolveSubPath(base, "leg")
        );

        assertPath(
            "oak_table/leg/left",
            IdentifierUtils.resolveSubPath(base, "leg", "left")
        );
    }

    @Test
    @DisplayName("resolveSubPath: byte simple and args")
    void resolveSubPath_byteOverloads() {
        Identifier base = base();

        assertPath(
            "oak_table/1",
            IdentifierUtils.resolveSubPath(base, (byte) 1)
        );

        assertPath(
            "oak_table/1/2",
            IdentifierUtils.resolveSubPath(base, (byte) 1, (byte) 2)
        );
    }

    @Test
    @DisplayName("resolveSubPath: short simple and args")
    void resolveSubPath_shortOverloads() {
        Identifier base = base();

        assertPath(
            "oak_table/1",
            IdentifierUtils.resolveSubPath(base, (short) 1)
        );

        assertPath(
            "oak_table/1/2",
            IdentifierUtils.resolveSubPath(base, (short) 1, (short) 2)
        );
    }

    @Test
    @DisplayName("resolveSubPath: int simple and args")
    void resolveSubPath_intOverloads() {
        Identifier base = base();

        assertPath(
            "oak_table/1",
            IdentifierUtils.resolveSubPath(base, 1)
        );

        assertPath(
            "oak_table/1/2",
            IdentifierUtils.resolveSubPath(base, 1, 2)
        );
    }

    @Test
    @DisplayName("resolveSubPath: long simple and args")
    void resolveSubPath_longOverloads() {
        Identifier base = base();

        assertPath(
            "oak_table/1",
            IdentifierUtils.resolveSubPath(base, 1L)
        );

        assertPath(
            "oak_table/1/2",
            IdentifierUtils.resolveSubPath(base, 1L, 2L)
        );
    }

    @Test
    @DisplayName("resolveSubPath: float simple and args")
    void resolveSubPath_floatOverloads() {
        Identifier base = base();

        assertPath(
            "oak_table/1.5",
            IdentifierUtils.resolveSubPath(base, 1.5F)
        );

        assertPath(
            "oak_table/1.5/2.5",
            IdentifierUtils.resolveSubPath(base, 1.5F, 2.5F)
        );
    }

    @Test
    @DisplayName("resolveSubPath: double simple and args")
    void resolveSubPath_doubleOverloads() {
        Identifier base = base();

        assertPath(
            "oak_table/1.5",
            IdentifierUtils.resolveSubPath(base, 1.5D)
        );

        assertPath(
            "oak_table/1.5/2.5",
            IdentifierUtils.resolveSubPath(base, 1.5D, 2.5D)
        );
    }

    @Test
    @DisplayName("resolveSubPath: boolean simple and args")
    void resolveSubPath_booleanOverloads() {
        Identifier base = base();

        assertPath(
            "oak_table/true",
            IdentifierUtils.resolveSubPath(base, true)
        );

        assertPath(
            "oak_table/true/false",
            IdentifierUtils.resolveSubPath(base, true, false)
        );
    }

    @Test
    @DisplayName("resolveSubPath: char simple and args")
    void resolveSubPath_charOverloads() {
        Identifier base = base();

        assertPath(
            "oak_table/a",
            IdentifierUtils.resolveSubPath(base, 'a')
        );

        assertPath(
            "oak_table/a/b",
            IdentifierUtils.resolveSubPath(base, 'a', 'b')
        );
    }

    // ==================== 异常与不可变性 ====================

    @Test
    @DisplayName("null 参数应抛出 NullPointerException")
    void nullArgumentsThrow() {
        Identifier base = base();

        assertThrows(
            NullPointerException.class,
            () -> IdentifierUtils.resolveParentPath(null, "furniture")
        );
        assertThrows(
            NullPointerException.class,
            () -> IdentifierUtils.resolveParentPath(base, (String) null)
        );
        assertThrows(
            NullPointerException.class,
            () -> IdentifierUtils.resolveParentPath(base, (String[]) null)
        );
        assertThrows(
            NullPointerException.class,
            () -> IdentifierUtils.resolveParentPath(base, new String[]{"a", null})
        );
        assertThrows(
            NullPointerException.class,
            () -> IdentifierUtils.resolveParentPath(null, (byte) 1)
        );
        assertThrows(
            NullPointerException.class,
            () -> IdentifierUtils.resolveParentPath(base, (byte[]) null)
        );

        assertThrows(
            NullPointerException.class,
            () -> IdentifierUtils.resolveSubPath(null, "leg")
        );
        assertThrows(
            NullPointerException.class,
            () -> IdentifierUtils.resolveSubPath(base, (String) null)
        );
        assertThrows(
            NullPointerException.class,
            () -> IdentifierUtils.resolveSubPath(base, (String[]) null)
        );
        assertThrows(
            NullPointerException.class,
            () -> IdentifierUtils.resolveSubPath(base, new String[]{"leg", null})
        );
        assertThrows(
            NullPointerException.class,
            () -> IdentifierUtils.resolveSubPath(null, (byte) 1)
        );
        assertThrows(
            NullPointerException.class,
            () -> IdentifierUtils.resolveSubPath(base, (byte[]) null)
        );
    }

    @Test
    @DisplayName("Original Identifier can not be modified")
    void originalIdentifierIsNotModified() {
        Identifier base = base();

        IdentifierUtils.resolveParentPath(base, "furniture");
        IdentifierUtils.resolveSubPath(base, "leg");

        assertPath(BASE_PATH, base);
    }
}