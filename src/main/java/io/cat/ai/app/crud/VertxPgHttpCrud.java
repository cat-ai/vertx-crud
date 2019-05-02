package io.cat.ai.app.crud;

import com.typesafe.config.Config;

import io.cat.ai.app.executor.PgTaskExecutor;
import io.cat.ai.app.executor.TaskExecutorFactory;
import io.cat.ai.core.crud.VertxHttpCrud;
import io.cat.ai.model.Client;
import io.cat.ai.model.ResponseMessage;

import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;

import lombok.val;

import static io.cat.ai.app.crud.CrudUtils.*;
import static io.cat.ai.vertx.http.Http11Channel.*;

public class VertxPgHttpCrud implements VertxHttpCrud {

    private static final Logger logger = LoggerFactory.getLogger(VertxPgHttpCrud.class);

    private final PgTaskExecutor executor;

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

                            val client = new Client(column.getString(1), column.getString(2), column.getString(3));

                            jsonOk(ctx, new ResponseMessage(client));
                        }
                    }
                    else {
                        logger.error(asyncResult.cause());
                        badRequestJson(ctx, asyncResult.cause());
                    }
                },
                params);
    }

    @Override
    public void update(final RoutingContext ctx, final String... params) {
        executor.executeSingle(
                UPDATE_CLIENT_SET_EMAIL,
                asyncResult -> {

                    if (asyncResult.succeeded())
                        jsonOk(ctx, new ResponseMessage("Email changed to " + params[0]));

                    else {
                        logger.error(asyncResult.cause());
                        internalServerError(ctx);
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

                    if (asyncResult.succeeded())
                        jsonOk(ctx, new ResponseMessage("Removed client: " + params[0]));

                    else {
                        logger.error(asyncResult.cause());
                        internalServerError(ctx);
                    }
                },
                params);
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
                            jsonOk(ctx, new ResponseMessage("Created new client: " + row.getString(1)));
                        }
                    }
                    else {
                        logger.error(asyncResult.result());
                        internalServerError(ctx);
                    }
                },
                params
        );
    }
}
