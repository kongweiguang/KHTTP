package io.github.kongweiguang.khttp.core;


/**
 * ContentType中涉及到的常量
 *
 * @author kongweiguang
 */
public enum ContentType {

    form_urlencoded("application/x-www-form-urlencoded"),
    multipart("multipart/form-data"),
    json("application/json"),
    xml("application/xml"),
    text_plain("text/plain"),
    text_xml("text/xml"),
    text_html("text/html"),
    octet_stream("application/octet-stream"),
    event_stream("text/event-stream");

    private final String v;

    ContentType(final String v) {
        this.v = v;
    }

    public String v() {
        return v;
    }

    @Override
    public String toString() {
        return v();
    }
}
