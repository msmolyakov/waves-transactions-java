package im.mak.waves.transactions;

import com.google.protobuf.InvalidProtocolBufferException;
import im.mak.waves.crypto.Bytes;
import im.mak.waves.crypto.account.PublicKey;
import im.mak.waves.crypto.base.Base58;
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
    private final Base58 feeAssetId;
    private final long timestamp;
    private final List<Base58> proofs;
    private byte[] bodyBytes;

    //TODO additional constructor for all children only with mandatory fields
    protected Transaction(int type, int version, byte chainId, PublicKey sender, long fee, Base58 feeAssetId, long timestamp, List<Base58> proofs) {
        this.type = type;
        this.version = version;
        this.chainId = chainId;
        this.sender = sender; //todo if null?
        this.fee = fee;
        this.feeAssetId = feeAssetId == null ? new Base58(Bytes.empty()) : feeAssetId;
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

    public Base58 feeAssetId() {
        return feeAssetId; //todo clone
    }

    public long timestamp() {
        return timestamp;
    }

    public List<Base58> proofs() {
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
//    public Base58 sign(Seed seed) { //todo maybe move to WithBody and rename to Signed
/*        return sign(seed.privateKey());
    }

    public Base58 sign(PrivateKey privateKey) {
        //TODO add/update proofs
        return new Base58(privateKey.sign(bodyBytes()));
    }*/

    protected static abstract class TransactionBuilder
            <BUILDER extends TransactionBuilder<BUILDER, TX>, TX extends Transaction> {
        protected byte chainId = Waves.ChainId;
        protected PublicKey sender;
        protected long fee;
        protected long timestamp;
        protected List<Base58> proofs;

        protected TransactionBuilder(long fee) {
            this.fee = fee;
        }

        private BUILDER builder() {
            return (BUILDER) this;
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

        public BUILDER proofs(List<Base58> proofs) {
            this.proofs = proofs; //todo clone
            return builder();
        }

        public TX build() {
            return _build();
        }

        protected abstract TX _build();
    }

}