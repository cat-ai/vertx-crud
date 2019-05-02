package io.cat.ai.vertx.http.handler;

import com.typesafe.config.Config;

import io.cat.ai.model.Http11RequestMessage;
import io.cat.ai.model.ResponseMessage;
import io.cat.ai.vertx.http.service.HttpCrudService;
import io.cat.ai.vertx.http.service.HttpCrudServiceFactory;

import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;

import lombok.val;

import static io.cat.ai.vertx.VertxRequestUtil.*;
import static io.cat.ai.vertx.http.Http11Channel.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Http11RequestHandler {

    private static final ResponseMessage incorrectBodyMsg = new ResponseMessage("Incorrect JSON body");

    private final HttpCrudService service;

    public Http11RequestHandler(Vertx vertx, Config config) {
        this.service = HttpCrudServiceFactory.newHttpCrudService(vertx, config);
    }

    public void handleBase(RoutingContext ctx) {
        ok(ctx);
    }

    public void handleGet(RoutingContext ctx) {
        val nickname = getNicknameParam(ctx);

        val email = queryParam(ctx, "email");

        val name = queryParam(ctx, "name");

        if (isNull(nickname)) noContent(ctx);

        else if (nonNull(email) && nonNull(name))
            service.findOrCreate(ctx, email, name, nickname);

        else badRequest(ctx);
    }

    public void handlePost(RoutingContext ctx) {
        val json = parseBody(ctx, Http11RequestMessage.class);

        if (nonNull(json))
            service.updateClient(ctx, json.getMsg().getEmail(), json.getMsg().getNickname());

        else badRequestJson(ctx, incorrectBodyMsg);
    }

    public void handlePut(RoutingContext ctx) {
        val nickname = getNicknameParam(ctx);

        val email = queryParam(ctx, "email");

        val name = queryParam(ctx, "name");

        if (isNull(nickname)) noContent(ctx);

        else if (nonNull(email) && nonNull(name))
            service.addNew(ctx, email, name, nickname);

        else badRequest(ctx);
    }

    public void handleDelete(RoutingContext ctx) {
        val nickname = getNicknameParam(ctx);

        if (nonNull(nickname)) service.remove(ctx, nickname);

        else noContent(ctx);
    }

    private String getNicknameParam(RoutingContext ctx) {
        return pathParam(ctx, "nickname");
    }
}
