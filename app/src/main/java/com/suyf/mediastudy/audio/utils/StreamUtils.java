package com.suyf.mediastudy.audio.utils;

import java.io.Closeable;
import java.io.IOException;

public class StreamUtils {

    public static void closeQuitely(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
