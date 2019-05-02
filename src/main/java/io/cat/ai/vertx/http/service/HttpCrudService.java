package io.cat.ai.vertx.http.service;

import com.typesafe.config.Config;

import io.cat.ai.app.crud.CrudFactory;
import io.cat.ai.core.crud.VertxHttpCrud;

import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;

public class HttpCrudService {

    private final VertxHttpCrud pgCrud;

    HttpCrudService(Vertx vertx, Config conf) {
        this.pgCrud = CrudFactory.newVertxPgHttpCrud(vertx, conf);
    }

    public void findOrCreate(final RoutingContext ctx, final String... params) {
        pgCrud.select(ctx, params);
    }

    public void addNew(final RoutingContext ctx, final String... params) {
        pgCrud.insert(ctx, params);
    }

    public void remove(final RoutingContext ctx, final String... params) {
        pgCrud.delete(ctx, params);
    }

    public void updateClient(final RoutingContext ctx, final String... params) {
        pgCrud.update(ctx, params);
    }

}
