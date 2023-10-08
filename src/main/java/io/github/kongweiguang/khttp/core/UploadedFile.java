package io.github.kongweiguang.khttp.core;

import java.io.InputStream;

public class UploadedFile {
    private String fileName;
    private InputStream content;


    public String fileName() {
        return fileName;
    }

    public UploadedFile setFileName(final String fileName) {
        this.fileName = fileName;
        return this;
    }

    public InputStream content() {
        return content;
    }

    public UploadedFile setContent(final InputStream content) {
        this.content = content;
        return this;
    }
}
