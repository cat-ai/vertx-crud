package io.cat.ai.vertx.websocket;

import com.typesafe.config.Config;

import io.cat.ai.vertx.websocket.handler.WebSocketHandler;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerOptions;

import lombok.*;

@NoArgsConstructor
public class WebSocketVerticle extends AbstractVerticle {

    @Setter private Config config;

    @Override
    public void start() {
        val wsHandler = new WebSocketHandler(vertx, config);

        val wsPort = config.getInt("vertx.ws.port");

        vertx.createHttpServer(
                new HttpServerOptions()
                        .setTcpQuickAck(config.getBoolean("vertx.ws.tcpQuickAck"))
                        .setReusePort(config.getBoolean("vertx.ws.reusePort")))
                .websocketHandler(wsHandler::handle)
                .listen(wsPort);
    }
}