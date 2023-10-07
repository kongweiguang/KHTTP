package io.github.kongweiguang.khttp.core;

import io.github.kongweiguang.khttp.KHTTP;

import java.util.concurrent.Executors;

public class Test1 {
    public static void main(String[] args) {
        KHTTP.of()
                .executor(Executors.newCachedThreadPool())
                .file("/Users/kongweiguang/Desktop/hegui/xm/gs")
                .get("/get", (req, res) -> {
                    res.send("hello".getBytes());
                })
                .post("/post", ((req, res) -> {
                    res.send("{\"key\":\"i am post res\"}");
                }))
                .post("/upload", (req, res) -> {
                    req.multipart();
                })
                .ok(8080);

    }
}
