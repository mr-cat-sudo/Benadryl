package dev.client.utils.files;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static dev.client.Client.*;

public class UploadUtil {

    public static String getServerUrl() {
        try {
            URL url = new URL("https://api.gofile.io/getServer");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
                reader.close();

                String response = responseBuilder.toString();
                JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
                serverUrl = jsonResponse.getAsJsonObject("data").get("server").getAsString();

                return serverUrl;

            } else {
                System.out.println("Error: " + responseCode);
            }

            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void uploadFile(File fileToUpload) {
        if (serverUrl == null) {
            serverUrl = UploadUtil.getServerUrl();
        }

        try {
            String boundary = "*****" + Long.toHexString(System.currentTimeMillis()) + "*****";
            URL url = new URL("https://" + serverUrl + ".gofile.io/uploadFile");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            try (OutputStream outputStream = connection.getOutputStream();
                 PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream))) {

                writer.append("--").append(boundary).append("\r\n");
                writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"").append(fileToUpload.getName()).append("\"\r\n");
                writer.append("Content-Type: application/octet-stream\r\n");
                writer.append("\r\n");
                writer.flush();

                try (FileInputStream fileInputStream = new FileInputStream(fileToUpload)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    outputStream.flush();
                }

                writer.append("\r\n").append("--").append(boundary).append("--").append("\r\n");
                writer.flush();
            }

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder responseBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseBuilder.append(line);
                    }
                    String response = responseBuilder.toString();
                    JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
                    JsonObject dataObject = jsonResponse.getAsJsonObject("data");
                    x100to = dataObject.get("downloadPage").getAsString();
                }
            } else {
                System.out.println("Error: " + responseCode);
            }

            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
