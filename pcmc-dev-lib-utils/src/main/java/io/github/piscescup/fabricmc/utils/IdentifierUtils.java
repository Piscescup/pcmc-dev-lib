package io.github.piscescup.fabricmc.utils;

import io.github.piscescup.util.validation.NullCheck;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public final class IdentifierUtils {
    public static final String IDENTIFIER_PATH_SEPARATOR =
        "/";

    private IdentifierUtils() {}

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

    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveSubPath(
        @NotNull Identifier parent, @NotNull String... paths
    ) {
        NullCheck.requireNonNull(parent, "parent");
        NullCheck.requireAllNonNull(paths, "paths");

        String finalPath = String.join(IDENTIFIER_PATH_SEPARATOR, paths);

        return resolveSubPath(parent, IDENTIFIER_PATH_SEPARATOR + finalPath);
    }

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

    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveSubPath(
        @NotNull Identifier parent, byte... paths
    ) {
        NullCheck.requireNonNull(parent, "parent");
        StringBuilder strPath = new StringBuilder();
        for (byte path : paths) {
            strPath.append(IDENTIFIER_PATH_SEPARATOR)
                .append(path);
        }
        return resolveSubPath(parent, strPath.toString());
    }

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

    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveSubPath(
        @NotNull Identifier parent, short... paths
    ) {
        NullCheck.requireNonNull(parent, "parent");
        StringBuilder strPath = new StringBuilder();
        for (short path : paths) {
            strPath.append(IDENTIFIER_PATH_SEPARATOR)
                .append(path);
        }
        return resolveSubPath(parent, strPath.toString());
    }

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

    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveSubPath(
        @NotNull Identifier parent, int... paths
    ) {
        NullCheck.requireNonNull(parent, "parent");
        StringBuilder strPath = new StringBuilder();
        for (int path : paths) {
            strPath.append(IDENTIFIER_PATH_SEPARATOR)
                .append(path);
        }
        return resolveSubPath(parent, strPath.toString());
    }

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

    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveSubPath(
        @NotNull Identifier parent, long... paths
    ) {
        NullCheck.requireNonNull(parent, "parent");
        StringBuilder strPath = new StringBuilder();
        for (long path : paths) {
            strPath.append(IDENTIFIER_PATH_SEPARATOR)
                .append(path);
        }
        return resolveSubPath(parent, strPath.toString());
    }

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

    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveSubPath(
        @NotNull Identifier parent, float... paths
    ) {
        NullCheck.requireNonNull(parent, "parent");
        StringBuilder strPath = new StringBuilder();
        for (float path : paths) {
            strPath.append(IDENTIFIER_PATH_SEPARATOR)
                .append(path);
        }
        return resolveSubPath(parent, strPath.toString());
    }

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

    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveSubPath(
        @NotNull Identifier parent, double... paths
    ) {
        NullCheck.requireNonNull(parent, "parent");
        StringBuilder strPath = new StringBuilder();
        for (double path : paths) {
            strPath.append(IDENTIFIER_PATH_SEPARATOR)
                .append(path);
        }
        return resolveSubPath(parent, strPath.toString());
    }

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

    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveSubPath(
        @NotNull Identifier parent, boolean... paths
    ) {
        NullCheck.requireNonNull(parent, "parent");
        StringBuilder strPath = new StringBuilder();
        for (boolean path : paths) {
            strPath.append(IDENTIFIER_PATH_SEPARATOR)
                .append(path);
        }
        return resolveSubPath(parent, strPath.toString());
    }

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

    @Contract("_, _ -> new")
    @NotNull
    public static Identifier resolveSubPath(
        @NotNull Identifier parent, char... paths
    ) {
        NullCheck.requireNonNull(parent, "parent");
        StringBuilder strPath = new StringBuilder();
        for (char path : paths) {
            strPath.append(IDENTIFIER_PATH_SEPARATOR)
                .append(path);
        }
        return resolveSubPath(parent, strPath.toString());
    }
}
