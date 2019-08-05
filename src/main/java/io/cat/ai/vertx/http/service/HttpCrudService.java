package io.cat.ai.vertx.http.service;

import com.typesafe.config.Config;

import io.cat.ai.app.crud.CrudFactory;
import io.cat.ai.core.crud.VertxHttpCrud;

import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;

public class HttpCrudService {

    private final VertxHttpCrud crud;

    HttpCrudService(Vertx vertx, Config conf) {
        this.crud = CrudFactory.newVertxHttpCrud(vertx, conf);
    }

    public void findClient(final RoutingContext ctx, final String... params) {
        crud.select(ctx, params);
    }

    public void createClient(final RoutingContext ctx, final String... params) {
        crud.insert(ctx, params);
    }

    public void removeClient(final RoutingContext ctx, final String... params) {
        crud.delete(ctx, params);
    }

    public void updateClient(final RoutingContext ctx, final String... params) {
        crud.update(ctx, params);
    }
}