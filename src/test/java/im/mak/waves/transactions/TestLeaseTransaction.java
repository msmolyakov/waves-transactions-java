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

    static PublicKey sender = PublicKey.as("AXbaBkJNocyrVpwqTzD4TpUY8fQ6eeRto9k1m2bNCzXV");
    static long timestamp = 1600000000000L;

    @BeforeAll
    static void beforeAll() {
        Waves.chainId = 'R';
    }

    static Stream<Arguments> transactionsProvider() {
        Recipient minAlias = Recipient.as(Alias.as("rich"));
        Recipient maxAlias = Recipient.as(Alias.as("_rich-account.with@30_symbols_"));
        Recipient address = Recipient.as(Address.from(sender, Waves.chainId)); // 3M4qwDomRabJKLZxuXhwfqLApQkU592nWxF
        return Stream.of(
                arguments(3, minAlias, 1, Proof.list(Proof.as("3mYdtrBDiizLESUQ88ZF3Bi9Xt8nM4FwYpDPVfSA5JMHHKs58ZirSG1eXuhwB2qPLQq8VXm7QRubHHuxiJXUcFq2")), TxId.id("65jwmmr3Yb3z2iNUMSUQuqF7PRPckhB8Z7MUgQiBXxLh"), Base64.decode("CFISII2Pso3AdXwKxUYtumBGAOwXiwd7VICSuiPRijFoYzd0GgQQoI0GIICAurvILigD4gYKCgYSBHJpY2gQAQ=="), Base64.decode("CkAIUhIgjY+yjcB1fArFRi26YEYA7BeLB3tUgJK6I9GKMWhjN3QaBBCgjQYggIC6u8guKAPiBgoKBhIEcmljaBABEkCKcsPrOA/Pp1aeI0uKaXwb4TFvAxoXOksJLboVftMLLZQ9NWadkYPh9pc3Q2O+467DsbmvnnFGrObYnQH6b5eB")),
                //{"senderPublicKey":"AXbaBkJNocyrVpwqTzD4TpUY8fQ6eeRto9k1m2bNCzXV","amount":1,"sender":"3MsX9C2MzzxE4ySF5aYcJoaiPfkyxZMg4cW","feeAssetId":null,"chainId":82,"proofs":["3mYdtrBDiizLESUQ88ZF3Bi9Xt8nM4FwYpDPVfSA5JMHHKs58ZirSG1eXuhwB2qPLQq8VXm7QRubHHuxiJXUcFq2"],"fee":100000,"recipient":"alias:R:rich","id":"65jwmmr3Yb3z2iNUMSUQuqF7PRPckhB8Z7MUgQiBXxLh","type":8,"version":3,"timestamp":1600000000000}
                arguments(3, maxAlias, Long.MAX_VALUE, Proof.list(Proof.as("3rAeZ2PVGxKMjHVfetxLxKeaTAmYCM5AddPzAQ5BhD6iia2d1snAT2pF5grZm7jHWuAFJ2ZxP9VyvzvPv4QwkprL")), TxId.id("5XXAMNKLxEy3a5Ews3cr4Vzaiw6UTuRUffQTmkDxwpbq"), Base64.decode("CFISII2Pso3AdXwKxUYtumBGAOwXiwd7VICSuiPRijFoYzd0GgQQoI0GIICAurvILigD4gYsCiASHl9yaWNoLWFjY291bnQud2l0aEAzMF9zeW1ib2xzXxD//////////38="), Base64.decode("CmIIUhIgjY+yjcB1fArFRi26YEYA7BeLB3tUgJK6I9GKMWhjN3QaBBCgjQYggIC6u8guKAPiBiwKIBIeX3JpY2gtYWNjb3VudC53aXRoQDMwX3N5bWJvbHNfEP//////////fxJAjm7kwi6bNKy5edF9kur+ApzwKVpZaWZvcSKRdG+SfnjSbVfR/i2Nf6hSiTRS3BUJz2LilsudZhhZbRGX8NFvgQ==")),
                //{"senderPublicKey":"AXbaBkJNocyrVpwqTzD4TpUY8fQ6eeRto9k1m2bNCzXV","amount":9223372036854775807,"sender":"3MsX9C2MzzxE4ySF5aYcJoaiPfkyxZMg4cW","feeAssetId":null,"chainId":82,"proofs":["3rAeZ2PVGxKMjHVfetxLxKeaTAmYCM5AddPzAQ5BhD6iia2d1snAT2pF5grZm7jHWuAFJ2ZxP9VyvzvPv4QwkprL"],"fee":100000,"recipient":"alias:R:_rich-account.with@30_symbols_","id":"5XXAMNKLxEy3a5Ews3cr4Vzaiw6UTuRUffQTmkDxwpbq","type":8,"version":3,"timestamp":1600000000000}
                arguments(3, address, Long.MAX_VALUE, Proof.list(Proof.as("4hZmtnVwFkj1N5ypgoxmFsvzvgmcr32ffRbuHRv9eWQ5ZeiVP8EoJeitACXHJvitENBHgrGcEtWhM6WRGqabgDLr"), Proof.as("3WtzGAGW2BmWRvJCVQEgKeUyCwpidGiLmnAHXFVzrVL7wkdztZJ8TnBeme643JMUCCC7hURvkt9gpa5NXu7wxXxX"), Proof.as("5qTG4chcuyKGVnYQksyYwz1dCPenqz4jcbo7e51oCkKJ3k19hhdR3UfLsLEJrUAH8oudDdAGV7NDyUcvUW3jCvb2"), Proof.as("3kGPckN5hLuGNvNxhc292WS32dek7pfQ8uUTJC4nfMRvzqyXAtqnrwFtnpZGkVZLXxrqBREaAySD5rpgAGLGcady"), Proof.as("62uijRo8Wo7Z1sBhNNcrhM2jeBLKsKuXpWZRumxD3Pqt4GVzcB5ChYe58chRbxMWcL5R5MReQz9SiBbm9t2XNpFr"), Proof.as("hJUpAhoCqJWhpQBGD5VGeG9wnzH5b8gF1QasFwUg7kTycBQ7qvo7n3qS37XiEi5aYEzxeNRkBARwVb7jNkFce4h"), Proof.as("59Ejnjny6tZ8NHuJfik7wciCNxE2QNwakAnfHrQEdk4cbpzHPHZa5oJ1t3ccj2RVD12kpFmDLMHrC49BxP3sb9Mq"), Proof.as("4M5UhsF2ACXVJV1BmrQg8hUoCdy4YS7XGWHJJxpZzUtMTJ1QUQKw7N99uKGUUPMwrfBG9aMfahvbdLycHhYtAWBr")), TxId.id("96peWN7hJLzTRiT68VLxgZtAnw6gHWzX7P9mqmM2B56E"), Base64.decode("CFISII2Pso3AdXwKxUYtumBGAOwXiwd7VICSuiPRijFoYzd0GgQQoI0GIICAurvILigD4gYiChYKFCeJyYXTXWfWhdwOmLovIW2Gvo+8EP//////////fw=="), Base64.decode("ClgIUhIgjY+yjcB1fArFRi26YEYA7BeLB3tUgJK6I9GKMWhjN3QaBBCgjQYggIC6u8guKAPiBiIKFgoUJ4nJhdNdZ9aF3A6Yui8hbYa+j7wQ//////////9/EkC5CGOuvm0XFw0/oBoHm9fk1mpDdqAKbOXJmnxjr8KvqEnO9O3ISmd7BA++F9PnNEB/rGiqS9IZlEV9+LFo+uuHEkB90MHoN3RtcrYQpu/oxfDmxCBDqD0twu9U0/Ir7eJEmjQzbX4l/gTGrdB1ePqB9mPscVlhEYO3q2iT9yXLTiGEEkDx2hAksYCIJw9ld/iMPpKv9goEASOZs3B08QdsmTYcQ2BGQKtZQsb8P7TFPVnOcW2ENHSbB9++eG73eoTcsQ+BEkCJWCm9diw2dzPIT5V6MrjdrdOgn6TmrCr/RL9pY0fet9qj0O/Yo+mI0VlOdxM2hbA/USeJZMN5TxXlTtPKmBmMEkD7uzAvsSrSTonbwRR5o4Uu8EfMoTDbY20m7w/77qJ59dlUaBvSL6PU0TV6aN6ZHD7GUxrkzGcAq6YOTMKxyWKBEkAiwR6UZBo9Ce4UKdltAOPIfKCS/i5KUJKQfsZR2HC572T3vADEyyZ8GtkUzsi4aBuJd5bVgW4UJUQ/ENmGyymCEkDPK9BGTfF8eGx7YBCvoMSj/nSkfSzseB6g3VxUO/Blgyfrru3/hca+eXPPqLnqlyOyw94JUsmnTkY42otnXhiIEkCnXWRF4gHSKVKyp/Q2rVSkaMWLJPCkGBpv/Ay1y6l45g/nfRSmbiq7hUWnAPKxklE7SAOqMkws7e11fCwAeSSB"))
                //{"senderPublicKey":"AXbaBkJNocyrVpwqTzD4TpUY8fQ6eeRto9k1m2bNCzXV","amount":9223372036854775807,"sender":"3M4qwDomRabJKLZxuXhwfqLApQkU592nWxF","feeAssetId":null,"chainId":82,"proofs":["4hZmtnVwFkj1N5ypgoxmFsvzvgmcr32ffRbuHRv9eWQ5ZeiVP8EoJeitACXHJvitENBHgrGcEtWhM6WRGqabgDLr","3WtzGAGW2BmWRvJCVQEgKeUyCwpidGiLmnAHXFVzrVL7wkdztZJ8TnBeme643JMUCCC7hURvkt9gpa5NXu7wxXxX","5qTG4chcuyKGVnYQksyYwz1dCPenqz4jcbo7e51oCkKJ3k19hhdR3UfLsLEJrUAH8oudDdAGV7NDyUcvUW3jCvb2","3kGPckN5hLuGNvNxhc292WS32dek7pfQ8uUTJC4nfMRvzqyXAtqnrwFtnpZGkVZLXxrqBREaAySD5rpgAGLGcady","62uijRo8Wo7Z1sBhNNcrhM2jeBLKsKuXpWZRumxD3Pqt4GVzcB5ChYe58chRbxMWcL5R5MReQz9SiBbm9t2XNpFr","hJUpAhoCqJWhpQBGD5VGeG9wnzH5b8gF1QasFwUg7kTycBQ7qvo7n3qS37XiEi5aYEzxeNRkBARwVb7jNkFce4h","59Ejnjny6tZ8NHuJfik7wciCNxE2QNwakAnfHrQEdk4cbpzHPHZa5oJ1t3ccj2RVD12kpFmDLMHrC49BxP3sb9Mq","4M5UhsF2ACXVJV1BmrQg8hUoCdy4YS7XGWHJJxpZzUtMTJ1QUQKw7N99uKGUUPMwrfBG9aMfahvbdLycHhYtAWBr"],"fee":100000,"recipient":"3M4qwDomRabJKLZxuXhwfqLApQkU592nWxF","id":"96peWN7hJLzTRiT68VLxgZtAnw6gHWzX7P9mqmM2B56E","type":8,"version":3,"timestamp":1600000000000}
        );
    }

    @ParameterizedTest(name = "{index}: v{0} to {1} of {2} wavelets")
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
                () -> assertThat(tx.bodyBytes()).isEqualTo(expectedBody),
                () -> assertThat(tx.toBytes()).isEqualTo(expectedBytes),
                () -> assertThat(tx.id()).isEqualTo(expectedId)
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

                () -> assertThat(deserTx.bodyBytes()).isEqualTo(expectedBody),
                () -> assertThat(deserTx.toBytes()).isEqualTo(expectedBytes),
                () -> assertThat(deserTx.id()).isEqualTo(expectedId)
        );
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
