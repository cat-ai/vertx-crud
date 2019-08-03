package io.cat.ai.core.crud;

import io.vertx.ext.web.RoutingContext;

public interface VertxHttpCrud extends AsyncCrud<RoutingContext, String> {

    void select(RoutingContext ctx, String... params);

    void update(RoutingContext ctx, String... params);

    void delete(RoutingContext ctx, String... params);

    void insert(RoutingContext ctx, String... params);
}
