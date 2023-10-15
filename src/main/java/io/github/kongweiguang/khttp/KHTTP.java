package io.github.kongweiguang.khttp;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsServer;
import io.github.kongweiguang.khttp.core.Handler;
import io.github.kongweiguang.khttp.core.Method;
import io.github.kongweiguang.khttp.core.Req;
import io.github.kongweiguang.khttp.core.Res;
import io.github.kongweiguang.khttp.core.RestHandler;
import io.github.kongweiguang.khttp.core.WebHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static io.github.kongweiguang.khttp.core.Method.DELETE;
import static io.github.kongweiguang.khttp.core.Method.GET;
import static io.github.kongweiguang.khttp.core.Method.POST;
import static io.github.kongweiguang.khttp.core.Method.PUT;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * 基于内置httpserver封装的简易http服务器
 */
public final class KHTTP {

    private HttpServer httpServer;
    private final List<Filter> filters = new ArrayList<>();
    private HttpsConfigurator config;
    private Executor executor;


    private KHTTP() {
    }

    public static KHTTP of() {
        return new KHTTP();
    }

    public KHTTP httpsConfig(final HttpsConfigurator config) {
        this.config = config;
        return this;
    }

    public KHTTP executor(final Executor executor) {
        this.executor = executor;
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

    public void ok(int port) {
        start(new InetSocketAddress(port));
    }


    public void ok(InetSocketAddress address) {
        start(address);
    }


    private void start(final InetSocketAddress address) {
        try {
            final long start = System.currentTimeMillis();

            init();

            server().setExecutor(executor());

            server().bind(address, 0);

            addContext();

            server().start();

            print(start);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void init() throws IOException {
        if (nonNull(config())) {
            final HttpsServer server = HttpsServer.create();
            server.setHttpsConfigurator(config());
            this.httpServer = server;
        } else {
            this.httpServer = HttpServer.create();
        }
    }

    private void addContext() {
        server()
                .createContext("/", new RestHandler())
                .getFilters()
                .addAll(filters());
    }

    private void print(long start) {
        final long cur = System.currentTimeMillis();

        System.err.printf(
                "[%s]KHTTP Server listen on 【%s:%s】 use time %dms %n",
                String.format("%tF %<tT", cur),
                server().getAddress().getHostName(),
                server().getAddress().getPort(),
                (cur - start)
        );
    }

    public void stop(int delay) {
        server().stop(delay);
    }

    //get

    public HttpServer server() {
        return httpServer;
    }

    public List<Filter> filters() {
        return filters;
    }

    public HttpsConfigurator config() {
        return config;
    }

    public Executor executor() {
        if (isNull(executor)) {
            this.executor = Executors.newCachedThreadPool();
        }

        return executor;
    }
}
