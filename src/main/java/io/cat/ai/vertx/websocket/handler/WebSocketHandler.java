package io.cat.ai.vertx.websocket.handler;

import com.typesafe.config.Config;

import io.cat.ai.model.ResponseMessage;
import io.cat.ai.vertx.websocket.cache.WsChannelCache;
import io.cat.ai.vertx.websocket.cache.WsChannelCacheFactory;
import io.cat.ai.vertx.websocket.processor.WebSocketFrameProcessor;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.ServerWebSocket;

import static io.cat.ai.vertx.websocket.WebSocketChannel.writeFrame;

public final class WebSocketHandler implements Handler<ServerWebSocket> {

    private static final ResponseMessage incorrectPathMsg = new ResponseMessage("Incorrect path");
    private static final ResponseMessage okMsg = new ResponseMessage("Successfully connected to VertxCrud Websocket; To use methods reconnect to /api");
    private static final ResponseMessage wsApiOkMsg = new ResponseMessage("Successfully connected to Websocket API");

    private final WsChannelCache cache;
    private final ResponseMessage apiInfoMsg;

    private final WebSocketFrameProcessor wsFrameProcessor;

    public WebSocketHandler(Vertx vertx, Config config) {
        this.cache = WsChannelCacheFactory.newWsChannelCache();

        this.wsFrameProcessor = new WebSocketFrameProcessor(vertx, config, cache);

        this.apiInfoMsg = new ResponseMessage(config.getString("vertx.ws.api.info"));
    }

    public void handle(ServerWebSocket websocket) {
        cache.putChannelIfAbsent(websocket);

        switch (websocket.path()) {
            case "/":
                writeFrame(okMsg, websocket);
                break;

            case "/info":
                writeFrame(apiInfoMsg, websocket);
                break;

            case "/api":
                writeFrame(wsApiOkMsg, websocket);
                wsFrameProcessor.process(websocket);
                break;

            default:
                writeFrame(incorrectPathMsg, websocket);
        }

    }
}
