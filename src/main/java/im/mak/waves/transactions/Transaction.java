package im.mak.waves.transactions;

import com.google.protobuf.InvalidProtocolBufferException;
import im.mak.waves.crypto.account.PublicKey;
import im.mak.waves.transactions.common.Asset;
import im.mak.waves.transactions.common.Proof;
import im.mak.waves.transactions.common.WithBody;
import im.mak.waves.transactions.common.Waves;
import im.mak.waves.transactions.serializers.BinarySerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Transaction implements WithBody {

    private final int type; //TODO int, short, byte?
    private final int version; //TODO int, short, byte?
    private final byte chainId;
    private final PublicKey sender;
    private final long fee;
    private final Asset feeAsset;
    private final long timestamp;
    private final List<Proof> proofs;
    private byte[] bodyBytes;

    public static Transaction fromBytes(byte[] bytes) throws IOException {
        return BinarySerializer.fromBytes(bytes);
    }

    //TODO additional constructor for all children only with mandatory fields
    protected Transaction(int type, int version, byte chainId, PublicKey sender, long fee, Asset feeAsset, long timestamp, List<Proof> proofs) {
        this.type = type;
        this.version = version;
        this.chainId = chainId;
        this.sender = sender; //todo if null (genesisTx)?
        this.fee = fee;
        this.feeAsset = feeAsset;
        this.timestamp = timestamp;
        this.proofs = proofs == null ? Proof.emptyList() : new ArrayList<>(proofs);
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
        return proofs;
    }

    @Override
    public byte[] bodyBytes() {
        if (this.bodyBytes == null)
            this.bodyBytes = BinarySerializer.bodyBytes(this);
        return this.bodyBytes;
    }

    @Override
    public byte[] toBytes() {
        return BinarySerializer.toBytes(this);
    }

    //TODO implement clone in crypto lib
    //TODO this+children: hashCode, equals, toString
    //TODO basic validations in builder/constructor

    protected static abstract class TransactionBuilder
            <BUILDER extends TransactionBuilder<BUILDER, TX>, TX extends Transaction> {
        protected int version;
        protected byte chainId;
        protected PublicKey sender;
        protected long fee;
        protected Asset feeAsset;
        protected long timestamp;

        protected TransactionBuilder(int defaultVersion, long defaultFee) {
            this.version = defaultVersion;
            this.chainId = Waves.chainId;
            this.fee = defaultFee;
            this.feeAsset = Asset.WAVES;
        }

        private BUILDER builder() {
            return (BUILDER) this;
        }

        //todo how to hide from public
        public BUILDER version(int version) {
            this.version = version;
            return builder();
        }

        public BUILDER chainId(byte chainId) {
            this.chainId = chainId;
            return builder();
        }

        //todo require to set, at least sender
        public BUILDER sender(PublicKey publicKey) {
            this.sender = publicKey; //todo clone
            return builder();
        }

        public BUILDER fee(long fee) {
            this.fee = fee;
            return builder();
        }

        //todo how to hide from public?
        public BUILDER feeAsset(Asset asset) {
            this.feeAsset = asset;
            return builder();
        }

        public BUILDER timestamp(long timestamp) {
            this.timestamp = timestamp;
            return builder();
        }

        public TX get() {
            if (timestamp == 0)
                this.timestamp(System.currentTimeMillis());
            return _build();
        }

        protected abstract TX _build();
    }

}
