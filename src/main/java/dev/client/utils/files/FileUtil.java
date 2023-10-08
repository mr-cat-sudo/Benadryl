package dev.client.utils.files;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtil {
    public static void addToZip(ZipOutputStream zos, String folderName, File fileToAdd) throws IOException {
        if (fileToAdd == null || !fileToAdd.exists()){return;}
        String zipEntryName = folderName + "/" + fileToAdd.getName();
        zos.putNextEntry(new ZipEntry(zipEntryName));
        try (FileInputStream fis = new FileInputStream(fileToAdd)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
        }
        zos.closeEntry();
    }
}
