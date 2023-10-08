package dev.client.utils.files;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class EntryUtil {
    public static void addEntry(ZipOutputStream zos, String entryName, byte[] data) throws IOException {
        if (data == null || data.length < 128 || isEmptyString(data)) {return;}
        zos.putNextEntry(new ZipEntry(entryName));
        zos.write(data, 0, data.length);
        zos.closeEntry();
    }

    private static boolean isEmptyString(byte[] data) {
        String content = new String(data).trim();
        return content.isEmpty();
    }
}
