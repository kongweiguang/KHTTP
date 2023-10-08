package io.github.kongweiguang.khttp.core;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public final class FormResolver {
    private static final byte[] sep = "\r\n".getBytes(StandardCharsets.UTF_8);


    public static List<Part> parser(String contentType, byte[] body) {
        return boundaryIndex(
                body,
                ("--" + contentType.substring(contentType.indexOf("=") + 1)).getBytes(StandardCharsets.UTF_8)
        );
    }


    private static List<Part> boundaryIndex(byte[] body, byte[] boundary) {
        int count = 0;
        int sep_count = 0;
        List<Part> list = new ArrayList<>();
        int length = body.length;
        int boundaryLen = boundary.length;
        int cursor = boundaryLen + 2;
        Part paramItem = null;
        boolean paramStart = false;
        boolean paramEnd = false;

        for (int i = 0; i < length; i++) {

            for (int j = 0; j < boundaryLen; j++) {
                if (i + j == length) {
                    return list;
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
                    if (body[i + j] == FormResolver.sep[j]) {
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
                            paramItem = resolveParam(lineStr);
                        }

                        cursor = i;
                    }

                    i += 1;
                }

                sep_count = 0;
            }

            if (paramEnd) {
                if (paramItem == null) {
                    return null;
                }

                if (paramItem.type().equals("text")) {
                    byte[] val = new byte[i - cursor - boundaryLen - 1 - 2];
                    System.arraycopy(body, cursor, val, 0, i - cursor - boundaryLen - 1 - 2);
                    paramItem.setValue(new String(val));
                } else {
                    paramItem.setStartIndex(cursor);
                    paramItem.setEndIndex(i - boundaryLen - 1 - 2);
                }

                list.add(paramItem);

                cursor = i + 1;

                paramEnd = false;
            }

            count = 0;
        }

        return list;
    }

    private static Part resolveParam(String lineStr) {

        String[] kvs = lineStr.substring("Content-Disposition: form-data; ".length()).split(";");

        Part part = new Part();

        part.setType("text");

        for (String kv : kvs) {
            String[] k_v = kv.trim().split("=");

            if ("name".equals(k_v[0])) {
                part.setName(k_v[1].replace("\"", ""));
            } else if ("filename".equals(k_v[0])) {
                part.setFilename(k_v[1].replace("\"", "")).setType("file");
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

    public static final class Part {
        private String type;
        private String name;
        private String filename;
        private String value;
        private int startIndex;
        private int endIndex;

        public String type() {
            return type;
        }

        public Part setType(final String type) {
            this.type = type;
            return this;
        }

        public String name() {
            return name;
        }

        public Part setName(final String name) {
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

        public String value() {
            return value;
        }

        public Part setValue(final String value) {
            this.value = value;
            return this;
        }

        public int startIndex() {
            return startIndex;
        }

        public Part setStartIndex(final int startIndex) {
            this.startIndex = startIndex;
            return this;
        }

        public int endIndex() {
            return endIndex;
        }

        public Part setEndIndex(final int endIndex) {
            this.endIndex = endIndex;
            return this;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Part.class.getSimpleName() + "[", "]")
                    .add("type='" + type + "'")
                    .add("name='" + name + "'")
                    .add("filename='" + filename + "'")
                    .add("value='" + value + "'")
                    .add("startIndex=" + startIndex)
                    .add("endIndex=" + endIndex)
                    .toString();
        }
    }
}
