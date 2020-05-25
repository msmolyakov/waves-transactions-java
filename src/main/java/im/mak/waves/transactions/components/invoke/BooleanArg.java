package im.mak.waves.transactions.components.invoke;

public class BooleanArg extends Arg {

    public static BooleanArg as(boolean value) {
        return new BooleanArg(value);
    }

    public BooleanArg(boolean value) {
        super(ArgType.BOOLEAN, value);
    }

    public boolean value() {
        return (boolean) super.valueAsObject();
    }

}
