package im.mak.waves.transactions.common;

import im.mak.waves.crypto.Bytes;

import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Alias {

    public static final String PREFIX = "alias:";
    public static final byte TYPE = 2;
    public static final int MIN_LENGTH = 4;
    public static final int MAX_LENGTH = 30;
    public static final int BYTES_LENGTH = 1 + 1 + MAX_LENGTH;

    private static final String ALPHABET = "-.0-9@_a-z";

    private final byte chainId;
    private final String alias;

    public Alias(String alias) {
        this(Waves.chainId, alias);
    }

    public Alias(byte chainId, String value) {
        if (isValid(value, chainId)) {
            this.chainId = chainId;
            this.alias = value.replaceFirst("^" + PREFIX + (char) chainId + ":", "");
        } else throw new IllegalArgumentException("Alias must be " + MIN_LENGTH
                + " to " + MAX_LENGTH + " long of " + ALPHABET + " characters"
                + " and may have a prefix 'alias:" + (char) chainId + ":', but actual is '" + value + "'");
    }

    public static boolean isValid(String alias) {
        return isValid(alias, Waves.chainId);
    }

    public static boolean isValid(String alias, byte chainId) {
        String maybeAlias = alias.replaceFirst("^alias:" + (char) chainId + ":", "");

        return maybeAlias.matches("[" + ALPHABET + "]{" + MIN_LENGTH + "," + MAX_LENGTH + "}");
    }

    public static Alias as(String alias) {
        return new Alias(alias);
    }

    public static Alias as(byte chainId, String value) {
        return new Alias(chainId, value);
    }

    public byte chainId() {
        return chainId;
    }

    public String value() {
        return alias;
    }

    public byte[] bytes() {
        return Bytes.concat(Bytes.of((byte) 2, chainId()), Bytes.toSizedByteArray(alias.getBytes(UTF_8)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alias that = (Alias) o;
        return this.chainId == that.chainId &&
                Objects.equals(this.alias, that.alias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chainId, alias);
    }

    @Override
    public String toString() {
        return "alias:" + (char) chainId + ":" + alias;
    }
}
