package io.github.kongweiguang.khttp.core;

import java.io.InputStream;

public class UpFile {
    private String fileName;
    private InputStream content;


    public String fileName() {
        return fileName;
    }

    public UpFile setFileName(final String fileName) {
        this.fileName = fileName;
        return this;
    }

    public InputStream content() {
        return content;
    }

    public UpFile setContent(final InputStream content) {
        this.content = content;
        return this;
    }
}
