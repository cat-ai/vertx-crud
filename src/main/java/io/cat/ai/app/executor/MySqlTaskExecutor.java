package io.cat.ai.app.executor;

import com.typesafe.config.Config;

import io.cat.ai.app.crud.CrudUtils;
import io.cat.ai.core.executor.VertxDbTaskExecutor;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.SQLClient;

import lombok.val;

import java.util.List;

public class MySqlTaskExecutor implements VertxDbTaskExecutor<String, JsonArray> {

    private final SQLClient client;

    MySqlTaskExecutor(Vertx vertx, Config config) {
        val mySqlJsonObjConf = new JsonObject();

        mySqlJsonObjConf
                .put("username", config.getString("mysql.crud.username"))
                .put("password", config.getString("mysql.crud.password"))
                .put("database", config.getString("mysql.database"))
                .put("maxPoolSize", config.getInt("mysql.pool.maxPoolSize"))
                .put("host", config.getString("mysql.host"))
                .put("port", config.getInt("mysql.port"));

        this.client = MySQLClient.createNonShared(vertx, mySqlJsonObjConf);
    }

    @Override
    public void executeSingle(String query, Handler<AsyncResult<JsonArray>> asyncResult, String... params) {
        client.querySingleWithParams(query, CrudUtils.mySqlQueryArg(params), asyncResult);
    }

    @Override
    public void executeBatch(String sql, Handler<AsyncResult<JsonArray>> asyncResultHandler, List<String> batch) {
        throw new UnsupportedOperationException("Not implemented!");
    }
}
