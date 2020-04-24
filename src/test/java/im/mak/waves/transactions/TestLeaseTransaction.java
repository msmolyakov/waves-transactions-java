package im.mak.waves.transactions;

import com.google.protobuf.InvalidProtocolBufferException;
import im.mak.waves.crypto.Bytes;
import im.mak.waves.crypto.account.Address;
import im.mak.waves.crypto.account.PublicKey;
import im.mak.waves.crypto.base.Base58;
import im.mak.waves.crypto.base.Base64;
import im.mak.waves.transactions.common.Waves;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TestLeaseTransaction {

    byte[] originTxBodyBytes = Base64.decode("CFISII2Pso3AdXwKxUYtumBGAOwXiwd7VICSuiPRijFoYzd0GgQQoI0GIJyQm/OZLigD4gYiChYKFCeJyYXTXWfWhdwOmLovIW2Gvo+8EP//////////fw==");
    byte[] originTxBytes = Base64.decode("ClgIUhIgjY+yjcB1fArFRi26YEYA7BeLB3tUgJK6I9GKMWhjN3QaBBCgjQYgnJCb85kuKAPiBiIKFgoUJ4nJhdNdZ9aF3A6Yui8hbYa+j7wQ//////////9/EkAcb3c/UoHj5Cm5xcJZ5cf3dHbfswtfxPpujeBJq5b3M6G6hAy3htBhujUcc3X613p020xfat3whx07IxGqDeaM");
    byte[] originId = Base58.decode("D2H9GTZ1F6fViJibjimJd62prJCh3WhXpwa8gkf3JTfd");
    long maxAmount = Long.MAX_VALUE;
    long timestamp = 1587500468252L;
    Base58 proof = new Base58("ZyV6cBzUFEBQ6wDyzj689KUBXSTahGeoec6GTonM9CJWc9LMEHXy7d51f4Mysk78zVnkrjuF539pvEJBV2uL4nP");
    String json = "{\"senderPublicKey\":\"AXbaBkJNocyrVpwqTzD4TpUY8fQ6eeRto9k1m2bNCzXV\",\"amount\":9223372036854775807,\"sender\":\"3MsX9C2MzzxE4ySF5aYcJoaiPfkyxZMg4cW\",\"feeAssetId\":null,\"chainId\":82,\"proofs\":[\"ZyV6cBzUFEBQ6wDyzj689KUBXSTahGeoec6GTonM9CJWc9LMEHXy7d51f4Mysk78zVnkrjuF539pvEJBV2uL4nP\"],\"fee\":100000,\"recipient\":\"3M4qwDomRabJKLZxuXhwfqLApQkU592nWxF\",\"id\":\"D2H9GTZ1F6fViJibjimJd62prJCh3WhXpwa8gkf3JTfd\",\"type\":8,\"version\":3,\"timestamp\":1587500468252}";

    PublicKey sender = PublicKey.as("AXbaBkJNocyrVpwqTzD4TpUY8fQ6eeRto9k1m2bNCzXV");
    Address recipient = sender.address(Waves.chainId); // 3M4qwDomRabJKLZxuXhwfqLApQkU592nWxF

    @BeforeAll
    static void beforeAll() {
        Waves.chainId = 'R';
    }

    @Test
    void base64() {
        System.out.println(Base64.encode("Привет, мир!".getBytes(UTF_8)));
    }

    @Test
    void protoV3__canSerialize() {
        LeaseTransaction txSigned = LeaseTransaction.builder()
                .recipient(recipient)
                .amount(maxAmount)
                .chainId(Waves.chainId)
                .fee(LeaseTransaction.MIN_FEE)
                .timestamp(timestamp)
                .sender(sender)
                .proofs(singletonList(proof))
                .build();

        LeaseTransaction txSignedAfterCreation = LeaseTransaction.builder()
                .recipient(recipient)
                .amount(maxAmount)
                .chainId(Waves.chainId)
                .fee(LeaseTransaction.MIN_FEE)
                .timestamp(timestamp)
                .sender(sender)
                .build();
        txSignedAfterCreation.proofs().add(proof);

        assertAll("check bytes",
                () -> assertThat(txSigned.id()).isEqualTo(originId),
                () -> assertThat(txSigned.bodyBytes()).isEqualTo(originTxBodyBytes),
                () -> assertThat(txSigned.toBytes()).isEqualTo(originTxBytes),
                () -> assertThat(txSigned.id()).isEqualTo(txSignedAfterCreation.id()),
                () -> assertThat(txSigned.bodyBytes()).isEqualTo(txSignedAfterCreation.bodyBytes()),
                () -> assertThat(txSigned.toBytes()).isEqualTo(txSignedAfterCreation.toBytes())
        );
    }

    @Disabled
    @Test
    void protoV3_withAlias__canSerialize() {
        LeaseTransaction tx = LeaseTransaction.builder()
                .recipient(recipient) //todo alias
                .amount(maxAmount)
                .chainId(Waves.chainId)
                .fee(LeaseTransaction.MIN_FEE)
                .timestamp(timestamp)
                .sender(sender)
                .proofs(singletonList(proof))
                .build();

        assertAll("check bytes",
                () -> assertThat(tx.id()).isEqualTo(originId),
                () -> assertThat(tx.bodyBytes()).isEqualTo(originTxBodyBytes),
                () -> assertThat(tx.toBytes()).isEqualTo(originTxBytes)
        );
    }

    @Test
    void protoV3__serialize__canUpdateProofs() {
        LeaseTransaction tx = LeaseTransaction.builder()
                .recipient(recipient)
                .amount(maxAmount)
                .chainId(Waves.chainId)
                .fee(LeaseTransaction.MIN_FEE)
                .timestamp(timestamp)
                .sender(sender)
                .proofs(new ArrayList<>(singletonList(proof)))
                .build();
        tx.proofs().add(proof);

        assertAll("check bytes",
                () -> assertThat(tx.id()).isEqualTo(originId),
                () -> assertThat(tx.bodyBytes()).isEqualTo(originTxBodyBytes),
                () -> assertThat(tx.toBytes()).isNotEqualTo(originTxBytes)
        );
    }

    @Test
    void protoV3_serializeWithoutProofs__equalToDescriptorPlusBodyBytes() throws InvalidProtocolBufferException {
        LeaseTransaction tx = LeaseTransaction.builder()
                .recipient(recipient)
                .amount(maxAmount)
                .chainId(Waves.chainId)
                .fee(LeaseTransaction.MIN_FEE)
                .timestamp(timestamp)
                .sender(sender)
                .build();

        byte[] fieldNumberAndDescriptor = Bytes.of((byte) 10, (byte) 88);
        assertThat(tx.toBytes()).isEqualTo(Bytes.concat(
                fieldNumberAndDescriptor, tx.bodyBytes()));
        assertThat(tx.id()).isEqualTo(originId);
    }

    @Test
    void protoV3__canDeserialize() throws InvalidProtocolBufferException {
        LeaseTransaction tx = LeaseTransaction.from(originTxBytes);

        assertAll("check tx fields",
                () -> assertThat(tx.id()).isEqualTo(originId),
                () -> assertThat(tx.type()).isEqualTo(LeaseTransaction.TYPE),
                () -> assertThat(tx.version()).isEqualTo(LeaseTransaction.VERSIONS[0]),
                () -> assertThat(tx.chainId()).isEqualTo(Waves.chainId),
                () -> assertThat(tx.sender()).isEqualTo(sender),
                () -> assertThat(tx.recipient()).isEqualTo(recipient),
                () -> assertThat(tx.amount()).isEqualTo(maxAmount),
                () -> assertThat(tx.fee()).isEqualTo(LeaseTransaction.MIN_FEE),
                () -> assertThat(tx.feeAssetId()).isEqualTo(new Base58(Bytes.empty())),
                () -> assertThat(tx.timestamp()).isEqualTo(timestamp),
                () -> assertThat(tx.proofs()).isEqualTo(singletonList(proof))
        );
    }

}
