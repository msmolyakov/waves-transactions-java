package im.mak.waves.transactions.serializers;

import com.google.protobuf.ByteString;
import com.wavesplatform.protobuf.transaction.RecipientOuterClass;
import im.mak.waves.crypto.account.Address;
import im.mak.waves.transactions.common.Alias;
import im.mak.waves.transactions.common.Recipient;

public class ProtobufConverter {

    public static Recipient fromProto(RecipientOuterClass.Recipient proto, byte chainId) {
        if (proto.getRecipientCase().getNumber() == 1)
            return Recipient.as(Address.fromPart(proto.getPublicKeyHash().toByteArray(), chainId));
        else if (proto.getRecipientCase().getNumber() == 2) {
            return Recipient.as(Alias.as(chainId, proto.getAlias()));
        } else throw new IllegalArgumentException("Protobuf recipient must be specified");
    }

    public static RecipientOuterClass.Recipient toProto(Recipient recipient) {
        RecipientOuterClass.Recipient.Builder proto = RecipientOuterClass.Recipient.newBuilder();
        if (recipient.isAlias())
            proto.setAlias(recipient.alias().value());
        else
            proto.setPublicKeyHash(ByteString.copyFrom(
                    recipient.address().publicKeyHash()));
        return proto.build();
    }

}
