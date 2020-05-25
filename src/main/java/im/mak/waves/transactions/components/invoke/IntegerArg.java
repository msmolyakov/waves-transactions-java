package im.mak.waves.transactions.components.invoke;

public class IntegerArg extends Arg {

    public static IntegerArg as(long value) {
        return new IntegerArg(value);
    }

    public IntegerArg(long value) {
        super(ArgType.INTEGER, value);
    }

    public long value() {
        return (long) super.valueAsObject();
    }

}
