package im.mak.waves.transactions;

import im.mak.waves.transactions.common.ChainId;

public abstract class WavesConfig {

    private static byte chainId = ChainId.MAINNET;

    public static byte chainId() {
        return chainId;
    }

    public static void chainId(byte chainId) {
        WavesConfig.chainId = chainId;
    }

    public static void chainId(char chainId) {
        chainId((byte) chainId);
    }

}
