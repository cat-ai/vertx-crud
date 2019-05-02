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

public class WebSocketServerNode implements Node {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketServerNode.class);

    private final Set<Vertx> vertxSet = new HashSet<>();

    private final Config config;

    @Inject
    public WebSocketServerNode(Config config) {
        this.config = config;
    }

    @Override
    public void startNode() {
        logger.info("Starting WebSocketServerNode");

        val vertx = Vertx.vertx(new VertxOptions().setPreferNativeTransport(config.getBoolean("vertx.ws.preferNativeTransport")));

        if (vertx.isNativeTransportEnabled())
            logger.info("WebSocketServerNode native transport enabled: using " + Transport.nativeTransport().getClass().getSimpleName());
        else
            logger.debug("WebSocketServerNode native transport is not enabled");

        vertx.deployVerticle(
                () -> VerticleFactory.newWebsocketVerticle(config),

                new DeploymentOptions().setInstances(CpuCoreSensor.availableProcessors()), asyncRes -> {

                    if (asyncRes.succeeded())
                        logger.debug("WebSocketServerNode started!");
                    else
                        logger.error("Unable to start WebSocketServerNode", asyncRes.cause());
                });

        vertxSet.add(vertx);
        logger.info("WebSocketServerNode successfully started!");
    }

    @Override
    public void stopNode() {
        logger.warn("WebSocketServerNode will stopped");
        vertxSet.forEach(Vertx::close);
        logger.info("WebSocketServerNode stopped!");
    }
}
