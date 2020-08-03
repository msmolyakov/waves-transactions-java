package im.mak.waves.transactions.account;

import im.mak.waves.crypto.Bytes;
import im.mak.waves.crypto.Crypto;
import im.mak.waves.crypto.Hash;
import im.mak.waves.crypto.base.Base58;
import im.mak.waves.transactions.common.Recipient;

import java.util.Arrays;

/**
 * Address is used as recipient of transactions.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Address implements Recipient {

    private static final int CHECKSUM_LENGTH = 4;
    private static final int PUBLIC_KEY_HASH_LENGTH = 20;

    public static final String PREFIX = "address:";
    public static final byte TYPE = 1;
    public static final int BYTES_LENGTH = 1 + 1 + PUBLIC_KEY_HASH_LENGTH + CHECKSUM_LENGTH;
    public static final int STRING_LENGTH = (int) Math.ceil(Math.log(256) / Math.log(58) * BYTES_LENGTH);

    /**
     * Generate an address from the public key.
     * Depends on the Id of a particular blockchain network.
     *
     * @param chainId blockchain network Id.
     * @return address
     */
    public static Address from(byte chainId, PublicKey publicKey) {
        return new Address(chainId, publicKey);
    }

    /**
     * Generate an address from the public key.
     * Depends on the Id of a particular blockchain network.
     *
     * @param chainId blockchain network Id.
     * @return address
     */
    public static Address fromPart(byte chainId, byte[] publicKeyHash) {
        return new Address(chainId, publicKeyHash);
    }

    /**
     * Create address instance from its base58 representation.
     *
     * @param base58Encoded address bytes as base58-encoded string
     * @return address instance
     * @throws IllegalArgumentException if base58 string is null
     */
    public static Address as(String base58Encoded) throws IllegalArgumentException {
        return new Address(base58Encoded);
    }

    /**
     * Create address instance from its bytes.
     *
     * @param bytes address bytes
     * @return address instance
     * @throws IllegalArgumentException if the address is wrong
     */
    public static Address as(byte[] bytes) throws IllegalArgumentException {
        return new Address(bytes);
    }

    /**
     * Check if the address is correct for specified Waves network.
     *
     * @param chainId blockchain network Id
     * @param address address as base58-encoded string
     * @return true if the address is correct
     */
    public static boolean isValid(byte chainId, String address) {
        try {
            return isValid(chainId, Base58.decode(address));
        } catch (IllegalArgumentException iae) {
            return false;
        }
    }

    /**
     * Check if the address is correct for any Waves network.
     *
     * @param address address as base58-encoded string
     * @return true if the address is correct
     */
    public static boolean isValid(String address) {
        try {
            return isValid(Base58.decode(address));
        } catch (IllegalArgumentException iae) {
            return false;
        }
    }

    /**
     * Check if the address is correct for specified Waves network.
     *
     * @param chainId      blockchain network Id
     * @param addressBytes address bytes
     * @return true if the address is correct
     */
    public static boolean isValid(byte chainId, byte[] addressBytes) {
        return isValid(addressBytes) && addressBytes[1] == chainId;
    }

    /**
     * Check if the address is correct for any Waves network.
     *
     * @param addressBytes address bytes
     * @return true if the address is correct
     */
    public static boolean isValid(byte[] addressBytes) {
        try {
            new Address(addressBytes);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    private final byte[] bytes;
    private final String encoded;

    /**
     * Generate an address from the public key.
     * Depends on the Id of a particular blockchain network.
     *
     * @param chainId blockchain network Id
     */
    public Address(byte chainId, PublicKey publicKey) {
        this(chainId, Crypto.getPublicKeyHash(publicKey.bytes()));
    }

    /**
     * Generate an address from a part of the public key.
     * Depends on the Id of a particular blockchain network.
     *
     * @param chainId blockchain network Id
     */
    public Address(byte chainId, byte[] publicKeyHash) {
        this.bytes = Crypto.getAddress(chainId, publicKeyHash);
        this.encoded = Base58.encode(this.bytes);
    }

    /**
     * Create address instance from its base58 representation.
     *
     * @param encodedAddress address bytes as base58-encoded string
     * @throws IllegalArgumentException if base58 string is null
     */
    public Address(String encodedAddress) throws IllegalArgumentException {
        this(Base58.decode(encodedAddress));
    }

    /**
     * Create address instance from its bytes.
     *
     * @param addressBytes address bytes
     * @throws IllegalArgumentException if the address is wrong
     */
    public Address(byte[] addressBytes) throws IllegalArgumentException {
        if (addressBytes.length != 26)
            throw new IllegalArgumentException("Address has wrong length. " +
                    "Expected: " + 26 + " bytes, actual: " + addressBytes.length + " bytes");
        if (addressBytes[0] != 1)
            throw new IllegalArgumentException("Address has unknown version " + addressBytes[0]);

        byte[][] parts = Bytes.chunk(addressBytes, 22, 4);
        byte[] checkSumPrefix = Bytes.chunk(Hash.secureHash(parts[0]), 4)[0];
        if (!Bytes.equal(parts[1], checkSumPrefix))
            throw new IllegalArgumentException(String.format(
                    "Address has wrong checksum base58:%s instead of base58:%s",
                    Base58.encode(parts[1]),
                    Base58.encode(checkSumPrefix)
            ));

        this.bytes = addressBytes;
        this.encoded = Base58.encode(this.bytes);
    }

    public byte type() {
        return TYPE;
    }

    /**
     * Extract blockchain network id from the address.
     *
     * @return network id
     */
    public byte chainId() {
        return this.bytes[1];
    }

    /**
     * Extract part of the public key hash from the address.
     *
     * @return public key hash
     */
    public byte[] publicKeyHash() {
        return Bytes.chunk(this.bytes(), 2, 20)[1];
    }

    /**
     * Extract checksum from the address.
     *
     * @return checksum
     */
    public byte[] checksum() {
        return Bytes.chunk(this.bytes(), 22)[1];
    }

    /**
     * Get bytes of the address.
     *
     * @return bytes of the address
     */
    public byte[] bytes() {
        return this.bytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Arrays.equals(bytes, address.bytes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }

    /**
     * Get the address encoded to base58.
     *
     * @return the base58-encoded address
     */
    @Override
    public String toString() {
        return this.encoded;
    }

}