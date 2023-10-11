package io.github.kongweiguang.khttp.core;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.kongweiguang.khttp.core.Util._404;
import static java.util.Objects.nonNull;

public final class RestHandler implements HttpHandler {

    private static final Map<Method, Map<String, Handler>> rest_map = new ConcurrentHashMap<>();
    private static final Map<String, Handler> defalut_map = new ConcurrentHashMap<>();

    public static void add(final String path, final Handler handler) {
        defalut_map.put(path, handler);
    }

    public static void add(final Method method, final String path, final Handler handler) {
        rest_map.computeIfAbsent(method, k -> new ConcurrentHashMap<>()).put(path, handler);
    }

    @Override
    public void handle(final HttpExchange he) throws IOException {
        try {
            final Method method = Method.valueOf(he.getRequestMethod());

            final Map<String, Handler> map = rest_map.getOrDefault(method, defalut_map);
            Handler handler = map.get(he.getRequestURI().getPath());

            if (nonNull(handler)) {
                handler0(he, handler);
            } else {
                if (Method.GET.equals(method)) {
                    handler0(he, defalut_map.get(WebHandler.PATH));
                }
            }
        } finally {
            he.close();
        }

    }

    private static void handler0(final HttpExchange he, final Handler handler) throws IOException {
        if (_404(he, handler)) return;

        handler.doHandler(new Req(he), new Res(he));
    }


}
