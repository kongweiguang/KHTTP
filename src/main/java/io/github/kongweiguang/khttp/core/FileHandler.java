package io.github.kongweiguang.khttp.core;

import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Formatter;
import java.util.Locale;

import static io.github.kongweiguang.khttp.core.Util._404;

/**
 * 参考jlhttp的文件输出
 */
public final class FileHandler implements Handler {

    private final String path;

    public FileHandler(final String path) {
        this.path = path;
    }

    @Override
    public void doHandler(final Req req, final Res res) throws IOException {
        final HttpExchange exchange = req.httpExchange();
        if (Method.GET.equals(Method.valueOf(req.method()))) {
            String requestPath = exchange.getRequestURI().getPath();

            File file = new File(this.path + requestPath);

            if (file.exists()) {

                if (file.isDirectory()) {
                    final String index = createIndex(file, requestPath);
                    exchange.sendResponseHeaders(200, index.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(index.getBytes());
                    os.close();
                } else if (file.isFile()) {
                    // 如果是文件，则返回文件内容
                    byte[] fileBytes = Files.readAllBytes(file.toPath());
                    exchange.sendResponseHeaders(200, fileBytes.length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(fileBytes);
                    os.close();
                }

            } else {
                _404(req.httpExchange(), null);
            }
        }

    }


    public static String createIndex(File dir, String path) {
        if (!path.endsWith("/")) {
            path += "/";
        }

        int w = 21;
        for (String name : dir.list()) {
            if (name.length() > w) {
                w = name.length();
            }
        }

        w += 2;

        Formatter f = new Formatter(Locale.US);

        f.format("<!DOCTYPE html>%n" +
                        "<html><head><title>Index of %s</title></head>%n" +
                        "<body><h1>Index of %s</h1>%n" +
                        "<pre> Name%" + (w - 5) + "s Last modified      Size<hr>",
                path, path, "");

        if (path.length() > 1) {
            f.format(
                    " <a href=\"%s/\">Parent Directory</a>%"
                            + (w + 5) + "s-%n",
                    getParentPath(path), ""
            );
        }

        for (File file : dir.listFiles()) {
            try {
                String name = file.getName() + (file.isDirectory() ? "/" : "");

                String size = file.isDirectory() ? "- " : toSizeApproxString(file.length());

                String link = new URI(null, path + name, null).toASCIIString();

                if (!file.isHidden() && !name.startsWith(".")) {
                    f.format(
                            " <a href=\"%s\">%s</a>%-" + (w - name.length()) +
                                    "s&#8206;%td-%<tb-%<tY %<tR%6s%n",
                            link, name, "", file.lastModified(), size
                    );
                }

            } catch (URISyntaxException ignore) {
            }
        }

        f.format("</pre></body></html>");
        return f.toString();
    }

    public static String getParentPath(String path) {
        path = trimRight(path, '/'); // remove trailing slash
        int slash = path.lastIndexOf('/');
        return slash < 0 ? null : path.substring(0, slash);
    }

    public static String trimRight(String s, char c) {
        int len = s.length() - 1;
        int end;
        for (end = len; end >= 0 && s.charAt(end) == c; end--) ;
        return end == len ? s : s.substring(0, end + 1);
    }

    public static String toSizeApproxString(long size) {
        final char[] units = {' ', 'K', 'M', 'G', 'T', 'P', 'E'};
        int u;
        double s;
        for (u = 0, s = size; s >= 1000; u++, s /= 1024) ;
        return String.format(s < 10 ? "%.1f%c" : "%.0f%c", s, units[u]);
    }

}
