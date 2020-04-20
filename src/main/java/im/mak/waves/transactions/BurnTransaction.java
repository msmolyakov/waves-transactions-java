package im.mak.waves.transactions;

import im.mak.waves.crypto.Bytes;
import im.mak.waves.crypto.base.Base58;
import im.mak.waves.transactions.common.Chained;

public class BurnTransaction extends Transaction implements Chained {

    public static final int TYPE = 6;

    //TODO static
    //TODO fromBytes(bodyBytes)

    private final Base58 assetId;
    private final long quantity;
    private final byte chainId;

    public BurnTransaction(Base58 assetId, long quantity, long fee, long timestamp, byte chainId) {
        this(assetId, quantity, fee, timestamp, chainId, new Base58[0], new Base58(Bytes.empty()));
    }

    public BurnTransaction(Base58 assetId, long quantity, long fee, long timestamp, byte chainId, Base58[] proofs) {
        this(assetId, quantity, fee, timestamp, chainId, proofs, new Base58(Bytes.empty()));
    }

    public BurnTransaction(Base58 assetId, long quantity, long fee, long timestamp, byte chainId, Base58 id) {
        this(assetId, quantity, fee, timestamp, chainId, new Base58[0], id);
    }

    public BurnTransaction(Base58 assetId, long quantity, long fee, long timestamp, byte chainId, Base58[] proofs, Base58 id) {
        super(TYPE, fee, new Base58(Bytes.empty()), timestamp, proofs, id);
        this.assetId = assetId;
        this.quantity = quantity;
        this.chainId = chainId;
    }

    public Base58 assetId() {
        return assetId;
    }

    public long quantity() {
        return quantity;
    }

    @Override
    public byte chainId() {
        return chainId;
    }

    @Override
    public byte[] bodyBytes() {
        return null;
    }

    //TODO hashCode, equals, toString

}
