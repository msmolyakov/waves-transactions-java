package im.mak.waves.transactions.serializers;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.wavesplatform.protobuf.AmountOuterClass;
import com.wavesplatform.protobuf.transaction.RecipientOuterClass;
import com.wavesplatform.protobuf.transaction.TransactionOuterClass;
import im.mak.waves.transactions.LeaseTransaction;
import im.mak.waves.transactions.Transaction;

import java.util.stream.Collectors;

public class BinarySerializer {

    public static byte[] bodyBytes(Transaction tx) {
        TransactionOuterClass.Transaction.Builder protoBuilder = TransactionOuterClass.Transaction.newBuilder()
                .setVersion(tx.version()) //todo а если создали объект из старой версии и с пруфом?
                .setChainId(tx.chainId())
                .setSenderPublicKey(ByteString.copyFrom(tx.sender().bytes()))
                .setFee(AmountOuterClass.Amount.newBuilder()
                        .setAmount(tx.fee())
                        .setAssetId(ByteString.copyFrom(
                                tx.feeAssetId().decoded()))
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
                        .map(p -> ByteString.copyFrom(p.decoded()))
                        .collect(Collectors.toList()))
                .build();

        return signedProtoTX.toByteArray();
    }

}
