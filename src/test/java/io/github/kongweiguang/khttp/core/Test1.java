package io.github.kongweiguang.khttp.core;

import io.github.kongweiguang.khttp.KHTTP;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class Test1 {
    public static void main(String[] args) {
        KHTTP.of()
                .executor(Executors.newCachedThreadPool())
                .web("/Users/kongweiguang/Desktop/hegui/xm/gs")
                .get("/get", (req, res) -> {
                    res.send("hello");
                })
                .post("/post", ((req, res) -> {
                    final String str = req.str();
                    System.out.println("str = " + str);

                    res.send("{\"key\":\"i am post res\"}");
                }))
                .post("/upload", (req, res) -> {
                    final MultiValueMap<String, String> params = req.params();
                    final Map<String, List<UpFile>> files = req.fileMap();
                    System.out.println(" ===================== ");
                })
                .ok(8080);

    }
}
