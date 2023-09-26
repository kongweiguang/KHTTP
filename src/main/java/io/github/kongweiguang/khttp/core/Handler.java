package io.github.kongweiguang.khttp.core;

import java.io.IOException;

@FunctionalInterface
public interface Handler {
    void doHandler(Req req, Res res) throws IOException;
}
