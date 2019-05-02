package io.cat.ai.vertx.websocket.cache;

import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.impl.ConcurrentHashSet;

public class WsChannelCache extends ConcurrentHashSet<ServerWebSocket> implements WebSocketChannelCache<ServerWebSocket> {

    @Override
    public void putChannel(ServerWebSocket channel) {
        add(channel);
    }

    @Override
    public void putChannelIfAbsent(ServerWebSocket webSocket) {
        if (!contains(webSocket))
            add(webSocket);
    }

    @Override
    public void removeChannel(ServerWebSocket channel) {
        remove(channel);
    }

    @Override
    public boolean isChannelExists(ServerWebSocket webSocket) {
        return contains(webSocket);
    }
}
