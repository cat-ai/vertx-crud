package io.cat.ai.vertx.websocket.handler;

import com.typesafe.config.Config;

import io.cat.ai.model.ResponseMessage;
import io.cat.ai.vertx.websocket.WebSocketFrameWriter;
import io.cat.ai.vertx.websocket.cache.WebSocketChannelCache;
import io.cat.ai.vertx.websocket.cache.WsChannelCacheFactory;
import io.cat.ai.vertx.websocket.processor.WebSocketFrameProcessor;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.ServerWebSocket;

public final class WebSocketHandler implements Handler<ServerWebSocket> {

    private static final ResponseMessage INVALID_PATH_MSG = new ResponseMessage("Invalid path");
    private static final ResponseMessage OK_MSG = new ResponseMessage("Successfully connected to VertxCrud Websocket; To use methods reconnect to /api");
    private static final ResponseMessage WS_API_OK_MSG = new ResponseMessage("Successfully connected to Websocket API");

    private final WebSocketChannelCache<ServerWebSocket> cache;
    private final ResponseMessage apiInfoMsg;

    private final WebSocketFrameProcessor wsFrameProcessor;

    public WebSocketHandler(Vertx vertx, Config config) {
        this.cache = WsChannelCacheFactory.getInstance();
        this.wsFrameProcessor = new WebSocketFrameProcessor(vertx, config, cache);
        this.apiInfoMsg = new ResponseMessage(config.getString("vertx.ws.api.info"));
    }

    public void handle(ServerWebSocket websocket) {
        cache.putChannel(websocket);

        switch (websocket.path()) {
            case "/":
                WebSocketFrameWriter.writeFrame(OK_MSG, websocket);
                break;

            case "/info":
                WebSocketFrameWriter.writeFrame(apiInfoMsg, websocket);
                break;

            case "/api":
                WebSocketFrameWriter.writeFrame(WS_API_OK_MSG, websocket);
                wsFrameProcessor.process(websocket);
                break;

            default:
                WebSocketFrameWriter.writeFrame(INVALID_PATH_MSG, websocket);
        }

    }
}