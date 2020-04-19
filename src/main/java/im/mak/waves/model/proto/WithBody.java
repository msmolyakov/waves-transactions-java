package im.mak.waves.model.proto;

import im.mak.waves.crypto.Hash;
import im.mak.waves.crypto.base.Base58;
import im.mak.waves.crypto.base.Base64;

public interface WithBody {

    default Base58 id() {
        return new Base58(Hash.blake(bodyBytes().decoded()));
    }

    Base64 bodyBytes();
    Base64 bytes();

}
