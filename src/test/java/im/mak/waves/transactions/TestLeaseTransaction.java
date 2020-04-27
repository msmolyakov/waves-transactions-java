package im.mak.waves.transactions;

import im.mak.waves.crypto.Bytes;
import im.mak.waves.crypto.account.Address;
import im.mak.waves.crypto.account.PublicKey;
import im.mak.waves.crypto.base.Base64;
import im.mak.waves.transactions.common.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class TestLeaseTransaction {

    //todo version   v1, v2, v3
    //todo recipient address, alias
    //todo amount    min, max
    //todo proofs    0, 1, 8

    @ParameterizedTest(name = "{index} v{0} to {1} of {2} wavelets")
    @MethodSource("transactionsProvider")
    void leaseTransaction(int version, Recipient recipient, long amount, List<Proof> proofs, TxId expectedId, byte[] expectedBody, byte[] expectedBytes) throws IOException {
        LeaseTransaction tx = LeaseTransaction
                .builder(recipient, amount)
                .chainId(Waves.chainId)
                .fee(LeaseTransaction.MIN_FEE)
                .timestamp(timestamp)
                .sender(sender)
                .version(version)
                .get();
        proofs.forEach(p -> tx.proofs().add(p));

        assertAll("check bytes",
                () -> assertThat(tx.id()).isEqualTo(expectedId),
                () -> assertThat(tx.bodyBytes()).isEqualTo(expectedBody),
                () -> assertThat(tx.toBytes()).isEqualTo(expectedBytes)
        );

        LeaseTransaction deserTx = LeaseTransaction.fromBytes(tx.toBytes());

        assertAll("check bytes",
                () -> assertThat(deserTx).isEqualTo(tx),

                () -> assertThat(deserTx.recipient()).isEqualTo(recipient),
                () -> assertThat(deserTx.amount()).isEqualTo(amount),

                () -> assertThat(deserTx.version()).isEqualTo(version),
                () -> assertThat(deserTx.chainId()).isEqualTo(Waves.chainId),
                () -> assertThat(deserTx.sender()).isEqualTo(sender),
                () -> assertThat(deserTx.fee()).isEqualTo(tx.fee()),
                () -> assertThat(deserTx.feeAsset()).isEqualTo(tx.feeAsset()),
                () -> assertThat(deserTx.timestamp()).isEqualTo(tx.timestamp()),
                () -> assertThat(deserTx.proofs()).isEqualTo(tx.proofs()),

                () -> assertThat(deserTx.id()).isEqualTo(expectedId),
                () -> assertThat(deserTx.bodyBytes()).isEqualTo(expectedBody),
                () -> assertThat(deserTx.toBytes()).isEqualTo(expectedBytes)
        );
    }

    static Stream<Arguments> transactionsProvider() {
        return Stream.of(
                arguments(3, recipient, Long.MAX_VALUE, Proof.list(Proof.as("ZyV6cBzUFEBQ6wDyzj689KUBXSTahGeoec6GTonM9CJWc9LMEHXy7d51f4Mysk78zVnkrjuF539pvEJBV2uL4nP")), TxId.id("D2H9GTZ1F6fViJibjimJd62prJCh3WhXpwa8gkf3JTfd"), Base64.decode("CFISII2Pso3AdXwKxUYtumBGAOwXiwd7VICSuiPRijFoYzd0GgQQoI0GIJyQm/OZLigD4gYiChYKFCeJyYXTXWfWhdwOmLovIW2Gvo+8EP//////////fw=="), Base64.decode("ClgIUhIgjY+yjcB1fArFRi26YEYA7BeLB3tUgJK6I9GKMWhjN3QaBBCgjQYgnJCb85kuKAPiBiIKFgoUJ4nJhdNdZ9aF3A6Yui8hbYa+j7wQ//////////9/EkAcb3c/UoHj5Cm5xcJZ5cf3dHbfswtfxPpujeBJq5b3M6G6hAy3htBhujUcc3X613p020xfat3whx07IxGqDeaM"))
        );
    }

    static PublicKey sender = PublicKey.as("AXbaBkJNocyrVpwqTzD4TpUY8fQ6eeRto9k1m2bNCzXV");
    static Address address = sender.address(Waves.chainId); // 3M4qwDomRabJKLZxuXhwfqLApQkU592nWxF
    static Recipient recipient = Recipient.as(address);

    static long timestamp = 1600000000000L;

    //String json = "{\"senderPublicKey\":\"AXbaBkJNocyrVpwqTzD4TpUY8fQ6eeRto9k1m2bNCzXV\",\"amount\":9223372036854775807,\"sender\":\"3MsX9C2MzzxE4ySF5aYcJoaiPfkyxZMg4cW\",\"feeAssetId\":null,\"chainId\":82,\"proofs\":[\"ZyV6cBzUFEBQ6wDyzj689KUBXSTahGeoec6GTonM9CJWc9LMEHXy7d51f4Mysk78zVnkrjuF539pvEJBV2uL4nP\"],\"fee\":100000,\"recipient\":\"3M4qwDomRabJKLZxuXhwfqLApQkU592nWxF\",\"id\":\"D2H9GTZ1F6fViJibjimJd62prJCh3WhXpwa8gkf3JTfd\",\"type\":8,\"version\":3,\"timestamp\":1587500468252}";

    @BeforeAll
    static void beforeAll() {
        Waves.chainId = 'R';
    }

    /*@Test
    void protoV3_serializeWithoutProofs__equalToDescriptorPlusBodyBytes() {
        LeaseTransaction tx = LeaseTransaction
                .builder(recipient, maxAmount)
                .chainId(Waves.chainId)
                .fee(LeaseTransaction.MIN_FEE)
                .timestamp(timestamp)
                .sender(sender)
                .get();

        byte[] fieldNumberAndDescriptor = Bytes.of((byte) 10, (byte) 88);
        assertThat(tx.toBytes()).isEqualTo(Bytes.concat(
                fieldNumberAndDescriptor, tx.bodyBytes()));
        assertThat(tx.id()).isEqualTo(originId);
    }

    @Test
    void protoV3_withMinAlias__canSerialize() {
        byte[] expectedBody = Base64.decode("CFISII2Pso3AdXwKxUYtumBGAOwXiwd7VICSuiPRijFoYzd0GgQQoI0GIJyQm/OZLigD4gYSCgYSBHJpY2gQ//////////9/");
        byte[] expectedBytes = Base64.decode("CkgIUhIgjY+yjcB1fArFRi26YEYA7BeLB3tUgJK6I9GKMWhjN3QaBBCgjQYgnJCb85kuKAPiBhIKBhIEcmljaBD//////////38SQAbdYPn2WSjhB+bRw5VKYGFAWuePxZfOGVOfKzTE40pOfQBFgo6dOhwgQpC7IPs91tNc4rEk8O5TiYtQlZJCJYQ=");
        TxId expectedId = TxId.id("BeSJ2XNzRMz6eHFfZgPDAH62h7kLzQdjvq4nhqaS3jq7");
        String json = "{\"senderPublicKey\":\"AXbaBkJNocyrVpwqTzD4TpUY8fQ6eeRto9k1m2bNCzXV\",\"amount\":9223372036854775807,\"sender\":\"3MsX9C2MzzxE4ySF5aYcJoaiPfkyxZMg4cW\",\"feeAssetId\":null,\"chainId\":82,\"proofs\":[\"8xhqn7Mqk4tvgFAsskwX2UKtNLZrQG9RCXqBXLqR8M1KYs1ENktSZfphA3qoyctcpLVfauf3uu8Mg9J5VY3N8SP\"],\"fee\":100000,\"recipient\":\"alias:R:rich\",\"id\":\"BeSJ2XNzRMz6eHFfZgPDAH62h7kLzQdjvq4nhqaS3jq7\",\"type\":8,\"version\":3,\"timestamp\":1587500468252}";

        Alias minAlias = Alias.as("rich");
        Proof proof = new Proof("8xhqn7Mqk4tvgFAsskwX2UKtNLZrQG9RCXqBXLqR8M1KYs1ENktSZfphA3qoyctcpLVfauf3uu8Mg9J5VY3N8SP");

        LeaseTransaction tx = LeaseTransaction
                .builder(Recipient.as(minAlias), maxAmount)
                .chainId(Waves.chainId)
                .fee(LeaseTransaction.MIN_FEE)
                .timestamp(timestamp)
                .sender(sender)
                .get();
        tx.proofs().add(proof);

        assertAll("check bytes",
                () -> assertThat(tx.id()).isEqualTo(expectedId),
                () -> assertThat(tx.bodyBytes()).isEqualTo(expectedBody),
                () -> assertThat(tx.toBytes()).isEqualTo(expectedBytes)
        );
    }

    @Test
    void protoV3_withMaxAlias__canSerialize() {
        byte[] expectedBody = Base64.decode("CFISII2Pso3AdXwKxUYtumBGAOwXiwd7VICSuiPRijFoYzd0GgQQoI0GIJyQm/OZLigD4gYsCiASHl9yaWNoLWFjY291bnQud2l0aEAzMF9zeW1ib2xzXxD//////////38=");
        byte[] expectedBytes = Base64.decode("CmIIUhIgjY+yjcB1fArFRi26YEYA7BeLB3tUgJK6I9GKMWhjN3QaBBCgjQYgnJCb85kuKAPiBiwKIBIeX3JpY2gtYWNjb3VudC53aXRoQDMwX3N5bWJvbHNfEP//////////fxJAPsb1U87HeanfVNMhE8DPd7hSL6yfrUAnwMw2+VFmqXV6SZCKxUBeA01tsrIj2vfppQMPfRcHU/vq6XkVAI60iQ==");
        TxId expectedId = TxId.id("FExiWyMPAqgcMx7orpawu2BsWcNz5BWJtbZFdeuveFY1");
        String json = "{\"senderPublicKey\":\"AXbaBkJNocyrVpwqTzD4TpUY8fQ6eeRto9k1m2bNCzXV\",\"amount\":9223372036854775807,\"sender\":\"3MsX9C2MzzxE4ySF5aYcJoaiPfkyxZMg4cW\",\"feeAssetId\":null,\"chainId\":82,\"proofs\":[\"2FoDNbHLGXB5iyd23mG7cTRhYXPawSJJPftwFSsQA9jX89CX72EtkC8xqsXsQhHHrLTCSubR9rz5569KUwmCWCFS\"],\"fee\":100000,\"recipient\":\"alias:R:_rich-account.with@30_symbols_\",\"id\":\"FExiWyMPAqgcMx7orpawu2BsWcNz5BWJtbZFdeuveFY1\",\"type\":8,\"version\":3,\"timestamp\":1587500468252}";

        Alias maxAlias = Alias.as("_rich-account.with@30_symbols_");
        Proof proof = new Proof("2FoDNbHLGXB5iyd23mG7cTRhYXPawSJJPftwFSsQA9jX89CX72EtkC8xqsXsQhHHrLTCSubR9rz5569KUwmCWCFS");

        LeaseTransaction tx = LeaseTransaction
                .builder(Recipient.as(maxAlias), maxAmount)
                .chainId(Waves.chainId)
                .fee(LeaseTransaction.MIN_FEE)
                .timestamp(timestamp)
                .sender(sender)
                .get();
        tx.proofs().add(proof);

        assertAll("check bytes",
                () -> assertThat(tx.id()).isEqualTo(expectedId),
                () -> assertThat(tx.bodyBytes()).isEqualTo(expectedBody),
                () -> assertThat(tx.toBytes()).isEqualTo(expectedBytes)
        );
    }

    @Test
    void protoV3__canDeserialize() throws IOException {
        LeaseTransaction tx = LeaseTransaction.fromBytes(originTxBytes);

        assertAll("check tx fields",
                () -> assertThat(tx.id()).isEqualTo(originId),
                () -> assertThat(tx.type()).isEqualTo(LeaseTransaction.TYPE),
                () -> assertThat(tx.version()).isEqualTo(LeaseTransaction.LATEST_VERSION),
                () -> assertThat(tx.chainId()).isEqualTo(Waves.chainId),
                () -> assertThat(tx.sender()).isEqualTo(sender),
                () -> assertThat(tx.recipient()).isEqualTo(recipient),
                () -> assertThat(tx.amount()).isEqualTo(maxAmount),
                () -> assertThat(tx.fee()).isEqualTo(LeaseTransaction.MIN_FEE),
                () -> assertThat(tx.feeAsset()).isEqualTo(Asset.WAVES),
                () -> assertThat(tx.timestamp()).isEqualTo(timestamp),
                () -> assertThat(tx.proofs()).isEqualTo(Proof.list(proof)),
                () -> assertThat(tx.bodyBytes()).isEqualTo(originTxBodyBytes),
                () -> assertThat(tx.toBytes()).isEqualTo(originTxBytes)
        );
    }

    @Test
    void protoV3_withAlias__canDeserialize() throws IOException {
        byte[] expectedTxBodyBytes = Base64.decode("CFISII2Pso3AdXwKxUYtumBGAOwXiwd7VICSuiPRijFoYzd0GgQQoI0GIJyQm/OZLigD4gYsCiASHl9yaWNoLWFjY291bnQud2l0aEAzMF9zeW1ib2xzXxD//////////38=");
        byte[] expectedTxBytes = Base64.decode("CmIIUhIgjY+yjcB1fArFRi26YEYA7BeLB3tUgJK6I9GKMWhjN3QaBBCgjQYgnJCb85kuKAPiBiwKIBIeX3JpY2gtYWNjb3VudC53aXRoQDMwX3N5bWJvbHNfEP//////////fxJAPsb1U87HeanfVNMhE8DPd7hSL6yfrUAnwMw2+VFmqXV6SZCKxUBeA01tsrIj2vfppQMPfRcHU/vq6XkVAI60iQ==");
        TxId expectedId = TxId.id("FExiWyMPAqgcMx7orpawu2BsWcNz5BWJtbZFdeuveFY1");

        Alias maxAlias = Alias.as("_rich-account.with@30_symbols_");
        Proof proof = new Proof("2FoDNbHLGXB5iyd23mG7cTRhYXPawSJJPftwFSsQA9jX89CX72EtkC8xqsXsQhHHrLTCSubR9rz5569KUwmCWCFS");

        LeaseTransaction ltx = LeaseTransaction.fromBytes(expectedTxBytes);

        assertAll("check ltx fields",
                () -> assertThat(ltx.id()).isEqualTo(expectedId),
                () -> assertThat(ltx.type()).isEqualTo(LeaseTransaction.TYPE),
                () -> assertThat(ltx.version()).isEqualTo(LeaseTransaction.LATEST_VERSION),
                () -> assertThat(ltx.chainId()).isEqualTo(Waves.chainId),
                () -> assertThat(ltx.sender()).isEqualTo(sender),
                () -> assertThat(ltx.recipient()).isEqualTo(Recipient.as(maxAlias)),
                () -> assertThat(ltx.amount()).isEqualTo(maxAmount),
                () -> assertThat(ltx.fee()).isEqualTo(LeaseTransaction.MIN_FEE),
                () -> assertThat(ltx.feeAsset()).isEqualTo(Asset.WAVES),
                () -> assertThat(ltx.timestamp()).isEqualTo(timestamp),
                () -> assertThat(ltx.proofs()).isEqualTo(Proof.list(proof)),
                () -> assertThat(ltx.bodyBytes()).isEqualTo(expectedTxBodyBytes),
                () -> assertThat(ltx.toBytes()).isEqualTo(expectedTxBytes)
        );

        LeaseTransaction tx = (LeaseTransaction) Transaction.fromBytes(expectedTxBytes);

        assertAll("check tx fields",
                () -> assertThat(tx.id()).isEqualTo(expectedId),
                () -> assertThat(tx.type()).isEqualTo(LeaseTransaction.TYPE),
                () -> assertThat(tx.version()).isEqualTo(LeaseTransaction.LATEST_VERSION),
                () -> assertThat(tx.chainId()).isEqualTo(Waves.chainId),
                () -> assertThat(tx.sender()).isEqualTo(sender),
                () -> assertThat(tx.recipient()).isEqualTo(Recipient.as(maxAlias)),
                () -> assertThat(tx.amount()).isEqualTo(maxAmount),
                () -> assertThat(tx.fee()).isEqualTo(LeaseTransaction.MIN_FEE),
                () -> assertThat(tx.feeAsset()).isEqualTo(Asset.WAVES),
                () -> assertThat(tx.timestamp()).isEqualTo(timestamp),
                () -> assertThat(tx.proofs()).isEqualTo(Proof.list(proof)),
                () -> assertThat(tx.bodyBytes()).isEqualTo(expectedTxBodyBytes),
                () -> assertThat(tx.toBytes()).isEqualTo(expectedTxBytes)
        );
    }*/

}
