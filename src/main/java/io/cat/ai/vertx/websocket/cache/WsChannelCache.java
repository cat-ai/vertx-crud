package io.cat.ai.vertx.websocket.cache;

import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.impl.ConcurrentHashSet;

import lombok.*;

import static java.util.Objects.isNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class WsChannelCache extends ConcurrentHashSet<ServerWebSocket> implements WebSocketChannelCache<ServerWebSocket> {

    private static volatile WsChannelCache INSTANCE;

    @Override
    public void putChannel(ServerWebSocket channel) {
        super.add(channel);
    }

    @Override
    public void removeChannel(ServerWebSocket channel) {
        super.remove(channel);
    }

    @Override
    public boolean isChannelExists(ServerWebSocket webSocket) {
        return super.contains(webSocket);
    }

    static WsChannelCache getInstance() {
        var localInstance = INSTANCE;

        if (isNull(localInstance)) {
            synchronized (WsChannelCache.class) {
                localInstance = INSTANCE;
                if (isNull(localInstance)) {
                    INSTANCE = localInstance = new WsChannelCache();
                }
            }
        }
        return localInstance;
    }
}