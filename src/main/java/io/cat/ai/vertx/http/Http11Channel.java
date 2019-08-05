package io.cat.ai.vertx.http;

import io.cat.ai.vertx.http.util.Http11ResponseWriter;
import io.cat.ai.vertx.http.util.MimeType;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Http11Channel {

    private static final Buffer BAD_REQUEST_MSG = Buffer.buffer(String.valueOf(HttpHeaders.createOptimized("Bad Request")));
    private static final Buffer INTERNAL_SERVER_ERROR_MSG = Buffer.buffer(String.valueOf(HttpHeaders.createOptimized("Internal Server Error")));
    private static final Buffer NOT_FOUND_MSG = Buffer.buffer(String.valueOf(HttpHeaders.createOptimized("Not Found")));
    private static final Buffer UNAUTHORIZED = Buffer.buffer(String.valueOf(HttpHeaders.createOptimized("Unauthorized")));
    private static final Buffer OK = Buffer.buffer(String.valueOf(HttpHeaders.createOptimized("Ok")));
    private static final Buffer ACCEPTED = Buffer.buffer(String.valueOf(HttpHeaders.createOptimized("Accepted")));
    private static final Buffer NO_CONTENT = Buffer.buffer(String.valueOf(HttpHeaders.createOptimized("No content")));
    private static final Buffer CONFLICT = Buffer.buffer(String.valueOf(HttpHeaders.createOptimized("Conflict")));

    private static final int OK_CODE = 200;
    private static final int ACCEPTED_CODE = 202;
    private static final int NO_CONTENT_CODE = 204;
    private static final int BAD_REQUEST_CODE = 400;
    private static final int UNAUTHORIZED_CODE = 401;
    private static final int NOT_FOUND_CODE = 404;
    private static final int CONFLICT_CODE = 409;
    private static final int INTERNAL_SERVER_ERROR_CODE = 500;

    public static void ok(RoutingContext ctx) {
        ok(ctx, MimeType.TXT_PLAIN, OK);
    }

    public static void jsonOk(RoutingContext ctx, Object msg) {
        ok(ctx, MimeType.APP_JSON, Json.encodeToBuffer(msg));
    }

    public static void ok(RoutingContext ctx, MimeType mimeType, Buffer msg) {
        ctx.request().response().setStatusCode(OK_CODE);
        Http11ResponseWriter.write(ctx, mimeType, msg);
    }

    public static void accepted(RoutingContext ctx) {
        ctx.request().response().setStatusCode(ACCEPTED_CODE);
        Http11ResponseWriter.write(ctx, MimeType.TXT_PLAIN, ACCEPTED);
    }

    public static void noContent(RoutingContext ctx) {
        ctx.request().response().setStatusCode(NO_CONTENT_CODE);
        Http11ResponseWriter.write(ctx, MimeType.TXT_PLAIN, NO_CONTENT);
    }

    public static void badRequest(RoutingContext ctx) {
        badRequest(ctx, MimeType.TXT_PLAIN);
    }

    public static void badRequest(RoutingContext ctx, Buffer buffer, MimeType mimeType) {
        ctx.request().response().setStatusCode(BAD_REQUEST_CODE);
        Http11ResponseWriter.write(ctx, mimeType, buffer);
    }

    public static void badRequestJson(RoutingContext ctx, Object msg) {
        ctx.request().response().setStatusCode(BAD_REQUEST_CODE);
        Http11ResponseWriter.write(ctx, MimeType.APP_JSON, Json.encodeToBuffer(msg));
    }

    public static void unauthorized(RoutingContext ctx) {
        ctx.request().response().setStatusCode(UNAUTHORIZED_CODE);
        Http11ResponseWriter.write(ctx,  MimeType.TXT_PLAIN, UNAUTHORIZED);
    }

    public static void unauthorized(RoutingContext ctx, MimeType type, Buffer msg) {
        ctx.request().response().setStatusCode(UNAUTHORIZED_CODE);
        Http11ResponseWriter.write(ctx,  type, msg);
    }

    public static void badRequest(RoutingContext ctx, MimeType mimeType) {
        Http11ResponseWriter.write(ctx, mimeType, BAD_REQUEST_MSG);
    }

    public static void notFound(RoutingContext ctx) {
        ctx.request().response().setStatusCode(NOT_FOUND_CODE);
        Http11ResponseWriter.write(ctx,  MimeType.TXT_PLAIN, NOT_FOUND_MSG);
    }

    public static void conflict(RoutingContext ctx) {
        ctx.request().response().setStatusCode(CONFLICT_CODE);
        Http11ResponseWriter.write(ctx, MimeType.TXT_PLAIN, CONFLICT);
    }

    public static void conflict(RoutingContext ctx, Buffer buffer, MimeType mimeType) {
        ctx.request().response().setStatusCode(CONFLICT_CODE);
        Http11ResponseWriter.write(ctx, mimeType, buffer);
    }

    public static void internalServerError(RoutingContext ctx) {
        ctx.request().response().setStatusCode(INTERNAL_SERVER_ERROR_CODE);
        Http11ResponseWriter.write(ctx, MimeType.TXT_PLAIN, INTERNAL_SERVER_ERROR_MSG);
    }

    public static void internalServerError(RoutingContext ctx, Throwable exc) {
        ctx.request().response().setStatusCode(INTERNAL_SERVER_ERROR_CODE);
        Http11ResponseWriter.write(ctx, MimeType.TXT_PLAIN, Buffer.buffer(exc.getMessage()));
    }
}
