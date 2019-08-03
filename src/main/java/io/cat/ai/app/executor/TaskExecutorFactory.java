package io.cat.ai.app.executor;

import com.typesafe.config.Config;

import io.vertx.core.Vertx;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskExecutorFactory {

    public static PgTaskExecutor newPgTaskExecutor(final Vertx vertx, final Config config) {
        return new PgTaskExecutor(vertx, config);
    }

    public static MySqlTaskExecutor newMySqlTaskExecutor(final Vertx vertx, final Config config) {
        return new MySqlTaskExecutor(vertx, config);
    }
}
