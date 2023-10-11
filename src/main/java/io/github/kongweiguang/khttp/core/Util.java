package io.github.kongweiguang.khttp.core;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;

import static java.util.Objects.isNull;

public final class Util {

    public static boolean _404(final HttpExchange he, final Handler handler) throws IOException {
        if (isNull(handler)) {
            String _404 = "404 not found";
            he.sendResponseHeaders(404, _404.length());
            he.getResponseBody().write(_404.getBytes());
            return true;
        }
        return false;
    }


    public static byte[] toByteArray(final InputStream input, final int size) {

        try {
            if (size < 0 || size == 0) {
                return new byte[0];
            }

            final byte[] data = new byte[size];
            int offset = 0;
            int read;

            while (offset < size && (read = input.read(data, offset, size - offset)) != -1) {
                offset += read;
            }

            if (offset != size) {
                return new byte[0];

            }

            return data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //hutool
    public static String getMimeType(final String fileName) {

        // 补充一些常用的mimeType
        if (fileName.endsWith(".css")) {
            return "text/css";
        } else if (fileName.endsWith(".js")) {
            return "application/x-javascript";
        } else if (fileName.endsWith(".rar")) {
            return "application/x-rar-compressed";
        } else if (fileName.endsWith(".7z")) {
            return "application/x-7z-compressed";
        } else if (fileName.endsWith(".wgt")) {
            return "application/widget";
        } else if (fileName.endsWith(".webp")) {
            return "image/webp";
        }

        return ContentType.octet_stream.v();
    }
}
