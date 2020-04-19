package im.mak.waves.model.proto;

import com.google.protobuf.ByteString;
import com.wavesplatform.protobuf.AmountOuterClass;
import com.wavesplatform.protobuf.transaction.RecipientOuterClass;
import com.wavesplatform.protobuf.transaction.TransactionOuterClass;
import im.mak.waves.model.LeaseTransaction;

import java.util.stream.Collectors;

public class ProtobufSerializer {

    public static TransactionOuterClass.SignedTransaction.Builder serialize(im.mak.waves.model.common.Transaction tx) {
        TransactionOuterClass.Transaction.Builder protoBuilder = TransactionOuterClass.Transaction.newBuilder()
                .setVersion(LeaseTransaction.VERSIONS[0]) //todo а если создали объект со старой версией и с пруфом?
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
                            .setPublicKeyHash() //todo PublicKeyHash??? А как адрес?
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
