package dev.client.utils.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FolderUtil {
    public static void addFolder(ZipOutputStream zos, String folderName, File folderToAdd) throws IOException {
        if (folderToAdd == null || !folderToAdd.exists() || !folderToAdd.isDirectory()) {
            return;
        }
        File[] files = folderToAdd.listFiles();
        if (files != null) {
            for (File file : files) {
                String entryName = folderName + file.getName();
                if (file.isDirectory()) {
                    entryName += "/";
                }
                addFile(zos, entryName, file);
            }
        }
    }
    private static void addFile(ZipOutputStream zos, String entryName, File file) throws IOException {
        if (file.isDirectory()) {
            addFolder(zos, entryName + "/", file);
        } else {
            zos.putNextEntry(new ZipEntry(entryName));
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
            }
            zos.closeEntry();
        }
    }
}
