package im.mak.waves.model.proto;

import com.google.protobuf.InvalidProtocolBufferException;
import com.wavesplatform.protobuf.transaction.TransactionOuterClass;
import com.wavesplatform.protobuf.transaction.TransactionOuterClass.*;
import im.mak.waves.crypto.account.PublicKey;
import im.mak.waves.crypto.base.Base58;
import im.mak.waves.model.GenesisTransaction;
import im.mak.waves.model.LeaseTransaction;
import im.mak.waves.model.Order;
import im.mak.waves.model.components.Arg;
import im.mak.waves.model.components.DataEntry;
import im.mak.waves.model.components.Payment;
import im.mak.waves.model.components.Transfer;

public class Transaction {

    private byte type = 0;
    private byte version = 0;
    private long fee = 0;
    private Base58 feeAssetId = null;
    private PublicKey senderPublicKey = null;
    private byte chainId = 0;
    private long timestamp = 0;
    private Base58[] proofs; // signature (json)

    private String name = null;
    private String description = null;
    private long quantity = 0;
    private byte decimals = 0;
    private Boolean reissuable = null;

    private Base58 assetId = null;
    private byte[] attachment = null;

    private Base58 recipient = null;
    private long amount = -1;

    private long price = -1;
    private Order buyOrder = null;
    private Order sellOrder = null;
    private long buyMatcherFee = -1;
    private long sellMatcherFee = -1;

    private Base58 leaseId = null;

    private String alias = null;

    private Transfer[] transfers = null;

    private DataEntry[] data = null;

    private long minSponsoredAssetFee = 0;

    private byte[] script = null;

    private Base58 dApp = null;
    private String function = null;
    private Arg[] args = null;
    private Payment[] payments = null; // payment



    //из байт (протобаф, легаси)

    //в байты (протобаф)

    //из json

    //в json

    public static Transaction fromBytes(byte[] bytes) {
        //todo подписанная?
        //todo сохранять версию или переопределять на новую?
        try {
            return parseProtobuf(bytes);
        } catch (InvalidProtocolBufferException e) {
            return parseLegacy(bytes);
            //todo or e.printStackTrace();
        }
    }

    //public T as(T type) {}

    private static Transaction parseProtobuf(byte[] protoBytes) throws InvalidProtocolBufferException {
        TransactionOuterClass.Transaction tx = TransactionOuterClass.Transaction.parseFrom(protoBytes);
        if (tx.hasGenesis()) return (Transaction) new GenesisTransaction();
//            else if (tx.hasPayment()) return (Transaction) new PaymentTransaction();
//            else if (tx.hasIssue()) return (Transaction) new IssueTransaction();
//            else if (tx.hasTransfer()) return (Transaction) new TransferTransaction();
//            else if (tx.hasReissue()) return (Transaction) new ReissueTransaction();
//            else if (tx.hasBurn()) return (Transaction) new BurnTransaction();
//            else if (tx.hasExchange()) return (Transaction) new ExchangeTransaction();
            else if (tx.hasLease()) return (Transaction) new LeaseTransaction();
//            else if (tx.hasLeaseCancel()) return (Transaction) new LeaseCancelTransaction();
//            else if (tx.hasCreateAlias()) return (Transaction) new CreateAliasTransaction();
//            else if (tx.hasMassTransfer()) return (Transaction) new MassTransferTransaction();
//            else if (tx.hasDataTransaction()) return (Transaction) new DataTransaction();
//            else if (tx.hasSetScript()) return (Transaction) new SetScriptTransaction();
//            else if (tx.hasSponsorFee()) return (Transaction) new SponsorFeeTransaction();
//            else if (tx.hasSetAssetScript()) return (Transaction) new SetAssetScriptTransaction();
//            else if (tx.hasInvokeScript()) return (Transaction) new InvokeScriptTransaction();
//            else if (tx.hasUpdateAssetInfo()) return (Transaction) new UpdateAssetInfoTransaction();
        else throw new RuntimeException(); //todo
    }

    private static Transaction parseLegacy(byte[] legacyBytes) {

    }

}
