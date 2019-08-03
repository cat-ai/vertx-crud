package io.cat.ai.app.crud;

import com.typesafe.config.Config;

import io.cat.ai.app.executor.TaskExecutorFactory;
import io.cat.ai.core.crud.VertxHttpCrud;
import io.cat.ai.core.executor.VertxDbTaskExecutor;
import io.cat.ai.model.User;
import io.cat.ai.model.ResponseMessage;
import io.cat.ai.vertx.http.Http11Channel;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;

import lombok.val;

// not tested!
// Native transport (KQueue x86-64) not supported for MySQL client
public class VertxMySqlHttpCrud implements VertxHttpCrud {

    private static final Logger logger = LoggerFactory.getLogger(VertxMySqlHttpCrud.class);

    private final VertxDbTaskExecutor<String, JsonArray> executor;

    VertxMySqlHttpCrud(Vertx vertx, Config config) {
        this.executor = TaskExecutorFactory.newMySqlTaskExecutor(vertx, config);
    }

    @Override
    public void select(final RoutingContext ctx, final String... params) {
        executor.executeSingle(
                "", // implement with MySQL dialect
                asyncResult -> {
                    if (asyncResult.succeeded()) {
                        val row = asyncResult.result();

                        val client = new User(row.getString(1), row.getString(2), row.getString(3));

                        Http11Channel.jsonOk(ctx, new ResponseMessage(client));
                    } else {
                        logger.error(asyncResult.cause());
                        Http11Channel.internalServerError(ctx, asyncResult.cause());
                    }
                },
                params
        );
    }

    @Override
    public void update(final RoutingContext ctx, final String... params) {
        executor.executeSingle(
                "", // implement with MySQL dialect
                asyncResult -> {
                    if (asyncResult.succeeded()) {
                        val row = asyncResult.result();
                        Http11Channel.jsonOk(ctx, new ResponseMessage(row.getString(0)));
                    } else {
                        logger.error(asyncResult.cause());
                        Http11Channel.internalServerError(ctx, asyncResult.cause());
                    }
                },
                params
        );
    }

    @Override
    public void delete(final RoutingContext ctx, final String... params) {
        executor.executeSingle(
                "", // implement with MySQL dialect
                asyncResult -> {
                    if (asyncResult.succeeded()) {
                        Http11Channel.jsonOk(ctx, new ResponseMessage(asyncResult.result().getString(0)));
                    } else {
                        logger.error(asyncResult.cause());
                        Http11Channel.internalServerError(ctx, asyncResult.cause());
                    }
                },
                params
        );
    }

    @Override
    public void insert(final RoutingContext ctx, final String... params) {
        executor.executeSingle(
                "", // implement with MySQL dialect
                asyncResult -> {
                    if (asyncResult.succeeded()) {
                        val row = asyncResult.result();

                        Http11Channel.jsonOk(ctx, new ResponseMessage(row.getString(1)));
                    } else {
                        logger.error(asyncResult.cause());
                        Http11Channel.internalServerError(ctx, asyncResult.cause());
                    }
                },
                params
        );
    }
}
