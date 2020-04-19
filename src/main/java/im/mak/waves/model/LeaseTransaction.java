package im.mak.waves.model;

import im.mak.waves.crypto.account.Address;
import im.mak.waves.crypto.account.PublicKey;
import im.mak.waves.crypto.base.Base58;
import im.mak.waves.model.common.Transaction;

import java.util.List;

public class LeaseTransaction extends Transaction {

    public static final int TYPE = 8;
    public static final int[] VERSIONS = new int[]{3, 2, 1};
    public static final long MIN_FEE = 100_000;

    private final Address recipient; //todo or alias
    private final long amount;

    public LeaseTransaction(Address recipient, long amount, byte chainId, PublicKey sender, long fee, long timestamp, List<Base58> proofs) {
        super(TYPE, VERSIONS[0], chainId, sender, fee, null, timestamp, proofs);

        this.recipient = recipient;
        this.amount = amount;
    }

    public static LeaseTransactionBuilder builder() {
        return new LeaseTransactionBuilder();
    }

    public Address recipient() {
        return recipient;
    }

    public long amount() {
        return amount;
    }

    public static class LeaseTransactionBuilder
            extends TransactionBuilder<LeaseTransactionBuilder, LeaseTransaction> {
        private Address recipient;
        private long amount;

        protected LeaseTransactionBuilder() {
            super(MIN_FEE);
        }

        public LeaseTransactionBuilder recipient(Address recipient) {
            this.recipient = recipient; //todo clone
            return this;
        }

        public LeaseTransactionBuilder amount(long amount) {
            this.amount = amount;
            return this;
        }

        protected LeaseTransaction buildInternal() {
            return new LeaseTransaction(recipient, amount, chainId, sender, fee, timestamp, proofs);
        }
    }

}