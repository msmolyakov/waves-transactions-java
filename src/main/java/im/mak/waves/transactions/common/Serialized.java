package im.mak.waves.transactions.common;

import com.google.protobuf.InvalidProtocolBufferException;
import im.mak.waves.crypto.Hash;

public interface Serialized {

    default byte[] id() {
        return Hash.blake(bodyBytes());
    }

    byte[] bodyBytes();
    byte[] toBytes() throws InvalidProtocolBufferException;

}
