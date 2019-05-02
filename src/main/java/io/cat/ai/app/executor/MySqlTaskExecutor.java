package io.cat.ai.app.executor;

import io.cat.ai.core.executor.VertxDbTaskExecutor;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import javafx.util.Pair;

import javax.sql.RowSet;
import java.util.List;

public class MySqlTaskExecutor implements VertxDbTaskExecutor<Pair<String, String>, RowSet> {

    @Override
    public void executeSingle(String query, Handler<AsyncResult<RowSet>> asyncResultHandler, String... params) {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public void executeBatch(String sql, Handler<AsyncResult<RowSet>> asyncResultHandler, List<Pair<String, String>> batch) {
        throw new RuntimeException("Not implemented!");
    }
}
