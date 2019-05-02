package io.cat.ai.app.executor;

import com.typesafe.config.Config;

import io.cat.ai.core.executor.VertxDbTaskExecutor;

import io.reactiverse.pgclient.*;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

import lombok.val;

import java.util.List;

import static io.cat.ai.app.crud.CrudUtils.pgQueryArg;

public class PgTaskExecutor implements VertxDbTaskExecutor<Tuple, PgRowSet> {

    private final PgPool pool;
    private final PgClient client;

    PgTaskExecutor(Vertx vertx, Config config) {
        val options = new PgPoolOptions();
        options.setDatabase(config.getString("pg.database"));
        options.setHost(config.getString("pg.host"));
        options.setPort(config.getInt("pg.port"));
        options.setUser(config.getString("pg.cred.username"));
        options.setPassword(config.getString("pg.cred.password"));
        options.setReusePort(config.getBoolean("pg.pool.reusePort"));
        options.setTcpQuickAck(config.getBoolean("pg.pool.tcpQuickAck"));
        options.setTcpFastOpen(config.getBoolean("pg.pool.tcpFastOpen"));
        options.setCachePreparedStatements(config.getBoolean("pg.pool.cachePrepStatement"));

        pool = PgClient.pool(vertx, new PgPoolOptions(options).setMaxSize(config.getInt("pg.pool.conn_size")));
        client = PgClient.pool(vertx, new PgPoolOptions(options).setMaxSize(config.getInt("pg.pool.client_size")));
    }

    @Override
    public void executeSingle(String query, Handler<AsyncResult<PgRowSet>> asyncResultHandler, String... args) {
        client.preparedQuery(query, pgQueryArg(args), asyncResultHandler);
    }

    @Override
    public void executeBatch(final String query,
                             final Handler<AsyncResult<PgRowSet>> asyncResultHandler,
                             final List<Tuple> batch) {
        client.preparedBatch(query, batch, asyncResultHandler);
    }
}
