package im.mak.waves.transactions.serializers;

import im.mak.waves.crypto.Bytes;
import im.mak.waves.crypto.Bytes.ByteReader;
import im.mak.waves.crypto.account.Address;
import im.mak.waves.crypto.account.PublicKey;
import im.mak.waves.transactions.LeaseTransaction;
import im.mak.waves.transactions.Transaction;
import im.mak.waves.transactions.common.Alias;
import im.mak.waves.transactions.common.Proof;
import im.mak.waves.transactions.common.Recipient;

import java.io.IOException;
import java.util.List;

import static im.mak.waves.crypto.Bytes.concat;
import static im.mak.waves.crypto.Bytes.of;

public class LegacyBinarySerializer {

//    todo
    public static byte[] bodyBytes(Transaction tx) {
        return Bytes.empty();
    }

    public static byte[] bytes(Transaction tx) {
        return Bytes.empty();
    }

    public static Transaction fromBytes(byte[] bytes) throws IOException {
        if (bytes.length < 4)
            throw new IOException("Byte array in too short to parse a transaction");
        boolean withProofs = bytes[0] == 0;
        int index = withProofs ? 1 : 0;

        int type = bytes[index];
        int version = withProofs ? bytes[index + 1] : 1;
        byte[] data = Bytes.chunk(bytes, withProofs ? 3 : 1)[1];

        Transaction transaction;
        ByteReader reader = new ByteReader(data);
        if (type == 1) throw new IOException("Genesis transactions are not supported"); //todo
        else if (type == 2) throw new IOException("Payment transactions are not supported"); //todo
        else if (type == LeaseTransaction.TYPE) transaction = lease(reader, version, withProofs);
        else throw new IOException("Unknown transaction type " + type);

        if (reader.hasNext())
            throw new IOException("The size of " + bytes.length
                    + " bytes is " + (bytes.length - reader.rest())
                    + " greater than expected for type " + type + " and version " + version + " of the transaction");

        return transaction;
    }

    private static LeaseTransaction lease(ByteReader data, int version, boolean withProofs) throws IOException {
        if (withProofs && data.read() != 0) throw new IOException("Reserved field must be 0");
        PublicKey sender = PublicKey.as(data.read(32)); //todo PublicKey.LENGTH
        Recipient recipient = readRecipient(data);
        long amount = data.readLong();
        long fee = data.readLong();
        long timestamp = data.readLong();
        List<Proof> proofs = readProofs(data, withProofs);
        LeaseTransaction tx = LeaseTransaction
                .builder(recipient, amount)
                .version(version)
                .chainId(recipient.chainId())
                .sender(sender)
                .fee(fee)
                .timestamp(timestamp)
                .get();
        proofs.forEach(p -> tx.proofs().add(p)); //todo `Proofs extends List` or move proofs to builder
        return tx;
    }

    private static Recipient readRecipient(ByteReader data) throws IOException {
        byte recipientType = data.read(); //todo Recipient.from(bytes) or Alias.from(bytes)
        if (recipientType == 1)
            return Recipient.as(Address.as(concat(of(recipientType), data.read(25)))); //todo Address.LENGTH
        else if (recipientType == 2) {
            return Recipient.as(Alias.as(data.read(), new String(data.readArray()))); //todo Alias.as(bytes)
        } else throw new IOException("Unknown recipient type");
    }

    private static List<Proof> readProofs(ByteReader data, boolean withProofs) throws IOException {
        if (withProofs) {
            byte version = data.read(); //todo Proofs.VERSION = 1
            if (version != 1) throw new IOException("Wrong proofs version " + version + " but " + 1 + " expected");
            ByteReader proofs = new ByteReader(data.readArray());
            List<Proof> result = Proof.emptyList();
            while (proofs.hasNext())
                result.add(Proof.as(proofs.readArray()));
            return result;
        } else {
            return Proof.list(Proof.as(data.read(64)));
        }
    }

}
