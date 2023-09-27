package io.github.kongweiguang.khttp.core;

import com.sun.net.httpserver.HttpExchange;
import io.github.kongweiguang.khttp.KHTTP;

import java.io.OutputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Test1 {
    public static void main(String[] args) {
        final KHTTP ok = KHTTP.of(8081)
                .executor(Executors.newCachedThreadPool())
                .file("/Users/kongweiguang/Desktop/hegui/xm/gs")
                .get("/get", (req, res) -> {
                    try {
                        try {
                            final MultiValueMap<String, String> params = req.params();
                            System.out.println("params = " + params);
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        final HttpExchange t = res.httpExchange();
                        String response = "{\"spoken\":\"Hello, World!\"}";
                        t.sendResponseHeaders(200, response.length());
                        OutputStream os = t.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
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
