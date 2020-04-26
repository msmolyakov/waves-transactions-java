package im.mak.waves.transactions;

import com.google.protobuf.InvalidProtocolBufferException;
import im.mak.waves.crypto.account.PublicKey;
import im.mak.waves.transactions.common.Asset;
import im.mak.waves.transactions.common.Proof;
import im.mak.waves.transactions.common.Recipient;

import java.util.List;

public class LeaseTransaction extends Transaction {

    //todo checkstyle custom checks
    public static final int TYPE = 8;
    public static final int[] VERSIONS = new int[]{3, 2, 1};
    public static final long MIN_FEE = 100_000;

    private final Recipient recipient;
    private final long amount;

    public LeaseTransaction(PublicKey sender, Recipient recipient, long amount, byte chainId, long fee, long timestamp, List<Proof> proofs) {
        super(TYPE, VERSIONS[0], chainId, sender, fee, Asset.WAVES, timestamp, proofs);

        this.recipient = recipient;
        this.amount = amount;
    }

    public static LeaseTransaction fromBytes(byte[] bytes) throws InvalidProtocolBufferException {
        return (LeaseTransaction) Transaction.fromBytes(bytes);
    }

    //todo rename each builder to "lease()", "transfer()" and etc?
    public static LeaseTransactionBuilder builder(Recipient recipient, long amount) {
        return new LeaseTransactionBuilder(recipient, amount);
    }

    public Recipient recipient() {
        return recipient; //todo clone
    }

    public long amount() {
        return amount;
    }

    public static class LeaseTransactionBuilder
            extends TransactionBuilder<LeaseTransactionBuilder, LeaseTransaction> {
        private final Recipient recipient;
        private final long amount;

        protected LeaseTransactionBuilder(Recipient recipient, long amount) {
            super(VERSIONS[0], MIN_FEE);
            this.recipient = recipient; //todo clone
            this.amount = amount;
        }

        protected LeaseTransaction _build() {
            return new LeaseTransaction(sender, recipient, amount, chainId, fee, timestamp, Proof.emptyList());
        }
    }

}
