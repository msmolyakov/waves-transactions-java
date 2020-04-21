package im.mak.waves.transactions;

import im.mak.waves.crypto.account.PublicKey;
import im.mak.waves.crypto.base.Base58;

import java.util.List;

public class LeaseCancelTransaction extends Transaction {

    public static final int TYPE = 9;
    public static final int[] VERSIONS = new int[]{3, 2, 1};
    public static final long MIN_FEE = 100_000;

    private final Base58 leaseId;

    public LeaseCancelTransaction(Base58 leaseId, byte chainId, PublicKey sender, long fee, long timestamp, List<Base58> proofs) {
        super(TYPE, VERSIONS[0], chainId, sender, fee, null, timestamp, proofs);

        this.leaseId = leaseId;
    }

    public static LeaseCancelTransactionBuilder builder() {
        return new LeaseCancelTransactionBuilder();
    }

    public Base58 leaseId() {
        return leaseId; //todo clone
    }

    //TODO hashCode, equals, toString

    public static class LeaseCancelTransactionBuilder
            extends TransactionBuilder<LeaseCancelTransactionBuilder, LeaseCancelTransaction> {
        private Base58 leaseId;

        protected LeaseCancelTransactionBuilder() {
            super(MIN_FEE);
        }

        public LeaseCancelTransactionBuilder leaseId(Base58 leaseId) {
            this.leaseId = leaseId; //todo clone
            return this;
        }

        protected LeaseCancelTransaction _build() {
            return new LeaseCancelTransaction(leaseId, chainId, sender, fee, timestamp, proofs);
        }
    }

}
