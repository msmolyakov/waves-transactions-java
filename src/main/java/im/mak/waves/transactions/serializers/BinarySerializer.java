package im.mak.waves.transactions.serializers;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.wavesplatform.protobuf.AmountOuterClass;
import com.wavesplatform.protobuf.transaction.RecipientOuterClass;
import com.wavesplatform.protobuf.transaction.TransactionOuterClass;
import im.mak.waves.crypto.account.PublicKey;
import im.mak.waves.transactions.LeaseTransaction;
import im.mak.waves.transactions.Transaction;
import im.mak.waves.transactions.common.Asset;
import im.mak.waves.transactions.common.Proof;

import java.util.stream.Collectors;

import static im.mak.waves.transactions.serializers.ProtobufConverter.fromProto;

public class BinarySerializer {

    public static byte[] bodyBytes(Transaction tx) {
        TransactionOuterClass.Transaction.Builder protoBuilder = TransactionOuterClass.Transaction.newBuilder()
                .setVersion(tx.version()) //todo а если создали объект из старой версии и с пруфом?
                .setChainId(tx.chainId())
                .setSenderPublicKey(ByteString.copyFrom(tx.sender().bytes()))
                .setFee(AmountOuterClass.Amount.newBuilder()
                        .setAmount(tx.fee())
                        .setAssetId(ByteString.copyFrom(
                                tx.feeAsset().bytes()))
                        .build())
                .setTimestamp(tx.timestamp());

        if (tx instanceof LeaseTransaction) {
            LeaseTransaction ltx = (LeaseTransaction) tx;
            RecipientOuterClass.Recipient recipient = ltx.recipient().isAlias()
                    ? RecipientOuterClass.Recipient.newBuilder().setAlias(ltx.recipient().alias().value()).build()
                    : RecipientOuterClass.Recipient.newBuilder().setPublicKeyHash(ByteString.copyFrom(
                            ltx.recipient().address().publicKeyHash())).build();
            protoBuilder.setLease(TransactionOuterClass.LeaseTransactionData.newBuilder()
                    .setRecipient(recipient)
                    .setAmount(ltx.amount())
                    .build());
        } //todo other types

        return protoBuilder.build().toByteArray();
    }

    public static byte[] bytes(Transaction tx) throws InvalidProtocolBufferException {
        TransactionOuterClass.Transaction protoTx = TransactionOuterClass.Transaction.parseFrom(tx.bodyBytes());

        TransactionOuterClass.SignedTransaction signedProtoTX = TransactionOuterClass.SignedTransaction.newBuilder()
                .setTransaction(protoTx)
                .addAllProofs(tx.proofs()
                        .stream()
                        .map(p -> ByteString.copyFrom(p.bytes()))
                        .collect(Collectors.toList()))
                .build();

        return signedProtoTX.toByteArray();
    }

    public static Transaction fromBytes(byte[] bytes) throws InvalidProtocolBufferException {
        TransactionOuterClass.SignedTransaction signed = TransactionOuterClass.SignedTransaction.parseFrom(bytes);
        if (!signed.hasTransaction())
            throw new InvalidProtocolBufferException("Parsed bytes are not a Transaction");

        TransactionOuterClass.Transaction tx = signed.getTransaction();

        //todo other types
        if (tx.hasLease()) {
            TransactionOuterClass.LeaseTransactionData lease = tx.getLease();
            LeaseTransaction ltx = LeaseTransaction
                    .builder(fromProto(lease.getRecipient(), (byte) tx.getChainId()), lease.getAmount())
                    .version(tx.getVersion())
                    .chainId((byte) tx.getChainId())
                    .sender(PublicKey.as(tx.getSenderPublicKey().toByteArray()))
                    .fee(tx.getFee().getAmount())
                    .feeAsset(Asset.id(tx.getFee().getAssetId().toByteArray()))
                    .timestamp(tx.getTimestamp())
                    .get();
            signed.getProofsList().forEach(p -> ltx.proofs().add(Proof.proof(p.toByteArray())));
            return ltx;
        } else throw new InvalidProtocolBufferException("Can't recognize transaction type");
    }

}
