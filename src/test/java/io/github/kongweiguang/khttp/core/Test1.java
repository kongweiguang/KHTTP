package io.github.kongweiguang.khttp.core;

import io.github.kongweiguang.khttp.KHTTP;

import java.util.concurrent.Executors;

public class Test1 {
    public static void main(String[] args) {
        final KHTTP ok = KHTTP.of(8080)
                .executor(Executors.newCachedThreadPool())
                .get("/get", (req, res) -> {
                    res.send("hello".getBytes());
                })
                .post("/post", ((req, res) -> {
                    res.send(
                            "{\n" +
                                    "    \"key\":\"i am post res\"\n" +
                                    "}"
                    );
                }))
                .ok();


    }
}
