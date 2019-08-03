package io.cat.ai.app.node;

import com.google.inject.Inject;

import com.typesafe.config.Config;

import io.cat.ai.vertx.VerticleFactory;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.impl.cpu.CpuCoreSensor;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.net.impl.transport.Transport;

import lombok.val;

import java.util.HashSet;
import java.util.Set;

public class Http11ServerNode implements Node {

    private static final Logger logger = LoggerFactory.getLogger(Http11ServerNode.class);

    private final Set<Vertx> vertxSet = new HashSet<>();

    private final Config config;

    @Inject
    public Http11ServerNode(Config config) {
        this.config = config;
    }

    @Override
    public void startNode() {
        logger.info("Starting Http11ServerNode");

        val vertx = Vertx.vertx(new VertxOptions().setPreferNativeTransport(config.getBoolean("vertx.http.preferNativeTransport")));

        if (vertx.isNativeTransportEnabled()) {
            logger.info("Http11ServerNode native transport enabled: using " + Transport.nativeTransport().getClass().getSimpleName());
        } else {
            logger.debug("Http11ServerNode native transport is not enabled");
        }

        vertx.deployVerticle(
                () -> VerticleFactory.newHttp11Verticle(config),

                new DeploymentOptions().setInstances(CpuCoreSensor.availableProcessors() * 2), event -> {

                    if (event.succeeded()) {
                        logger.debug("Http11ServerNode started!");
                    } else {
                        logger.error("Unable to start Http11ServerNode", event.cause());
                    }
                });

        vertxSet.add(vertx);
        logger.info("Http11ServerNode successfully started!");
    }

    @Override
    public void stopNode() {
        logger.warn("Http11ServerNode will stopped");
        vertxSet.forEach(Vertx::close);
        logger.info("Http11ServerNode stopped!");
    }
}