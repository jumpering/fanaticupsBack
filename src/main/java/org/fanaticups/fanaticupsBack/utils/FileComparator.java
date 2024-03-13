package org.fanaticups.fanaticupsBack.utils;

import java.io.*;

public class FileComparator {

    public static boolean isEqualImages(File imageFile1, File imageFile2) throws IOException {
        if (!imageFile1.exists() || !imageFile2.exists()) {
            throw new FileNotFoundException("Uno o ambos archivos no existen");
        }
        if (imageFile1.length() != imageFile2.length()) {
            return false;
        }

        try (InputStream inputStream1 = new FileInputStream(imageFile1);
             InputStream inputStream2 = new FileInputStream(imageFile2)) {
            int byte1 = 0;
            int byte2 = 0;

            while (byte1 != -1 && byte2 != -1) {
                byte1 = inputStream1.read();
                byte2 = inputStream2.read();

                if (byte1 != byte2) {
                    return false;
                }
            }
            return true;
        }
    }
}
