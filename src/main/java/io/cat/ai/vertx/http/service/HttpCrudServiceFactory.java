package io.cat.ai.vertx.http.service;

import com.typesafe.config.Config;

import io.vertx.core.Vertx;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpCrudServiceFactory {

    public static HttpCrudService newHttpCrudService(final Vertx vertx, final Config config) {
        return new HttpCrudService(vertx, config);
    }
}
