package im.mak.waves.transactions.serializers;

import com.google.protobuf.ByteString;
import com.wavesplatform.protobuf.AmountOuterClass;
import com.wavesplatform.protobuf.transaction.RecipientOuterClass;
import com.wavesplatform.protobuf.transaction.TransactionOuterClass;
import im.mak.waves.transactions.LeaseTransaction;
import im.mak.waves.transactions.Transaction;

import java.util.stream.Collectors;

public class ProtobufSerializer {

    public static TransactionOuterClass.SignedTransaction.Builder serialize(Transaction tx) {
        TransactionOuterClass.Transaction.Builder protoBuilder = TransactionOuterClass.Transaction.newBuilder()
                .setVersion(LeaseTransaction.VERSIONS[0]) //todo а если создали объект из старой версии и с пруфом?
                .setChainId(tx.chainId())
                .setSenderPublicKey(ByteString.copyFrom(tx.sender().bytes()))
                .setFee(AmountOuterClass.Amount.newBuilder()
                        .setAmount(tx.fee())
                        .setAssetId(ByteString.EMPTY)
                        .build())
                .setTimestamp(tx.timestamp());

        if (tx instanceof LeaseTransaction) {
            LeaseTransaction ltx = (LeaseTransaction) tx;
            protoBuilder.setLease(TransactionOuterClass.LeaseTransactionData.newBuilder()
                    .setRecipient(RecipientOuterClass.Recipient.newBuilder()
                            .setPublicKeyHash(ByteString.copyFrom(
                                    ltx.recipient().publicKeyHash()))
                            .build())
                    .setAmount(ltx.amount())
                    .build());
        } //todo other types

        return TransactionOuterClass.SignedTransaction.newBuilder()
                .setTransaction(protoBuilder.build())
                .addAllProofs(tx.proofs()
                        .stream()
                        .map(p -> ByteString.copyFrom(p.decoded()))
                        .collect(Collectors.toList()));
    }

}
