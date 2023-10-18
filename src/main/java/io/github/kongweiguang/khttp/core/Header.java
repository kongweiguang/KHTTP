/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package io.github.kongweiguang.khttp.core;

/**
 * Header中涉及到的常量
 *
 * @author kongweiguang
 */
public enum Header {
    authorization("Authorization"),
    content_type("Content-Type"),
    user_agent("User-Agent"),
    cookie("Cookie"),
    content_disposition("Content-Disposition"),
    content_length("Content-Length"),
    cache_control("Cache-Control"),
    connection("Connection")
    ;

    private final String v;

    Header(final String v) {
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
