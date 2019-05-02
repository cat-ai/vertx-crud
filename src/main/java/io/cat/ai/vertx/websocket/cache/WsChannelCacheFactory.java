package io.cat.ai.vertx.websocket.cache;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WsChannelCacheFactory {

    public static WsChannelCache newWsChannelCache() {
        return new WsChannelCache();
    }
}
