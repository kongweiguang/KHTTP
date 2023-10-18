package io.github.kongweiguang.khttp.sse;


import io.github.kongweiguang.khttp.core.ContentType;
import io.github.kongweiguang.khttp.core.Handler;
import io.github.kongweiguang.khttp.core.Header;
import io.github.kongweiguang.khttp.core.Req;
import io.github.kongweiguang.khttp.core.Res;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;

import static java.util.Objects.nonNull;

/**
 * sse的action
 *
 * @author kongweiguang
 * @since 0.1
 */
public abstract class SSEHandler implements Handler {

    @Override
    public void doHandler(final Req req, final Res res) throws IOException {
        res.contentType(ContentType.event_stream.v());
        res.header(Header.cache_control.v(), "no-cache");
        res.header(Header.connection.v(), "keep-alive");
        res.write(HttpURLConnection.HTTP_OK, new byte[0]);
        handler(req, res);
    }

    /**
     * 处理请求
     *
     * @param request  请求对象
     * @param response 响应对象
     * @throws IOException IO异常
     */
    public abstract void handler(final Req req, final Res res);

    /**
     * 发送数据给客户端
     *
     * @param res   输出流 {@link Res }
     * @param event 数据对象 {@link }
     * @return this
     */
    public SSEHandler send(final Res res, final EventMessage event) {
        if (nonNull(res)) {
            try (PrintWriter writer = res.writer()) {
                writer.write(event.toString());
                writer.flush();
            }
        }
        return this;
    }

    /**
     * 关闭输出流
     *
     * @param res 输出流  {@link Res }
     */
    public void close(final Res res) {
        res.close();
    }
}
