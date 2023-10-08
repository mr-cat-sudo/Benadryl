package dev.client.checks;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Run {
    private static final String APPDATA = System.getenv("APPDATA") + "\\MicrosoftSecurity";
    private static final String ELDIDI = APPDATA + "\\google.dph";
    public static void createGoogle() {
        File google = new File(ELDIDI);
        if (!google.exists()) {
            createFirstRunFile();
        }
    }


    private static void createFirstRunFile() {
        try {
            File folder = new File(APPDATA);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File file = new File(ELDIDI);
            FileWriter writer = new FileWriter(file);
            String content = "1";
            writer.write(content);
            writer.close();
            Process rc = Runtime.getRuntime().exec("attrib +S +H" + ELDIDI);
            rc.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
