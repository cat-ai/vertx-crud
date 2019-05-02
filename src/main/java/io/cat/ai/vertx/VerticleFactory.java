package io.cat.ai.vertx;

import com.typesafe.config.Config;

import io.cat.ai.vertx.http.Http11Verticle;
import io.cat.ai.vertx.websocket.WebSocketVerticle;

import io.vertx.core.AbstractVerticle;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VerticleFactory {

    public static AbstractVerticle newHttp11Verticle(final Config config) {
        val http11Verticle = new Http11Verticle();
        http11Verticle.setConfig(config);
        return http11Verticle;
    }

    public static  AbstractVerticle newWebsocketVerticle(final Config config) {
        val websocketVerticle = new WebSocketVerticle();
        websocketVerticle.setConfig(config);
        return websocketVerticle;
    }
}
