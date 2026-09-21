package io.github.piscescup.fabricmc.utils;

import io.github.piscescup.util.validation.NullCheck;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * The utility class for {@link Identifier Minecraft Identifier}.
 *
 * <p><b>Purpose:</b> Provides helper methods to add parent path segments
 * (prefixes) or child path segments (suffixes) to an {@link Identifier}
 * while preserving its namespace.</p>
 *
 * <p><b>Example:</b></p>
 * <pre>{@code
 * Identifier id = Identifier.fromNamespaceAndPath("example", "oak_table");
 *
 * Identifier parent = IdentifierUtils.resolveParentPath(id, "furniture");
 * // example:furniture/oak_table
 *
 * Identifier child = IdentifierUtils.resolveSubPath(id, "leg");
 * // example:oak_table/leg
 * }</pre>
 *
 * <p><b>Exceptions:</b> Methods generally throw {@link NullPointerException}
 * when a required argument is {@code null}, and {@link IllegalArgumentException}
 * when the resulting identifier path is invalid.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public final class IdentifierUtils {
    /**
     * The path separator used by Minecraft identifiers.
     */
    public static final String IDENTIFIER_PATH_SEPARATOR =
        "/";

    /**
     * Private constructor to prevent instantiation.
     */
    private IdentifierUtils() {}

    /**
     * Prepends a parent segment to the parent of the specified identifier while
     * preserving its namespace.
     *
     * <p><b>Purpose:</b> Adds {@code parent + "/"} to the beginning of the
     * identifier's path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier identifier = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveParentPath(
     *     identifier,
     *     "furniture"
     * );
     * // result: example:furniture/oak_table
     * }</pre>
     *
     * @param identifier the identifier whose parent is to be prefixed;
     *                   must not be {@code null}
     * @param parent the parent segment to prepend to the identifier's
     *               current parent; must not be {@code null}
     * @return a new identifier with {@code parent} prepended to its
     *         original parent
     * @throws NullPointerException if {@code identifier} or {@code parent}
     *                              is {@code null}
     * @throws IllegalArgumentException if the resulting identifier parent
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveParentPath(
        @NotNull Identifier identifier, @NotNull String parent
    ) {
        NullCheck.requireNonNull(identifier, "identifier");
        NullCheck.requireNonNull(parent, "parent");

        return identifier.withPrefix(parent + IDENTIFIER_PATH_SEPARATOR);
    }

    /**
     * Prepends multiple string path segments to an identifier.
     *
     * <p><b>Purpose:</b> Joins the given segments with {@code "/"} and
     * prepends the result to the identifier's path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier identifier = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveParentPath(
     *     identifier,
     *     "furniture",
     *     "wooden"
     * );
     * // result: example:furniture/wooden/oak_table
     * }</pre>
     *
     * @param identifier the identifier whose path is to be prefixed;
     *                   must not be {@code null}
     * @param parents the path segments to prepend, in order;
     *                the array and its elements must not be {@code null}
     * @return a new identifier containing the prefixed path segments
     * @throws NullPointerException if {@code identifier}, {@code parents},
     *                              or any element of {@code parents} is
     *                              {@code null}
     * @throws IllegalArgumentException if the resulting identifier path
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveParentPath(
        @NotNull Identifier identifier, @NotNull String... parents
    ) {
        NullCheck.requireNonNull(identifier, "identifier");
        NullCheck.requireAllNonNull(parents, "parents");
        return resolveParentPath(
            identifier,
            String.join(IDENTIFIER_PATH_SEPARATOR, parents)
        );
    }

    /**
     * Prepends a byte value as a parent segment to an identifier.
     *
     * <p><b>Purpose:</b> Converts {@code parent} to a string and prepends it
     * to the identifier's path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier identifier = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveParentPath(
     *     identifier,
     *     (byte) 1
     * );
     * // result: example:1/oak_table
     * }</pre>
     *
     * @param identifier the identifier whose parent is to be prefixed;
     *                   must not be {@code null}
     * @param parent the byte value to prepend
     * @return a new identifier containing the prefixed byte value
     * @throws NullPointerException if {@code identifier} is {@code null}
     * @throws IllegalArgumentException if the resulting identifier parent
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveParentPath(
        @NotNull Identifier identifier, byte parent
    ) {
        return resolveParentPath(identifier, Byte.toString(parent));
    }

    /**
     * Prepends byte values as path segments to an identifier.
     *
     * <p><b>Purpose:</b> Converts each byte value to a string, joins them
     * with {@code "/"}, and prepends the result to the identifier's path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier identifier = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveParentPath(
     *     identifier,
     *     (byte) 1,
     *     (byte) 2
     * );
     * // result: example:1/2/oak_table
     * }</pre>
     *
     * @param identifier the identifier whose path is to be prefixed;
     *                   must not be {@code null}
     * @param parents the byte values to prepend, in order;
     *                the array must not be {@code null}
     * @return a new identifier containing the prefixed byte values
     * @throws NullPointerException if {@code identifier} or {@code parents}
     *                              is {@code null}
     * @throws IllegalArgumentException if the resulting identifier path
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveParentPath(
        @NotNull Identifier identifier, byte @NotNull ... parents
    ) {
        NullCheck.requireNonNull(identifier, "identifier");
        StringBuilder result = new StringBuilder();
        for (byte parent : parents) {
            if (!result.isEmpty()) result.append(IDENTIFIER_PATH_SEPARATOR);
            result.append(parent);
        }
        return resolveParentPath(identifier, result.toString());
    }

    /**
     * Prepends a short value as a parent segment to an identifier.
     *
     * <p><b>Purpose:</b> Converts {@code parent} to a string and prepends it
     * to the identifier's path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier identifier = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveParentPath(
     *     identifier,
     *     (short) 1
     * );
     * // result: example:1/oak_table
     * }</pre>
     *
     * @param identifier the identifier whose parent is to be prefixed;
     *                   must not be {@code null}
     * @param parent the short value to prepend
     * @return a new identifier containing the prefixed short value
     * @throws NullPointerException if {@code identifier} is {@code null}
     * @throws IllegalArgumentException if the resulting identifier parent
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveParentPath(
        @NotNull Identifier identifier, short parent
    ) {
        return resolveParentPath(identifier, Short.toString(parent));
    }

    /**
     * Prepends short values as path segments to an identifier.
     *
     * <p><b>Purpose:</b> Converts each short value to a string, joins them
     * with {@code "/"}, and prepends the result to the identifier's path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier identifier = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveParentPath(
     *     identifier,
     *     (short) 1,
     *     (short) 2
     * );
     * // result: example:1/2/oak_table
     * }</pre>
     *
     * @param identifier the identifier whose path is to be prefixed;
     *                   must not be {@code null}
     * @param parents the short values to prepend, in order;
     *                the array must not be {@code null}
     * @return a new identifier containing the prefixed short values
     * @throws NullPointerException if {@code identifier} or {@code parents}
     *                              is {@code null}
     * @throws IllegalArgumentException if the resulting identifier path
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveParentPath(
        @NotNull Identifier identifier, short @NotNull... parents
    ) {
        NullCheck.requireNonNull(identifier, "identifier");
        StringBuilder result = new StringBuilder();
        for (short parent : parents) {
            if (!result.isEmpty()) result.append(IDENTIFIER_PATH_SEPARATOR);
            result.append(parent);
        }
        return resolveParentPath(identifier, result.toString());
    }

    /**
     * Prepends an integer value as a parent segment to an identifier.
     *
     * <p><b>Purpose:</b> Converts {@code parent} to a string and prepends it
     * to the identifier's path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier identifier = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveParentPath(
     *     identifier,
     *     1
     * );
     * // result: example:1/oak_table
     * }</pre>
     *
     * @param identifier the identifier whose parent is to be prefixed;
     *                   must not be {@code null}
     * @param parent the integer value to prepend
     * @return a new identifier containing the prefixed integer value
     * @throws NullPointerException if {@code identifier} is {@code null}
     * @throws IllegalArgumentException if the resulting identifier parent
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveParentPath(
        @NotNull Identifier identifier, int parent
    ) {
        return resolveParentPath(identifier, Integer.toString(parent));
    }

    /**
     * Prepends integer values as path segments to an identifier.
     *
     * <p><b>Purpose:</b> Converts each integer value to a string, joins them
     * with {@code "/"}, and prepends the result to the identifier's path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier identifier = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveParentPath(
     *     identifier,
     *     1,
     *     2
     * );
     * // result: example:1/2/oak_table
     * }</pre>
     *
     * @param identifier the identifier whose path is to be prefixed;
     *                   must not be {@code null}
     * @param parents the integer values to prepend, in order;
     *                the array must not be {@code null}
     * @return a new identifier containing the prefixed integer values
     * @throws NullPointerException if {@code identifier} or {@code parents}
     *                              is {@code null}
     * @throws IllegalArgumentException if the resulting identifier path
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveParentPath(
        @NotNull Identifier identifier, int @NotNull... parents
    ) {
        NullCheck.requireNonNull(identifier, "identifier");
        StringBuilder result = new StringBuilder();
        for (int parent : parents) {
            if (!result.isEmpty()) result.append(IDENTIFIER_PATH_SEPARATOR);
            result.append(parent);
        }
        return resolveParentPath(identifier, result.toString());
    }

    /**
     * Prepends a long value as a parent segment to an identifier.
     *
     * <p><b>Purpose:</b> Converts {@code parent} to a string and prepends it
     * to the identifier's path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier identifier = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveParentPath(
     *     identifier,
     *     1L
     * );
     * // result: example:1/oak_table
     * }</pre>
     *
     * @param identifier the identifier whose parent is to be prefixed;
     *                   must not be {@code null}
     * @param parent the long value to prepend
     * @return a new identifier containing the prefixed long value
     * @throws NullPointerException if {@code identifier} is {@code null}
     * @throws IllegalArgumentException if the resulting identifier parent
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveParentPath(
        @NotNull Identifier identifier, long parent
    ) {
        return resolveParentPath(identifier, Long.toString(parent));
    }

    /**
     * Prepends long values as path segments to an identifier.
     *
     * <p><b>Purpose:</b> Converts each long value to a string, joins them
     * with {@code "/"}, and prepends the result to the identifier's path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier identifier = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveParentPath(
     *     identifier,
     *     1L,
     *     2L
     * );
     * // result: example:1/2/oak_table
     * }</pre>
     *
     * @param identifier the identifier whose path is to be prefixed;
     *                   must not be {@code null}
     * @param parents the long values to prepend, in order;
     *                the array must not be {@code null}
     * @return a new identifier containing the prefixed long values
     * @throws NullPointerException if {@code identifier} or {@code parents}
     *                              is {@code null}
     * @throws IllegalArgumentException if the resulting identifier path
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveParentPath(
        @NotNull Identifier identifier, long @NotNull... parents
    ) {
        NullCheck.requireNonNull(identifier, "identifier");
        StringBuilder result = new StringBuilder();
        for (long parent : parents) {
            if (!result.isEmpty()) result.append(IDENTIFIER_PATH_SEPARATOR);
            result.append(parent);
        }
        return resolveParentPath(identifier, result.toString());
    }

    /**
     * Prepends a float value as a parent segment to an identifier.
     *
     * <p><b>Purpose:</b> Converts {@code parent} to a string and prepends it
     * to the identifier's path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier identifier = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveParentPath(
     *     identifier,
     *     1.5F
     * );
     * // result: example:1.5/oak_table
     * }</pre>
     *
     * @param identifier the identifier whose parent is to be prefixed;
     *                   must not be {@code null}
     * @param parent the float value to prepend
     * @return a new identifier containing the prefixed float value
     * @throws NullPointerException if {@code identifier} is {@code null}
     * @throws IllegalArgumentException if the resulting identifier parent
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveParentPath(
        @NotNull Identifier identifier, float parent
    ) {
        return resolveParentPath(identifier, Float.toString(parent));
    }

    /**
     * Prepends float values as path segments to an identifier.
     *
     * <p><b>Purpose:</b> Converts each float value to a string, joins them
     * with {@code "/"}, and prepends the result to the identifier's path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier identifier = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveParentPath(
     *     identifier,
     *     1.5F,
     *     2.5F
     * );
     * // result: example:1.5/2.5/oak_table
     * }</pre>
     *
     * @param identifier the identifier whose path is to be prefixed;
     *                   must not be {@code null}
     * @param parents the float values to prepend, in order;
     *                the array must not be {@code null}
     * @return a new identifier containing the prefixed float values
     * @throws NullPointerException if {@code identifier} or {@code parents}
     *                              is {@code null}
     * @throws IllegalArgumentException if the resulting identifier path
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveParentPath(
        @NotNull Identifier identifier, float @NotNull... parents
    ) {
        NullCheck.requireNonNull(identifier, "identifier");
        StringBuilder result = new StringBuilder();
        for (float parent : parents) {
            if (!result.isEmpty()) result.append(IDENTIFIER_PATH_SEPARATOR);
            result.append(parent);
        }
        return resolveParentPath(identifier, result.toString());
    }

    /**
     * Prepends a double value as a parent segment to an identifier.
     *
     * <p><b>Purpose:</b> Converts {@code parent} to a string and prepends it
     * to the identifier's path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier identifier = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveParentPath(
     *     identifier,
     *     1.5D
     * );
     * // result: example:1.5/oak_table
     * }</pre>
     *
     * @param identifier the identifier whose parent is to be prefixed;
     *                   must not be {@code null}
     * @param parent the double value to prepend
     * @return a new identifier containing the prefixed double value
     * @throws NullPointerException if {@code identifier} is {@code null}
     * @throws IllegalArgumentException if the resulting identifier parent
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveParentPath(
        @NotNull Identifier identifier, double parent
    ) {
        return resolveParentPath(identifier, Double.toString(parent));
    }

    /**
     * Prepends double values as path segments to an identifier.
     *
     * <p><b>Purpose:</b> Converts each double value to a string, joins them
     * with {@code "/"}, and prepends the result to the identifier's path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier identifier = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveParentPath(
     *     identifier,
     *     1.5D,
     *     2.5D
     * );
     * // result: example:1.5/2.5/oak_table
     * }</pre>
     *
     * @param identifier the identifier whose path is to be prefixed;
     *                   must not be {@code null}
     * @param parents the double values to prepend, in order;
     *                the array must not be {@code null}
     * @return a new identifier containing the prefixed double values
     * @throws NullPointerException if {@code identifier} or {@code parents}
     *                              is {@code null}
     * @throws IllegalArgumentException if the resulting identifier path
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveParentPath(
        @NotNull Identifier identifier, double @NotNull... parents
    ) {
        NullCheck.requireNonNull(identifier, "identifier");
        StringBuilder result = new StringBuilder();
        for (double parent : parents) {
            if (!result.isEmpty()) result.append(IDENTIFIER_PATH_SEPARATOR);
            result.append(parent);
        }
        return resolveParentPath(identifier, result.toString());
    }

    /**
     * Prepends a boolean value as a parent segment to an identifier.
     *
     * <p><b>Purpose:</b> Converts {@code parent} to a string and prepends it
     * to the identifier's path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier identifier = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveParentPath(
     *     identifier,
     *     true
     * );
     * // result: example:true/oak_table
     * }</pre>
     *
     * @param identifier the identifier whose parent is to be prefixed;
     *                   must not be {@code null}
     * @param parent the boolean value to prepend
     * @return a new identifier containing the prefixed boolean value
     * @throws NullPointerException if {@code identifier} is {@code null}
     * @throws IllegalArgumentException if the resulting identifier parent
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveParentPath(
        @NotNull Identifier identifier, boolean parent
    ) {
        return resolveParentPath(identifier, Boolean.toString(parent));
    }

    /**
     * Prepends boolean values as path segments to an identifier.
     *
     * <p><b>Purpose:</b> Converts each boolean value to a string, joins them
     * with {@code "/"}, and prepends the result to the identifier's path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier identifier = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveParentPath(
     *     identifier,
     *     true,
     *     false
     * );
     * // result: example:true/false/oak_table
     * }</pre>
     *
     * @param identifier the identifier whose path is to be prefixed;
     *                   must not be {@code null}
     * @param parents the boolean values to prepend, in order;
     *                the array must not be {@code null}
     * @return a new identifier containing the prefixed boolean values
     * @throws NullPointerException if {@code identifier} or {@code parents}
     *                              is {@code null}
     * @throws IllegalArgumentException if the resulting identifier path
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveParentPath(
        @NotNull Identifier identifier, boolean @NotNull... parents
    ) {
        NullCheck.requireNonNull(identifier, "identifier");
        StringBuilder result = new StringBuilder();
        for (boolean parent : parents) {
            if (!result.isEmpty()) result.append(IDENTIFIER_PATH_SEPARATOR);
            result.append(parent);
        }
        return resolveParentPath(identifier, result.toString());
    }

    /**
     * Prepends a character value as a parent segment to an identifier.
     *
     * <p><b>Purpose:</b> Converts {@code parent} to a string and prepends it
     * to the identifier's path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier identifier = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveParentPath(
     *     identifier,
     *     'a'
     * );
     * // result: example:a/oak_table
     * }</pre>
     *
     * @param identifier the identifier whose parent is to be prefixed;
     *                   must not be {@code null}
     * @param parent the character value to prepend
     * @return a new identifier containing the prefixed character value
     * @throws NullPointerException if {@code identifier} is {@code null}
     * @throws IllegalArgumentException if the resulting identifier parent
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveParentPath(
        @NotNull Identifier identifier, char parent
    ) {
        return resolveParentPath(identifier, Character.toString(parent));
    }

    /**
     * Prepends character values as path segments to an identifier.
     *
     * <p><b>Purpose:</b> Converts each character value to a string, joins
     * them with {@code "/"}, and prepends the result to the identifier's
     * path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier identifier = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveParentPath(
     *     identifier,
     *     'a',
     *     'b'
     * );
     * // result: example:a/b/oak_table
     * }</pre>
     *
     * @param identifier the identifier whose path is to be prefixed;
     *                   must not be {@code null}
     * @param parents the character values to prepend, in order;
     *                the array must not be {@code null}
     * @return a new identifier containing the prefixed character values
     * @throws NullPointerException if {@code identifier} or {@code parents}
     *                              is {@code null}
     * @throws IllegalArgumentException if the resulting identifier path
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveParentPath(
        @NotNull Identifier identifier, char @NotNull... parents
    ) {
        NullCheck.requireNonNull(identifier, "identifier");
        StringBuilder result = new StringBuilder();
        for (char parent : parents) {
            if (!result.isEmpty()) result.append(IDENTIFIER_PATH_SEPARATOR);
            result.append(parent);
        }
        return resolveParentPath(identifier, result.toString());
    }

    /**
     * Appends a string path segment to an identifier while preserving its
     * namespace.
     *
     * <p><b>Purpose:</b> Adds {@code "/" + path} to the end of the
     * identifier's path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier parent = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveSubPath(parent, "leg");
     * // result: example:oak_table/leg
     * }</pre>
     *
     * @param parent the identifier whose path is to be suffixed;
     *               must not be {@code null}
     * @param path the path segment to append; must not be {@code null}
     * @return a new identifier with {@code path} appended to its
     *         original path
     * @throws NullPointerException if {@code parent} or {@code path} is
     *                              {@code null}
     * @throws IllegalArgumentException if the resulting identifier path
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveSubPath(
        @NotNull Identifier parent, @NotNull String path
    ) {
        NullCheck.requireNonNull(parent, "parent");
        NullCheck.requireNonNull(path, "path");
        return parent
            .withSuffix(IDENTIFIER_PATH_SEPARATOR)
            .withSuffix(path);
    }

    /**
     * Appends multiple string path segments to an identifier.
     *
     * <p><b>Purpose:</b> Joins the given segments with {@code "/"} and
     * appends the result to the identifier's path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier parent = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveSubPath(
     *     parent,
     *     "leg",
     *     "left"
     * );
     * // Note: the current implementation may produce
     * // example:oak_table/leg/left
     * }</pre>
     *
     * @param parent the identifier whose path is to be suffixed;
     *               must not be {@code null}
     * @param paths the path segments to append, in order;
     *              the array and its elements must not be {@code null}
     * @return a new identifier with the joined segments appended to its
     *         original path
     * @throws NullPointerException if {@code parent}, {@code paths}, or
     *                              any element of {@code paths} is
     *                              {@code null}
     * @throws IllegalArgumentException if the resulting identifier path
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveSubPath(
        @NotNull Identifier parent, @NotNull String... paths
    ) {
        NullCheck.requireNonNull(parent, "parent");
        NullCheck.requireAllNonNull(paths, "paths");

        String finalPath = String.join(IDENTIFIER_PATH_SEPARATOR, paths);

        return resolveSubPath(parent, finalPath);
    }

    /**
     * Appends a byte value as a path segment to an identifier.
     *
     * <p><b>Purpose:</b> Converts {@code path} to a string and appends it
     * to the identifier's path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier parent = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveSubPath(parent, (byte) 1);
     * // result: example:oak_table/1
     * }</pre>
     *
     * @param parent the identifier whose path is to be suffixed;
     *               must not be {@code null}
     * @param path the byte value to append
     * @return a new identifier containing the appended byte value
     * @throws NullPointerException if {@code parent} is {@code null}
     * @throws IllegalArgumentException if the resulting identifier path
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveSubPath(
        @NotNull Identifier parent, byte path
    ) {
        NullCheck.requireNonNull(parent, "parent");
        return parent
            .withSuffix(IDENTIFIER_PATH_SEPARATOR)
            .withSuffix(Byte.toString(path));
    }

    /**
     * Appends byte values as path segments to an identifier.
     *
     * <p><b>Purpose:</b> Converts each byte value to a string, joins them
     * with {@code "/"}, and appends the result to the identifier's path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier parent = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveSubPath(
     *     parent,
     *     (byte) 1,
     *     (byte) 2
     * );
     * // Note: the current implementation may produce
     * // example:oak_table/1/2
     * }</pre>
     *
     * @param parent the identifier whose path is to be suffixed;
     *               must not be {@code null}
     * @param paths the byte values to append, in order;
     *              the array must not be {@code null}
     * @return a new identifier containing the appended byte values
     * @throws NullPointerException if {@code parent} or {@code paths} is
     *                              {@code null}
     * @throws IllegalArgumentException if the resulting identifier path
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveSubPath(
        @NotNull Identifier parent, byte @NotNull... paths
    ) {
        NullCheck.requireNonNull(parent, "parent");
        StringBuilder strPath = new StringBuilder();
        for (byte path : paths) {
            strPath.append(IDENTIFIER_PATH_SEPARATOR)
                .append(path);
        }
        return parent.withSuffix(strPath.toString());
    }

    /**
     * Appends a short value as a path segment to an identifier.
     *
     * <p><b>Purpose:</b> Converts {@code path} to a string and appends it
     * to the identifier's path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier parent = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveSubPath(parent, (short) 1);
     * // result: example:oak_table/1
     * }</pre>
     *
     * @param parent the identifier whose path is to be suffixed;
     *               must not be {@code null}
     * @param path the short value to append
     * @return a new identifier containing the appended short value
     * @throws NullPointerException if {@code parent} is {@code null}
     * @throws IllegalArgumentException if the resulting identifier path
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveSubPath(
        @NotNull Identifier parent, short path
    ) {
        NullCheck.requireNonNull(parent, "parent");
        return parent
            .withSuffix(IDENTIFIER_PATH_SEPARATOR)
            .withSuffix(Short.toString(path));
    }

    /**
     * Appends short values as path segments to an identifier.
     *
     * <p><b>Purpose:</b> Converts each short value to a string, joins them
     * with {@code "/"}, and appends the result to the identifier's path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier parent = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveSubPath(
     *     parent,
     *     (short) 1,
     *     (short) 2
     * );
     * // Note: the current implementation may produce
     * // example:oak_table/1/2
     * }</pre>
     *
     * @param parent the identifier whose path is to be suffixed;
     *               must not be {@code null}
     * @param paths the short values to append, in order;
     *              the array must not be {@code null}
     * @return a new identifier containing the appended short values
     * @throws NullPointerException if {@code parent} or {@code paths} is
     *                              {@code null}
     * @throws IllegalArgumentException if the resulting identifier path
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveSubPath(
        @NotNull Identifier parent, short @NotNull... paths
    ) {
        NullCheck.requireNonNull(parent, "parent");
        StringBuilder strPath = new StringBuilder();
        for (short path : paths) {
            strPath.append(IDENTIFIER_PATH_SEPARATOR)
                .append(path);
        }
        return parent.withSuffix(strPath.toString());
    }

    /**
     * Appends an integer value as a path segment to an identifier.
     *
     * <p><b>Purpose:</b> Converts {@code path} to a string and appends it
     * to the identifier's path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier parent = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveSubPath(parent, 1);
     * // result: example:oak_table/1
     * }</pre>
     *
     * @param parent the identifier whose path is to be suffixed;
     *               must not be {@code null}
     * @param path the integer value to append
     * @return a new identifier containing the appended integer value
     * @throws NullPointerException if {@code parent} is {@code null}
     * @throws IllegalArgumentException if the resulting identifier path
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveSubPath(
        @NotNull Identifier parent, int path
    ) {
        NullCheck.requireNonNull(parent, "parent");
        return parent
            .withSuffix(IDENTIFIER_PATH_SEPARATOR)
            .withSuffix(Integer.toString(path));
    }

    /**
     * Appends integer values as path segments to an identifier.
     *
     * <p><b>Purpose:</b> Converts each integer value to a string, joins
     * them with {@code "/"}, and appends the result to the identifier's
     * path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier parent = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveSubPath(parent, 1, 2);
     * // Note: the current implementation may produce
     * // example:oak_table//1/2
     * }</pre>
     *
     * @param parent the identifier whose path is to be suffixed;
     *               must not be {@code null}
     * @param paths the integer values to append, in order;
     *              the array must not be {@code null}
     * @return a new identifier containing the appended integer values
     * @throws NullPointerException if {@code parent} or {@code paths} is
     *                              {@code null}
     * @throws IllegalArgumentException if the resulting identifier path
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveSubPath(
        @NotNull Identifier parent, int @NotNull... paths
    ) {
        NullCheck.requireNonNull(parent, "parent");
        StringBuilder strPath = new StringBuilder();
        for (int path : paths) {
            strPath.append(IDENTIFIER_PATH_SEPARATOR)
                .append(path);
        }
        return parent.withSuffix(strPath.toString());
    }

    /**
     * Appends a long value as a path segment to an identifier.
     *
     * <p><b>Purpose:</b> Converts {@code path} to a string and appends it
     * to the identifier's path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier parent = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveSubPath(parent, 1L);
     * // result: example:oak_table/1
     * }</pre>
     *
     * @param parent the identifier whose path is to be suffixed;
     *               must not be {@code null}
     * @param path the long value to append
     * @return a new identifier containing the appended long value
     * @throws NullPointerException if {@code parent} is {@code null}
     * @throws IllegalArgumentException if the resulting identifier path
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveSubPath(
        @NotNull Identifier parent, long path
    ) {
        NullCheck.requireNonNull(parent, "parent");
        return parent
            .withSuffix(IDENTIFIER_PATH_SEPARATOR)
            .withSuffix(Long.toString(path));
    }

    /**
     * Appends long values as path segments to an identifier.
     *
     * <p><b>Purpose:</b> Converts each long value to a string, joins them
     * with {@code "/"}, and appends the result to the identifier's path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier parent = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveSubPath(parent, 1L, 2L);
     * // Note: the current implementation may produce
     * // example:oak_table/1/2
     * }</pre>
     *
     * @param parent the identifier whose path is to be suffixed;
     *               must not be {@code null}
     * @param paths the long values to append, in order;
     *              the array must not be {@code null}
     * @return a new identifier containing the appended long values
     * @throws NullPointerException if {@code parent} or {@code paths} is
     *                              {@code null}
     * @throws IllegalArgumentException if the resulting identifier path
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveSubPath(
        @NotNull Identifier parent, long @NotNull... paths
    ) {
        NullCheck.requireNonNull(parent, "parent");
        StringBuilder strPath = new StringBuilder();
        for (long path : paths) {
            strPath.append(IDENTIFIER_PATH_SEPARATOR)
                .append(path);
        }
        return parent.withSuffix(strPath.toString());
    }

    /**
     * Appends a float value as a path segment to an identifier.
     *
     * <p><b>Purpose:</b> Converts {@code path} to a string and appends it
     * to the identifier's path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier parent = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveSubPath(parent, 1.5F);
     * // result: example:oak_table/1.5
     * }</pre>
     *
     * @param parent the identifier whose path is to be suffixed;
     *               must not be {@code null}
     * @param path the float value to append
     * @return a new identifier containing the appended float value
     * @throws NullPointerException if {@code parent} is {@code null}
     * @throws IllegalArgumentException if the resulting identifier path
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveSubPath(
        @NotNull Identifier parent, float path
    ) {
        NullCheck.requireNonNull(parent, "parent");
        return parent
            .withSuffix(IDENTIFIER_PATH_SEPARATOR)
            .withSuffix(Float.toString(path));
    }

    /**
     * Appends float values as path segments to an identifier.
     *
     * <p><b>Purpose:</b> Converts each float value to a string, joins them
     * with {@code "/"}, and appends the result to the identifier's path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier parent = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveSubPath(
     *     parent,
     *     1.5F,
     *     2.5F
     * );
     * // Note: the current implementation may produce
     * // example:oak_table/1.5/2.5
     * }</pre>
     *
     * @param parent the identifier whose path is to be suffixed;
     *               must not be {@code null}
     * @param paths the float values to append, in order;
     *              the array must not be {@code null}
     * @return a new identifier containing the appended float values
     * @throws NullPointerException if {@code parent} or {@code paths} is
     *                              {@code null}
     * @throws IllegalArgumentException if the resulting identifier path
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveSubPath(
        @NotNull Identifier parent, float @NotNull... paths
    ) {
        NullCheck.requireNonNull(parent, "parent");
        StringBuilder strPath = new StringBuilder();
        for (float path : paths) {
            strPath.append(IDENTIFIER_PATH_SEPARATOR)
                .append(path);
        }
        return parent.withSuffix(strPath.toString());
    }

    /**
     * Appends a double value as a path segment to an identifier.
     *
     * <p><b>Purpose:</b> Converts {@code path} to a string and appends it
     * to the identifier's path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier parent = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveSubPath(parent, 1.5D);
     * // result: example:oak_table/1.5
     * }</pre>
     *
     * @param parent the identifier whose path is to be suffixed;
     *               must not be {@code null}
     * @param path the double value to append
     * @return a new identifier containing the appended double value
     * @throws NullPointerException if {@code parent} is {@code null}
     * @throws IllegalArgumentException if the resulting identifier path
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveSubPath(
        @NotNull Identifier parent, double path
    ) {
        NullCheck.requireNonNull(parent, "parent");
        return parent
            .withSuffix(IDENTIFIER_PATH_SEPARATOR)
            .withSuffix(Double.toString(path));
    }

    /**
     * Appends double values as path segments to an identifier.
     *
     * <p><b>Purpose:</b> Converts each double value to a string, joins
     * them with {@code "/"}, and appends the result to the identifier's
     * path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier parent = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveSubPath(
     *     parent,
     *     1.5D,
     *     2.5D
     * );
     * // Note: the current implementation may produce
     * // example:oak_table/1.5/2.5
     * }</pre>
     *
     * @param parent the identifier whose path is to be suffixed;
     *               must not be {@code null}
     * @param paths the double values to append, in order;
     *              the array must not be {@code null}
     * @return a new identifier containing the appended double values
     * @throws NullPointerException if {@code parent} or {@code paths} is
     *                              {@code null}
     * @throws IllegalArgumentException if the resulting identifier path
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveSubPath(
        @NotNull Identifier parent, double @NotNull... paths
    ) {
        NullCheck.requireNonNull(parent, "parent");
        StringBuilder strPath = new StringBuilder();
        for (double path : paths) {
            strPath.append(IDENTIFIER_PATH_SEPARATOR)
                .append(path);
        }
        return parent.withSuffix(strPath.toString());
    }

    /**
     * Appends a boolean value as a path segment to an identifier.
     *
     * <p><b>Purpose:</b> Converts {@code path} to a string and appends it
     * to the identifier's path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier parent = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveSubPath(parent, true);
     * // result: example:oak_table/true
     * }</pre>
     *
     * @param parent the identifier whose path is to be suffixed;
     *               must not be {@code null}
     * @param path the boolean value to append
     * @return a new identifier containing the appended boolean value
     * @throws NullPointerException if {@code parent} is {@code null}
     * @throws IllegalArgumentException if the resulting identifier path
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveSubPath(
        @NotNull Identifier parent, boolean path
    ) {
        NullCheck.requireNonNull(parent, "parent");
        return parent
            .withSuffix(IDENTIFIER_PATH_SEPARATOR)
            .withSuffix(Boolean.toString(path));
    }

    /**
     * Appends boolean values as path segments to an identifier.
     *
     * <p><b>Purpose:</b> Converts each boolean value to a string, joins
     * them with {@code "/"}, and appends the result to the identifier's
     * path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier parent = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveSubPath(
     *     parent,
     *     true,
     *     false
     * );
     * // Note: the current implementation may produce
     * // example:oak_table/true/false
     * }</pre>
     *
     * @param parent the identifier whose path is to be suffixed;
     *               must not be {@code null}
     * @param paths the boolean values to append, in order;
     *              the array must not be {@code null}
     * @return a new identifier containing the appended boolean values
     * @throws NullPointerException if {@code parent} or {@code paths} is
     *                              {@code null}
     * @throws IllegalArgumentException if the resulting identifier path
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveSubPath(
        @NotNull Identifier parent, boolean @NotNull... paths
    ) {
        NullCheck.requireNonNull(parent, "parent");
        StringBuilder strPath = new StringBuilder();
        for (boolean path : paths) {
            strPath.append(IDENTIFIER_PATH_SEPARATOR)
                .append(path);
        }
        return parent.withSuffix(strPath.toString());
    }

    /**
     * Appends a character value as a path segment to an identifier.
     *
     * <p><b>Purpose:</b> Converts {@code path} to a string and appends it
     * to the identifier's path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier parent = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveSubPath(parent, 'a');
     * // result: example:oak_table/a
     * }</pre>
     *
     * @param parent the identifier whose path is to be suffixed;
     *               must not be {@code null}
     * @param path the character value to append
     * @return a new identifier containing the appended character value
     * @throws NullPointerException if {@code parent} is {@code null}
     * @throws IllegalArgumentException if the resulting identifier path
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveSubPath(
        @NotNull Identifier parent, char path
    ) {
        NullCheck.requireNonNull(parent, "parent");
        return parent
            .withSuffix(IDENTIFIER_PATH_SEPARATOR)
            .withSuffix(Character.toString(path));
    }

    /**
     * Appends character values as path segments to an identifier.
     *
     * <p><b>Purpose:</b> Converts each character value to a string, joins
     * them with {@code "/"}, and appends the result to the identifier's
     * path.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Identifier parent = Identifier.fromNamespaceAndPath(
     *     "example",
     *     "oak_table"
     * );
     * Identifier result = IdentifierUtils.resolveSubPath(
     *     parent,
     *     'a',
     *     'b'
     * );
     * // Note: the current implementation may produce
     * // example:oak_table/a/b
     * }</pre>
     *
     * @param parent the identifier whose path is to be suffixed;
     *               must not be {@code null}
     * @param paths the character values to append, in order;
     *              the array must not be {@code null}
     * @return a new identifier containing the appended character values
     * @throws NullPointerException if {@code parent} or {@code paths} is
     *                              {@code null}
     * @throws IllegalArgumentException if the resulting identifier path
     *                                  is invalid
     */
    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveSubPath(
        @NotNull Identifier parent, char @NotNull... paths
    ) {
        NullCheck.requireNonNull(parent, "parent");
        StringBuilder strPath = new StringBuilder();
        for (char path : paths) {
            strPath.append(IDENTIFIER_PATH_SEPARATOR)
                .append(path);
        }
        return parent.withSuffix(strPath.toString());
    }
}