package im.mak.waves.transactions;

import im.mak.waves.crypto.account.Address;
import im.mak.waves.crypto.account.PublicKey;
import im.mak.waves.crypto.base.Base58;
import im.mak.waves.crypto.base.Base64;
import im.mak.waves.transactions.common.Waves;
import im.mak.waves.transactions.serializers.ProtobufSerializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class TestLeaseTransaction {

    @BeforeAll
    static void beforeAll() {
        Waves.ChainId = 'R';
    }

    @Test
    void protoV3Serialize() {
        Base64 originTxBytes = new Base64("");
        Base58 proof = new Base58("");

        PublicKey sender = PublicKey.as("AXbaBkJNocyrVpwqTzD4TpUY8fQ6eeRto9k1m2bNCzXV");
        Address recipient = sender.address(Waves.ChainId); // 3M4qwDomRabJKLZxuXhwfqLApQkU592nWxF

        LeaseTransaction tx = LeaseTransaction.builder()
                .recipient(recipient)
                .amount(100)
                .chainId(Waves.ChainId)
                .fee(LeaseTransaction.MIN_FEE)
                .timestamp(1)
                .sender(sender)
                .proofs(singletonList(proof))
                .build();

        assertThat(tx.bytes()).isEqualTo(originTxBytes);
    }

}
