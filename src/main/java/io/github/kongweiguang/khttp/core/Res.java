package io.github.kongweiguang.khttp.core;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

public final class Res {
    public Res(final HttpExchange httpExchange) {
        this.he = httpExchange;
    }

    private final HttpExchange he;
    private Charset charset = StandardCharsets.UTF_8;

    public HttpExchange httpExchange() {
        return he;
    }

    public Headers getHeaders() {
        return httpExchange().getResponseHeaders();
    }

    public Res header(final String k, final String v) {
        getHeaders().add(k, v);
        return this;
    }

    public Res headers(final Map<String, List<String>> headers) {
        getHeaders().putAll(headers);
        return this;
    }


    public Res charset(Charset charset) {
        this.charset = charset;
        return this;
    }


    public Charset charset() {
        return this.charset;
    }

    public OutputStream stream() {
        return httpExchange().getResponseBody();
    }

    public void send(String str) {
        if (isNull(str)) return;

        write(str.getBytes(charset()));
    }

    public void send(byte[] bytes) {
        write(bytes);
    }


    public void write(final byte[] bytes) {
        try (final OutputStream out = stream()) {
            httpExchange().sendResponseHeaders(200, bytes.length);

            out.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
