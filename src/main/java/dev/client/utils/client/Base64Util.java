package dev.client.utils.client;

import java.util.Base64;

public class Base64Util {
    public static String decode(String url) {
        byte[] decodedBytes = Base64.getDecoder().decode(url);
        return new String(decodedBytes);
    }
}