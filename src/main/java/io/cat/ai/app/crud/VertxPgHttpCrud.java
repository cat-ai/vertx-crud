package io.cat.ai.app.crud;

import com.typesafe.config.Config;

import io.cat.ai.app.executor.TaskExecutorFactory;
import io.cat.ai.core.crud.VertxHttpCrud;
import io.cat.ai.core.executor.VertxDbTaskExecutor;
import io.cat.ai.model.User;
import io.cat.ai.model.ResponseMessage;
import io.cat.ai.vertx.http.Http11Channel;

import io.reactiverse.pgclient.*;

import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;

import lombok.val;

import static io.cat.ai.app.crud.CrudUtils.*;

public class VertxPgHttpCrud implements VertxHttpCrud {

    private static final Logger logger = LoggerFactory.getLogger(VertxPgHttpCrud.class);

    private final VertxDbTaskExecutor<Tuple, PgRowSet> executor;

    VertxPgHttpCrud(Vertx vertx, Config conf) {
        this.executor = TaskExecutorFactory.newPgTaskExecutor(vertx, conf);
    }

    @Override
    public void select(final RoutingContext ctx, final String... params) {
        executor.executeSingle(
                SELECT_IF_EXISTS_OR_CREATE_IF_NOT,
                asyncResult -> {
                    if (asyncResult.succeeded()) {
                        val it = asyncResult.result().iterator();

                        if (it.hasNext()) {
                            val column = it.next();
                            val client = new User(column.getString("email"), column.getString("name"), column.getString("nickname"));

                            Http11Channel.jsonOk(ctx, new ResponseMessage(client));
                        } else {
                            Http11Channel.internalServerError(ctx);
                        }
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
                UPDATE_CLIENT_SET_EMAIL,
                asyncResult -> {
                    if (asyncResult.succeeded()) {
                        val it = asyncResult.result().iterator();

                        if (it.hasNext()) {
                            val row = it.next();
                            Http11Channel.jsonOk(ctx, new ResponseMessage(row.getString("email")));
                        } else {
                            Http11Channel.notFound(ctx);
                        }
                    } else {
                        System.out.println("Error");
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
                REMOVE_CLIENT,
                asyncResult -> {
                    if (asyncResult.succeeded()) {
                        val it = asyncResult.result().iterator();

                        if (it.hasNext()) {
                            val row = it.next();
                            Http11Channel.jsonOk(ctx, new ResponseMessage(row.getString("nickname")));
                        } else {
                            Http11Channel.notFound(ctx);
                        }
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
                INSERT_CLIENT,
                asyncResult ->  {
                    if (asyncResult.succeeded()) {
                        val it = asyncResult.result().iterator();

                        if (it.hasNext()) {
                            val row = it.next();
                            Http11Channel.jsonOk(ctx, new ResponseMessage(row.getString("nickname")));
                        } else {
                            Http11Channel.internalServerError(ctx);
                        }
                    } else {
                        logger.error(asyncResult.result());
                        Http11Channel.internalServerError(ctx, asyncResult.cause());
                    }
                },
                params
        );
    }
}