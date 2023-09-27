package io.github.kongweiguang.khttp.core;

import java.io.IOException;

@FunctionalInterface
public interface Filter {


    void doFilter(final Req req, final Res res, final com.sun.net.httpserver.Filter.Chain chain) throws IOException;

    default String description() {
        return "default";
    }

}
