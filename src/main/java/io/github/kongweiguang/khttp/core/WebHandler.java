package io.github.kongweiguang.khttp.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static io.github.kongweiguang.khttp.core.Util._404;
import static java.util.Objects.nonNull;

public final class WebHandler implements Handler {

    private final String base_path;
    private String index_file = "index.html";

    public WebHandler(final String path, final String indexName) {
        this.base_path = path;
        if (nonNull(indexName)) {
            this.index_file = indexName;
        }
    }

    @Override
    public void doHandler(final Req req, final Res res) throws IOException {

        File file = new File(this.base_path, req.path());

        if (file.exists()) {

            if (file.isDirectory()) {
                file = new File(file, this.index_file);
                if (!file.exists()) {
                    _404(req.httpExchange(), null);
                    return;
                }
            }

            res.send(Files.readAllBytes(file.toPath()));

        } else {
            _404(req.httpExchange(), null);
        }

    }

}
