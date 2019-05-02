package io.cat.ai.vertx.websocket.cache;

public interface WebSocketChannelCache<T> {

    void putChannel(T t);

    void putChannelIfAbsent(T t);

    void removeChannel(T t);

    boolean isChannelExists(T t);

}
