package io.cat.ai.core.executor;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

import java.util.List;

public interface VertxDbTaskExecutor<T, R> extends TaskExecutor<String, T, Handler<AsyncResult<R>>> {

    void executeSingle(String query, Handler<AsyncResult<R>> asyncResultHandler, String... args);

    void executeBatch(String sql, Handler<AsyncResult<R>> asyncResultHandler, List<T> batch);

}
