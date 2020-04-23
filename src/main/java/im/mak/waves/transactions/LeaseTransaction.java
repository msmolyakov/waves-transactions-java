package im.mak.waves.transactions;

import com.google.protobuf.InvalidProtocolBufferException;
import com.wavesplatform.protobuf.transaction.TransactionOuterClass;
import im.mak.waves.crypto.Bytes;
import im.mak.waves.crypto.Hash;
import im.mak.waves.crypto.account.Address;
import im.mak.waves.crypto.account.PublicKey;
import im.mak.waves.crypto.base.Base58;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class LeaseTransaction extends Transaction {

    //todo checkstyle custom checks
    public static final int TYPE = 8;
    public static final int[] VERSIONS = new int[]{3, 2, 1};
    public static final long MIN_FEE = 100_000;

    private final Address recipient; //todo or alias
    private final long amount;

    //todo move as universal method to Transaction or maybe interface
    public static LeaseTransaction from(byte[] bytes) throws InvalidProtocolBufferException {
        TransactionOuterClass.SignedTransaction signed = TransactionOuterClass.SignedTransaction.parseFrom(bytes);
        if (signed.hasTransaction() || !signed.getTransaction().hasLease())
            throw new InvalidProtocolBufferException("Parsed transaction is not a LeaseTransaction");

        TransactionOuterClass.Transaction tx = signed.getTransaction();
        TransactionOuterClass.LeaseTransactionData lease = tx.getLease();

        byte[] addressPart = Bytes.concat(
                Bytes.of((byte) 1, (byte) tx.getChainId()),
                lease.getRecipient().getPublicKeyHash().toByteArray());
        return builder()
                .version(tx.getVersion())
                .chainId((byte) tx.getChainId())
                .recipient(Address.as(Bytes.concat(
                        addressPart,
                        Bytes.chunk(Hash.secureHash(addressPart), 4)[0]
                ))) //todo what if alias?
                .amount(lease.getAmount())
                .sender(PublicKey.as(tx.getSenderPublicKey().toByteArray()))
                .fee(tx.getFee().getAmount()) //todo validate feeAssetId
                .timestamp(tx.getTimestamp())
                .proofs(signed.getProofsList().stream().map(p -> new Base58(p.toByteArray())).collect(toList()))
                .build();
    }

    public LeaseTransaction(Address recipient, long amount, byte chainId, PublicKey sender, long fee, long timestamp, List<Base58> proofs) {
        super(TYPE, VERSIONS[0], chainId, sender, fee, new Base58(Bytes.empty()), timestamp, proofs);

        this.recipient = recipient;
        this.amount = amount;
    }

    public static LeaseTransactionBuilder builder() {
        return new LeaseTransactionBuilder();
    }

    public Address recipient() {
        return recipient; //todo clone
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

        protected LeaseTransaction _build() {
            return new LeaseTransaction(recipient, amount, chainId, sender, fee, timestamp, proofs);
        }
    }

}
