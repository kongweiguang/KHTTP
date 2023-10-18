package io.github.kongweiguang.khttp.core;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
    private String contentType = ContentType.text_plain.v();

    public HttpExchange httpExchange() {
        return he;
    }

    public Headers getHeaders() {
        return httpExchange().getResponseHeaders();
    }

    public OutputStream stream() {
        return httpExchange().getResponseBody();
    }

    public Res header(final String k, final String v) {
        getHeaders().add(k, v);
        return this;
    }

    public Res headers(final Map<String, List<String>> headers) {
        getHeaders().putAll(headers);
        return this;
    }


    public Res charset(final Charset charset) {
        this.charset = charset;
        return this;
    }

    public Charset charset() {
        return this.charset;
    }

    public Res contentType(final String contentType) {
        this.contentType = contentType;
        return this;
    }

    public Res send(final String str) {
        if (isNull(str)) {
            return this;
        }

        return send(str.getBytes(charset()));
    }

    public Res send(final byte[] bytes) {
        return write(200, bytes);
    }

    public Res file(final String fileName, final byte[] bytes) {
        try {
            header(Header.content_disposition.v(), "attachment;filename=" + URLEncoder.encode(fileName, charset().name()));
            contentType(Util.getMimeType(fileName));

            send(bytes);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    public Res write(int code, final byte[] bytes) {
        try (final OutputStream out = stream()) {
            httpExchange().sendResponseHeaders(code, bytes.length);
            header(Header.content_type.v(), contentType);

            out.write(bytes);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    public PrintWriter writer() {
        return new PrintWriter(new OutputStreamWriter(stream(), charset));
    }

    public void close() {
        try {
            stream().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
