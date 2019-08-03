package io.cat.ai.vertx.websocket.cache;

import io.vertx.core.http.ServerWebSocket;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WsChannelCacheFactory {

    public static WebSocketChannelCache<ServerWebSocket> getInstance() {
        return WsChannelCache.getInstance();
    }
}
