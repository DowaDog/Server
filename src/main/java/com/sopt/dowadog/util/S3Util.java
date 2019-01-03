package com.sopt.dowadog.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;


public class S3Util {

    public static void displayText(InputStream input) throws IOException {
        // Read one text line at a time and display.
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        while (true) {
            String line = reader.readLine();
            if (line == null) break;
            System.out.println("    " + line);
        }
    }

    public static File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        return convFile;
    }

    public static String getFilePath(String baseDir, MultipartFile file){
        return new StringBuilder(baseDir).
                        append(S3Util.getUuid()).
                        append(file.getOriginalFilename()).toString();

    }

    public static String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String getImgPath(String endPoint, String imgPath) {
        return new StringBuilder(endPoint).append(imgPath).toString();
    }
}
