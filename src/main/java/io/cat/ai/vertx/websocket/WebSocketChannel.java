package io.cat.ai.vertx.websocket;

import io.netty.buffer.Unpooled;

import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.impl.FrameType;
import io.vertx.core.http.impl.ws.WebSocketFrameImpl;
import io.vertx.core.json.Json;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WebSocketChannel {

    public static void writeFrame(Object msg, ServerWebSocket serverWebSocket, boolean isClosed) {
        if (!isClosed)
            writeFrame(Json.encodePrettily(msg), serverWebSocket);
    }

    public static void writeFrame(Object msg, ServerWebSocket serverWebSocket) {
        writeFrame(Json.encodePrettily(msg), serverWebSocket);
    }

    public static void writeFrame(String textData, ServerWebSocket serverWebSocket) {
        serverWebSocket.writeFrame(new WebSocketFrameImpl(textData));
    }

    public static void writeFrame(FrameType type, byte[] bytes, ServerWebSocket serverWebSocket) {
        serverWebSocket.writeFrame(new WebSocketFrameImpl(type, Unpooled.wrappedBuffer(bytes)));
    }

}
