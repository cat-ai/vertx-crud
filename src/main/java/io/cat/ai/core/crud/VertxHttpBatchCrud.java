package io.cat.ai.core.crud;

import io.vertx.ext.web.RoutingContext;

import java.util.List;

public interface VertxHttpBatchCrud<T> extends AsyncCrud<RoutingContext, List<T>> {

    void select(RoutingContext ctx, List<T> batch);

    void update(RoutingContext ctx, List<T> batch);

    void delete(RoutingContext ctx, List<T> batch);

    void insert(RoutingContext ctx, List<T> batch);
}
