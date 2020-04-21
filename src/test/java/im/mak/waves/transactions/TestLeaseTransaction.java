package im.mak.waves.transactions;

import im.mak.waves.crypto.account.Address;
import im.mak.waves.crypto.account.PublicKey;
import im.mak.waves.crypto.base.Base58;
import im.mak.waves.crypto.base.Base64;
import im.mak.waves.transactions.common.Waves;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TestLeaseTransaction {

    @BeforeAll
    static void beforeAll() {
        Waves.ChainId = 'R';
    }

    @Test
    void protoV3Serialize() {
        Base64 originTxBodyBytes = new Base64("CFISII2Pso3AdXwKxUYtumBGAOwXiwd7VICSuiPRijFoYzd0GgQQoI0GIJyQm/OZLigD4gYaChYKFCeJyYXTXWfWhdwOmLovIW2Gvo+8EGQ=");
        Base64 originTxBytes = new Base64("ClAIUhIgjY+yjcB1fArFRi26YEYA7BeLB3tUgJK6I9GKMWhjN3QaBBCgjQYgnJCb85kuKAPiBhoKFgoUJ4nJhdNdZ9aF3A6Yui8hbYa+j7wQZBJAVjTJg2InL0or364uAkjG2TPNpb+ljusPjgNipnfrBwGkw3vqPuQD3OzANKIBTz35D1OTbwi8v49aWu72KZLmhQ==");
        String json = "{\"senderPublicKey\":\"AXbaBkJNocyrVpwqTzD4TpUY8fQ6eeRto9k1m2bNCzXV\",\"amount\":100,\"sender\":\"3MsX9C2MzzxE4ySF5aYcJoaiPfkyxZMg4cW\",\"feeAssetId\":null,\"chainId\":82,\"proofs\":[\"2ixyxSr8BUdxw4fSUT1BBAQT6QhD9mF1VBSZ91nC7fvhzfmuAtY67qnyNW15CY3So87wvxU9yi4Rb1dVbhU8Bp5a\"],\"fee\":100000,\"recipient\":\"3M4qwDomRabJKLZxuXhwfqLApQkU592nWxF\",\"id\":\"NFr9JJWkYH8gT6s8bELfusL5ksWKHu9SDGuZtoMptih\",\"type\":8,\"version\":3,\"timestamp\":1587500468252}";
        Base58 proof = new Base58("2ixyxSr8BUdxw4fSUT1BBAQT6QhD9mF1VBSZ91nC7fvhzfmuAtY67qnyNW15CY3So87wvxU9yi4Rb1dVbhU8Bp5a");

        PublicKey sender = PublicKey.as("AXbaBkJNocyrVpwqTzD4TpUY8fQ6eeRto9k1m2bNCzXV");
        Address recipient = sender.address(Waves.ChainId); // 3M4qwDomRabJKLZxuXhwfqLApQkU592nWxF

        LeaseTransaction tx = LeaseTransaction.builder()
                .recipient(recipient)
                .amount(100)
                .chainId(Waves.ChainId)
                .fee(LeaseTransaction.MIN_FEE)
                .timestamp(1587500468252L)
                .sender(sender)
                .proofs(singletonList(proof))
                .build();

        assertAll("check bytes",
                () -> assertThat(tx.bodyBytes()).isEqualTo(originTxBodyBytes),
                () -> assertThat(tx.bytes()).isEqualTo(originTxBytes)
        );
    }

}
