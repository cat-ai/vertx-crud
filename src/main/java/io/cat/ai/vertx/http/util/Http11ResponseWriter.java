package io.cat.ai.vertx.http.util;

import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import lombok.*;

import static io.netty.handler.codec.http.HttpHeaderValues.*;

import static io.vertx.core.http.HttpHeaders.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Http11ResponseWriter {

    private static final CharSequence SERVER_VALUE = HttpHeaders.createOptimized("io.cat.ai.vertx-crud");

    public static void write(RoutingContext ctx, MimeType mimeType, Buffer buffer) {
        val res = ctx.request().response();

        setHeaders(res.headers(), mimeType, buffer.length());

        write(res, buffer);
    }

    private static void setHeaders(MultiMap headers, MimeType mimeType, int contLen) {
        switch(mimeType) {

            case APP_JSON:
                headers.add(CONTENT_TYPE, APPLICATION_JSON);
                break;

            case TXT_PLAIN:
                headers.add(CONTENT_TYPE, TEXT_PLAIN);
                break;
        }

        headers.add(SERVER, SERVER_VALUE);
        headers.add(CONTENT_LENGTH, String.valueOf(contLen));
    }

    private static void write(HttpServerResponse response, Buffer buffer) {
        if (!response.closed())
            response.end(buffer);
    }
}