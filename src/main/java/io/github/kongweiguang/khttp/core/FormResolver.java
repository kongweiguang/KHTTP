package io.github.kongweiguang.khttp.core;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public final class FormResolver {
    private static final byte[] sep = "\r\n".getBytes(StandardCharsets.UTF_8);


    public static void parser(Req req) {
        boundaryIndex(req);
    }


    private static void boundaryIndex(Req req) {
        byte[] boundary = ("--" + req.contentType().substring(req.contentType().indexOf("=") + 1)).getBytes(StandardCharsets.UTF_8);
        byte[] body = req.bytes();
        int count = 0;
        int sep_count = 0;
        int length = body.length;
        int boundaryLen = boundary.length;
        int cursor = boundaryLen + 2;
        boolean paramStart = false;
        boolean paramEnd = false;
        Part part = null;

        for (int i = 0; i < length; i++) {

            for (int j = 0; j < boundaryLen; j++) {
                if (i + j == length) {
                    return;
                }
                if (body[i + j] == boundary[j]) {
                    count++;
                } else {
                    break;
                }
            }

            if (count == boundaryLen) {
                if (i > 0) {
                    paramEnd = true;
                }

                paramStart = true;

                i += boundaryLen + 2 - 1;
            }

            if (paramStart) {

                for (int j = 0; j < 2; j++) {
                    if (body[i + j] == sep[j]) {
                        sep_count++;
                    } else {
                        break;
                    }
                }

                if (sep_count == 2) {
                    byte[] line = new byte[i - cursor];

                    System.arraycopy(body, cursor, line, 0, i - cursor);

                    if (isLineBlank(line)) {
                        paramStart = false;
                        paramEnd = false;
                        cursor = i + 2;
                    } else {
                        String lineStr = new String(line);
                        if (lineStr.startsWith("Content-Disposition: form-data; ")) {
                            part = resolveParam(lineStr);
                        }

                        cursor = i;
                    }

                    i += 1;
                }

                sep_count = 0;
            }

            if (paramEnd) {
                if (part == null) {
                    return;
                }

                if (part.type().equals("text")) {
                    byte[] val = new byte[i - cursor - boundaryLen - 1 - 2];

                    System.arraycopy(body, cursor, val, 0, i - cursor - boundaryLen - 1 - 2);

                    req.params().put(part.name(), new String(val));
                } else {
                    final UpFile uf = new UpFile();
                    uf.setContent(new ByteArrayInputStream(body, cursor, (i - boundaryLen - 1 - 2) - cursor));
                    uf.setFileName(part.filename());

                    req.fileMap().computeIfAbsent(part.name(), k -> new ArrayList<>()).add(uf);
                }

                cursor = i + 1;

                paramEnd = false;
            }

            count = 0;
        }

    }

    private static Part resolveParam(String lineStr) {

        String[] kvs = lineStr.substring("Content-Disposition: form-data; ".length()).split(";");

        Part part = new Part();

        part.type("text");

        for (String kv : kvs) {
            String[] k_v = kv.trim().split("=");

            if ("name".equals(k_v[0])) {
                part.name(k_v[1].replace("\"", ""));
            } else if ("filename".equals(k_v[0])) {
                part.setFilename(k_v[1].replace("\"", "")).type("file");
            }

        }

        return part;
    }


    private static boolean isLineBlank(byte[] line) {
        if (line.length == 0) {
            return true;
        }

        if (line.length == 2) {
            return line[0] == sep[0] && line[1] == sep[1];
        }

        return false;
    }

    private static final class Part {
        private String type;
        private String name;
        private String filename;

        public String type() {
            return type;
        }

        public Part type(final String type) {
            this.type = type;
            return this;
        }

        public String name() {
            return name;
        }

        public Part name(final String name) {
            this.name = name;
            return this;
        }

        public String filename() {
            return filename;
        }

        public Part setFilename(final String filename) {
            this.filename = filename;
            return this;
        }
    }
}
