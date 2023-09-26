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

    public static byte[] read(InputStream source) {
        try {
            byte[] buf = new byte[source.available()];
            source.read(buf);
            return buf;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
