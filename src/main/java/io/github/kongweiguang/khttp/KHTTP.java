package io.github.kongweiguang.khttp;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsServer;
import io.github.kongweiguang.khttp.core.WebHandler;
import io.github.kongweiguang.khttp.core.Handler;
import io.github.kongweiguang.khttp.core.Method;
import io.github.kongweiguang.khttp.core.Req;
import io.github.kongweiguang.khttp.core.Res;
import io.github.kongweiguang.khttp.core.RestHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import static io.github.kongweiguang.khttp.core.Method.DELETE;
import static io.github.kongweiguang.khttp.core.Method.GET;
import static io.github.kongweiguang.khttp.core.Method.POST;
import static io.github.kongweiguang.khttp.core.Method.PUT;
import static java.util.Objects.nonNull;

/**
 * 基于内置httpserver封装的简易http服务器
 */
public final class KHTTP {


    private final HttpServer httpServer;
    private final List<Filter> filters = new ArrayList<>();

    private KHTTP(final HttpsConfigurator config) {
        try {
            if (nonNull(config)) {
                final HttpsServer server = HttpsServer.create();
                server.setHttpsConfigurator(config);
                this.httpServer = server;
            } else {
                this.httpServer = HttpServer.create();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static KHTTP of() {
        return new KHTTP(null);
    }

    public static KHTTP ofHttps(final HttpsConfigurator config) {
        return new KHTTP(config);
    }

    public KHTTP executor(final Executor executor) {
        httpServer().setExecutor(executor);
        return this;
    }

    public KHTTP web(final String path, final String... fileName) {
        RestHandler.add(WebHandler.PATH, new WebHandler(path, fileName.length > 1 ? fileName[0] : null));
        return this;
    }

    public KHTTP filter(final io.github.kongweiguang.khttp.core.Filter filter) {
        filters().add(new Filter() {
            @Override
            public void doFilter(final HttpExchange exchange, final Chain chain) throws IOException {
                filter.doFilter(new Req(exchange), new Res(exchange), chain);
            }

            @Override
            public String description() {
                return filter.description();
            }
        });
        return this;
    }

    public KHTTP rest(final Method method, final String path, final Handler handler) {
        RestHandler.add(method, path, handler);
        return this;
    }

    public KHTTP rest(final String path, final Handler handler) {
        RestHandler.add(path, handler);
        return this;
    }


    public KHTTP get(final String path, final Handler handler) {
        RestHandler.add(GET, path, handler);
        return this;
    }

    public KHTTP post(final String path, final Handler handler) {
        RestHandler.add(POST, path, handler);
        return this;
    }

    public KHTTP delete(final String path, final Handler handler) {
        RestHandler.add(DELETE, path, handler);
        return this;
    }

    public KHTTP put(final String path, final Handler handler) {
        RestHandler.add(PUT, path, handler);
        return this;
    }

    private void addContext() {
        httpServer()
                .createContext("/", new RestHandler())
                .getFilters()
                .addAll(filters());
    }

    public void ok(int port) {
        start(new InetSocketAddress(port));
    }


    public void ok(InetSocketAddress address) {
        start(address);
    }

    private void start(final InetSocketAddress address) {
        try {
            final long start = System.currentTimeMillis();

            addContext();

            httpServer().bind(address, 0);

            httpServer().start();

            print(start);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void print(long start) {
        final long cur = System.currentTimeMillis();
        System.err.printf(
                "[%s]KHTTP Server listen on 【%s:%s】 use time %dms",
                String.format("%tF %<tT", cur),
                httpServer().getAddress().getHostName(),
                httpServer().getAddress().getPort(),
                (cur - start)
        );
    }


    //get

    public HttpServer httpServer() {
        return httpServer;
    }

    public List<Filter> filters() {
        return filters;
    }
}
