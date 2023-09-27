package io.github.kongweiguang.khttp;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsServer;
import io.github.kongweiguang.khttp.core.FileHandler;
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

    private KHTTP(final InetSocketAddress address, final HttpsConfigurator configurator) {
        try {
            if (nonNull(configurator)) {
                final HttpsServer server = HttpsServer.create(address, 0);
                server.setHttpsConfigurator(configurator);
                this.httpServer = server;
            } else {
                this.httpServer = HttpServer.create(address, 0);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static KHTTP of(int port) {
        return new KHTTP(new InetSocketAddress(port), null);
    }

    public static KHTTP of(InetSocketAddress address, HttpsConfigurator config) {
        return new KHTTP(address, config);
    }

    public KHTTP executor(final Executor executor) {
        httpServer().setExecutor(executor);
        return this;
    }

    public KHTTP file(final String path) {
        RestHandler.add("/", new FileHandler(path));
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
        RestHandler.add(path, handler);
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

    public KHTTP ok() {
        addContext();
        httpServer().start();
        System.err.printf(
                "KHTTP Http Server listen on 【%s:%s】 start time 【%s】 ",
                httpServer().getAddress().getHostName(),
                httpServer().getAddress().getPort(),
                String.format("%tF %<tT", System.currentTimeMillis())
        );
        return this;
    }


    //get

    public HttpServer httpServer() {
        return httpServer;
    }

    public List<Filter> filters() {
        return filters;
    }
}
