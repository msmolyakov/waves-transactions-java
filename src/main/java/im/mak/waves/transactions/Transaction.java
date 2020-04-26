package im.mak.waves.transactions;

import com.google.protobuf.InvalidProtocolBufferException;
import im.mak.waves.crypto.account.PublicKey;
import im.mak.waves.crypto.base.Base58;
import im.mak.waves.transactions.common.Asset;
import im.mak.waves.transactions.common.Proof;
import im.mak.waves.transactions.common.Serialized;
import im.mak.waves.transactions.common.Waves;
import im.mak.waves.transactions.serializers.BinarySerializer;

import java.util.ArrayList;
import java.util.List;

public class Transaction implements Serialized {

    private final int type; //TODO int, short, byte?
    private final int version; //TODO int, short, byte? //TODO keep init version, use latest on sign
    private final byte chainId;
    private final PublicKey sender;
    private final long fee;
    private final Asset feeAsset;
    private final long timestamp;
    private final List<Proof> proofs;
    private byte[] bodyBytes;

    //TODO additional constructor for all children only with mandatory fields
    protected Transaction(int type, int version, byte chainId, PublicKey sender, long fee, Asset feeAsset, long timestamp, List<Proof> proofs) {
        this.type = type;
        this.version = version;
        this.chainId = chainId;
        this.sender = sender; //todo if null?
        this.fee = fee;
        this.feeAsset = feeAsset;
        this.timestamp = timestamp;
        this.proofs = proofs == null ? new ArrayList<>() : proofs;
    }

    public int type() {
        return type;
    }

    public int version() {
        return version;
    }

    public byte chainId() {
        return chainId;
    }

    public PublicKey sender() {
        return sender; //todo clone
    }

    public long fee() {
        return fee;
    }

    public Asset feeAsset() {
        return feeAsset; //todo clone
    }

    public long timestamp() {
        return timestamp;
    }

    public List<Proof> proofs() {
        return proofs; //todo clone
    }

    @Override
    public byte[] bodyBytes() {
        if (this.bodyBytes == null)
            this.bodyBytes = BinarySerializer.bodyBytes(this);
        return this.bodyBytes;
    }

    @Override
    public byte[] toBytes() throws InvalidProtocolBufferException {
        return BinarySerializer.bytes(this);
    }

    //TODO implement clone in crypto lib
    //TODO this+children: hashCode, equals, toString
    //TODO basic validations in builder/constructor
    //TODO typed fields? AssetId, Recipient, Proofs and etc

    protected static abstract class TransactionBuilder
            <BUILDER extends TransactionBuilder<BUILDER, TX>, TX extends Transaction> {
        protected int version;
        protected byte chainId = Waves.chainId;
        protected PublicKey sender;
        protected long fee;
        protected long timestamp;
        protected List<Proof> proofs;

        protected TransactionBuilder(long fee) {
            this.fee = fee;
        }

        private BUILDER builder() {
            return (BUILDER) this;
        }

        protected BUILDER version(int version) {
            this.version = version;
            return builder();
        }

        public BUILDER chainId(byte chainId) {
            this.chainId = chainId;
            return builder();
        }

        public BUILDER sender(PublicKey publicKey) {
            this.sender = publicKey; //todo clone
            return builder();
        }

        public BUILDER fee(long fee) {
            this.fee = fee;
            return builder();
        }

        public BUILDER timestamp(long timestamp) {
            this.timestamp = timestamp;
            return builder();
        }

        public BUILDER proofs(List<Proof> proofs) {
            this.proofs = proofs; //todo clone
            return builder();
        }

        public TX build() {
            return _build();
        }

        protected abstract TX _build();
    }

}
