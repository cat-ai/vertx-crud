package io.cat.ai.app.crud;

import io.cat.ai.core.crud.VertxHttpCrud;

import io.vertx.ext.web.RoutingContext;

public class VertxMySqlHttpCrud implements VertxHttpCrud {

    VertxMySqlHttpCrud() {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public void select(RoutingContext ctx, String... params) {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public void update(RoutingContext ctx, String... params) {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public void delete(RoutingContext ctx, String... params) {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public void insert(RoutingContext ctx, String... params) {
        throw new RuntimeException("Not implemented!");
    }
}
