package com.netease.mc.modSS.utils.misc;

import java.io.*;

public class FileUtils
{
    public static String readFile(final File file) {
        final StringBuilder result = new StringBuilder();
        try {
            final FileInputStream fIn = new FileInputStream(file);
            try (final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fIn))) {
                String str;
                while ((str = bufferedReader.readLine()) != null) {
                    result.append(str);
                    result.append(System.lineSeparator());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
