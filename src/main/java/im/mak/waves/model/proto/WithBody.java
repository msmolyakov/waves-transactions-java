package im.mak.waves.model.proto;

import im.mak.waves.crypto.base.Base58;
import im.mak.waves.crypto.base.Base64;

public interface WithBody {

    Base58 id(); //todo lazy new Base58(Hash.blake(bodyBytes())
    Base64 bodyBytes(); //todo lazy
    Base64 bytes();

}
