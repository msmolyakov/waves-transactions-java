package im.mak.waves.transactions.common;

import com.wavesplatform.protobuf.transaction.RecipientOuterClass;
import im.mak.waves.crypto.account.Address;

import java.util.Objects;

public class Recipient {
    private Address address;
    private Alias alias;

    public static Recipient as(Address address) {
        return new Recipient(address);
    }

    public static Recipient as(Alias alias) {
        return new Recipient(alias);
    }

    public Recipient(Address address) {
        this.address = address;
    }

    public Recipient(Alias alias) {
        this.alias = alias;
    }

    public boolean isAlias() {
        return this.address == null;
    }

    public Object value() {
        return this.isAlias() ? this.alias : this.address;
    }

    public Address address() {
        return this.address;
    }

    public Alias alias() {
        return this.alias;
    }

//todo
//    public RecipientOuterClass.Recipient toProto() {
//
//    }


    @Override
    public String toString() {
        return isAlias() ? alias.toString() : address.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipient other = (Recipient) o;
        return Objects.equals(this.address, other.address) &&
                Objects.equals(this.alias, other.alias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, alias);
    }
}
