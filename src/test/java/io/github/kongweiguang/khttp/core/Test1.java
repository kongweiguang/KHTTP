package io.github.kongweiguang.khttp.core;

import io.github.kongweiguang.khttp.KHTTP;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class Test1 {
    public static void main(String[] args) {
        KHTTP.of()
                .executor(Executors.newCachedThreadPool())
                .web("/Users/kongweiguang/Desktop/hegui/xm/gs")
                .get("/get", (req, res) -> {
                    final MultiValueMap<String, String> params = req.params();
                    res.send("hello").send("world");
                })
                .post("/post", ((req, res) -> {
                    final String str = req.str();
                    System.out.println("str = " + str);

                    res.send("{\"key\":\"i am post res\"}");
                }))
                .post("/upload", (req, res) -> {

                    final MultiValueMap<String, String> params = req.params();

                    final Map<String, List<UpFile>> files = req.fileMap();
                })
                .get("/xz", new Handler() {
                    @Override
                    public void doHandler(final Req req, final Res res) throws IOException {
                        res.file("k.txt", Files.readAllBytes(Paths.get("D:\\k\\k.txt")));
                    }
                })
                .ok(8080);

    }
}
