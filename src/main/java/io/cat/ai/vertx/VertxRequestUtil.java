package io.cat.ai.vertx;

import io.cat.ai.vertx.http.util.DecodeType;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.EncodeException;
import io.vertx.core.json.Json;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;

import lombok.*;

import java.util.Optional;

import static java.util.Objects.nonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VertxRequestUtil {

    private static final Logger logger = LoggerFactory.getLogger(VertxRequestUtil.class);

    public static String asString(Object obj) {
        String json = null;

        if (nonNull(obj)) {
            try {
                json = Json.encode(obj);
            } catch (EncodeException e) {
                logger.error("Error at encoding: " + e.getMessage());
            }
        }
        return json;
    }

    public static Buffer asBuffer(Object obj) {
        Buffer json = null;

        if (nonNull(obj)) {
            try {
                json = Json.encodeToBuffer(obj);
            } catch (EncodeException e) {
                logger.error("Error at encoding: " + e.getMessage());
            }
        }
        return json;
    }

    private static <T> T parse(DecodeType type, Object obj, Class<? extends T> clz) throws DecodeException {
        T body = null;

        if (nonNull(obj)) {
            try {
                switch (type) {
                    case BUFFER:
                        body = Json.decodeValue((Buffer) obj, clz);
                        break;
                    case STRING:
                        body = Json.decodeValue((String) obj, clz);
                        break;
                }
            } catch (DecodeException e) {
                logger.error("Error at decoding: " + e.getMessage());
            }
        }
        return body;
    }

    public static <T> T parseBody(RoutingContext ctx, Class<? extends T> clz) throws DecodeException {
        return parse(DecodeType.BUFFER, ctx.getBody(), clz);
    }

    public static <T> Optional<T> parseBodyOpt(RoutingContext ctx, Class<? extends T> clz) throws DecodeException {
        return Optional.ofNullable(parseBody(ctx, clz));
    }

    public static <T> T parseBody(String strBody, Class<? extends T> clz) throws DecodeException {
        return parse(DecodeType.STRING, strBody, clz);
    }

    public static <T> Optional<T> parseBodyOpt(String strBody, Class<? extends T> clz) throws DecodeException {
        return Optional.ofNullable(parseBody(strBody, clz));
    }

    public static <T> T parseBody(Buffer bytes, Class<? extends T> clz) throws DecodeException {
        return parse(DecodeType.BUFFER, bytes, clz);
    }

    public static <T> Optional<T> parseBodyOpt(Buffer bytes, Class<? extends T> clz) throws DecodeException {
        return Optional.ofNullable(parseBody(bytes, clz));
    }

    public static String pathParam(RoutingContext ctx, String param) {
        return ctx.request().getParam(param);
    }

    public static Optional<String> paramOpt(String param, RoutingContext ctx) {
        return Optional.ofNullable(ctx.request().getParam(param));
    }

    public static String queryParam(RoutingContext ctx, String paramName) {
        return ctx.request().params().get(paramName);
    }

    public static String path(RoutingContext ctx) {
        return ctx.request().path();
    }
}