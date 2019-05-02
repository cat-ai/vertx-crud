package io.cat.ai.vertx.http;

import com.typesafe.config.Config;

import io.cat.ai.vertx.http.handler.Http11RequestHandler;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;

import lombok.*;

@NoArgsConstructor
public class Http11Verticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(Http11Verticle.class);

    @Setter private Config config;

    @Override
    public void start() {
        val router = Router.router(vertx);

        val httpPort = config.getInt("vertx.http.port");

        val handler = new Http11RequestHandler(vertx, config);

        router.route().handler(BodyHandler.create());

        router.route().handler(CookieHandler.create());

        router.get("/").handler(handler::handleBase);

        router.get("/api/:nickname").handler(handler::handleGet);

        router.post("/api").handler(handler::handlePost);

        router.put("/api/:nickname").handler(handler::handlePut);

        router.delete("/api/:nickname").handler(handler::handleDelete);

        vertx.createHttpServer(new HttpServerOptions()
                .setTcpQuickAck(config.getBoolean("vertx.http.tcpQuickAck"))
                .setReusePort(config.getBoolean("vertx.http.reusePort"))
                .setTcpFastOpen(config.getBoolean("vertx.http.tcpFastOpen")))
                .requestHandler(router::accept)
                .listen(httpPort);

        logger.info(this.getClass().getSimpleName() + " instance started at port: " + httpPort);
    }

}