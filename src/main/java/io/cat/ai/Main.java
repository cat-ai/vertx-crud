package io.cat.ai;

import com.google.inject.Guice;

import io.cat.ai.app.App;
import io.cat.ai.app.module.AppModule;
import io.cat.ai.app.node.Http11ServerNode;
import io.cat.ai.app.node.WebSocketServerNode;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import lombok.val;

import java.util.concurrent.CountDownLatch;

class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static volatile boolean isStarted = true;

    public static void main(String[] args) throws InterruptedException {
        val injector = Guice.createInjector(new AppModule());

        logger.info("Google Guice Injector created");

        val http11ServerNode = injector.getInstance(Http11ServerNode.class);
        val websocketServerNode = injector.getInstance(WebSocketServerNode.class);

        val app = new App();

        app.addNodes(http11ServerNode, websocketServerNode);

        app.start();

        val latch = new CountDownLatch(1);
        Runtime.getRuntime()
                .addShutdownHook(new Thread(() -> {
                    isStarted = true;
                    logger.warn("Application exited!");
                    app.stop();
                    latch.countDown();
                }));

        while (!isStarted)
            latch.await();
    }
}
