package io.github.kongweiguang.khttp.core;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public final class Req {
    public Req(final HttpExchange httpExchange) {
        this.he = httpExchange;
    }

    private final HttpExchange he;
    private Charset charset;
    private MultiValueMap<String, String> paramMap;
    private Map<String, List<UpFile>> fileMap;
    private byte[] bytes;

    public HttpExchange httpExchange() {
        return this.he;
    }

    public Headers headers() {
        return httpExchange().getRequestHeaders();

    }

    public String header(final String name) {
        return headers().getFirst(name);
    }

    public String contentType() {
        return header(Header.content_type.v());
    }

    public Charset charset() {
        if (isNull(this.charset)) {
            final String contentType = header(Header.content_type.v());
            if (nonNull(contentType)) {
                this.charset = charset0(contentType);
            }
        }

        return this.charset;
    }

    private static Charset charset0(final String contentType) {
        try {
            String[] parts = contentType.split(";");
            if (parts.length > 1) {
                final String part = parts[1];
                if (part.startsWith("charset=")) {
                    return Charset.forName(part.substring("charset=".length()));
                }

            }
        } catch (Exception ignored) {

        }
        return StandardCharsets.UTF_8;
    }

    public String ua() {
        return header(Header.user_agent.v());
    }


    public String method() {
        return httpExchange().getRequestMethod();
    }

    public URI uri() {
        return this.he.getRequestURI();
    }


    public String path() {
        return uri().getPath();
    }

    public String query() {
        return uri().getQuery();
    }


    public boolean isMultipart() {
        if (!Objects.equals(Method.valueOf(method()), Method.POST)) {
            return false;
        }

        final String contentType = contentType();

        if (isNull(contentType)) {
            return false;
        }

        return contentType.toLowerCase().startsWith("multipart/");
    }

    public MultiValueMap<String, String> params() {
        if (isNull(this.paramMap)) {
            this.paramMap = new MultiValueMap<>();

            final String query = query();

            if (nonNull(query)) {
                getParams(query, this.paramMap);
            }

            if (isMultipart()) {
                try {
                    parserForm();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                getParams(str(), this.paramMap);
            }

        }
        return this.paramMap;
    }

    public static void getParams(String url, MultiValueMap<String, String> map) {
        String[] paramParts = url.split("&");
        for (String paramPart : paramParts) {
            String[] keyValue = paramPart.split("=");
            if (keyValue.length > 1) {
                map.put(keyValue[0], keyValue[1]);
            }
        }
    }

    public byte[] bytes() {
        if (isNull(this.bytes)) {
            bytes = Util.toByteArray(httpExchange().getRequestBody(), Integer.parseInt(header("Content-length")));
        }

        return this.bytes;
    }

    public String str() {
        return new String(bytes(), charset());
    }

    public InputStream stream() {
        return new ByteArrayInputStream(bytes());
    }

    private void parserForm() {
        this.fileMap = new HashMap<>();
        FormResolver.parser(this);
    }


    public Map<String, List<UpFile>> fileMap() {
        if (isNull(this.fileMap)) {
            try {
                parserForm();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return this.fileMap;
    }
}