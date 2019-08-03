package io.cat.ai.app.crud;

import com.typesafe.config.Config;

import io.cat.ai.core.crud.VertxHttpCrud;

import io.vertx.core.Vertx;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CrudFactory {

    public static VertxHttpCrud newVertxPgHttpCrud(final Vertx vertx, final Config conf) {
        return new VertxPgHttpCrud(vertx, conf);
    }

    public static VertxHttpCrud newVertxMySqlHttpCrud(final Vertx vertx, final Config conf) {
        return new VertxMySqlHttpCrud(vertx, conf);
    }

    public static VertxHttpCrud newVertxHttpCrud(final Vertx vertx, final Config conf) {
        val crud = conf.getString("vertx.crud.description");

        switch (crud) {
            case "pg":
            case "postgre":
            case "postgres":
            case "postgresql":
            case "postgreSql":
            case "Pg":
            case "Postgre":
            case "Postgres":
            case "PostgreSql":
            case "PostgreSQL":
            case "POSTGRESQL":
                return newVertxPgHttpCrud(vertx, conf);

            case "mysql":
            case "mySql":
            case "Mysql":
            case "MySql":
            case "MYSQL":
                return newVertxMySqlHttpCrud(vertx, conf);

             default:
                throw new RuntimeException(crud + " not found");
        }
    }
}