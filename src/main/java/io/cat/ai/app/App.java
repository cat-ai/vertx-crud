package io.cat.ai.app;

import io.cat.ai.app.node.Node;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import lombok.val;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    private final Set<Node> nodeSet = new HashSet<>();

    public void addNode(final Node node) {
        nodeSet.add(node);
    }

    public void addNodes(final Node... node) {
        nodeSet.addAll(Arrays.asList(node));
    }

    public void removeNode(final Node node) {
        nodeSet.remove(node);
    }

    public void start() {
        val nodes = Stream
                .of(nodeSet)
                .flatMap(Collection::stream)
                .map(__ -> __.getClass().getSimpleName())
                .collect(Collectors.joining(", "));

        logger.info("Starting the application: application has " + nodeSet.size() + " nodes  { " + nodes + " }");
        nodeSet.forEach(Node::startNode);
        logger.info("Application successfully started!");
    }

    public void stop() {
        logger.warn("Stopping the application");
        nodeSet.forEach(Node::stopNode);
        logger.info("Application successfully stopped!");
    }
}
