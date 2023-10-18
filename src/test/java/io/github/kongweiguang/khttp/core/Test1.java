package io.github.kongweiguang.khttp.core;

import io.github.kongweiguang.khttp.KHTTP;
import io.github.kongweiguang.khttp.sse.EventMessage;
import io.github.kongweiguang.khttp.sse.SSEHandler;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
                .get("/xz", (req, res) -> res.file("k.txt", Files.readAllBytes(Paths.get("D:\\k\\k.txt"))))
                .get("/sse", new SSEHandler() {
                    @Override
                    public void handler(final Req req, final Res res) {
                        for (int i = 0; i < 3; i++) {
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }

                            send(res, new EventMessage(UUID.randomUUID().toString(), "eventType", new Date().toString()));
                        }

                        //完成
                        send(res, new EventMessage(UUID.randomUUID().toString(), "eventType", "done"));

                        //关闭
                        close(res);
                    }
                })
                .ok(8080);

    }
}
