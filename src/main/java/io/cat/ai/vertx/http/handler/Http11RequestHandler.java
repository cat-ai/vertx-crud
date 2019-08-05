package io.cat.ai.vertx.http.handler;

import com.typesafe.config.Config;

import io.cat.ai.model.Http11RequestMessage;
import io.cat.ai.model.ResponseMessage;
import io.cat.ai.vertx.VertxRequestUtil;
import io.cat.ai.vertx.http.Http11Channel;
import io.cat.ai.vertx.http.service.HttpCrudService;
import io.cat.ai.vertx.http.service.HttpCrudServiceFactory;

import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;

import lombok.val;

import static java.util.Objects.*;

public class Http11RequestHandler {

    private static final ResponseMessage INVALID_JSON_BODY = new ResponseMessage("Invalid JSON body");

    private final HttpCrudService service;

    public Http11RequestHandler(Vertx vertx, Config config) {
        this.service = HttpCrudServiceFactory.newHttpCrudService(vertx, config);
    }

    public void handleBase(RoutingContext ctx) {
        Http11Channel.ok(ctx);
    }

    public void handleGet(RoutingContext ctx) {
        val nickname = getNicknameParam(ctx);
        val email = VertxRequestUtil.queryParam(ctx, "email");

        if (isNull(nickname)) {
            Http11Channel.noContent(ctx);
        } else if (nonNull(email)) {
            service.findClient(ctx, nickname, email);
        } else {
            Http11Channel.badRequestJson(ctx, new ResponseMessage("email parameter is mandatory"));
        }
    }

    public void handlePost(RoutingContext ctx) {
        val requestMessage = VertxRequestUtil.parseBody(ctx, Http11RequestMessage.class);

        if (nonNull(requestMessage)) {
            val user = requestMessage.getMsg();
            service.updateClient(ctx, user.getEmail(), user.getNickname());
        } else {
            Http11Channel.badRequestJson(ctx, INVALID_JSON_BODY);
        }
    }

    public void handlePut(RoutingContext ctx) {
        val nickname = getNicknameParam(ctx);
        val email = VertxRequestUtil.queryParam(ctx, "email");
        val name = VertxRequestUtil.queryParam(ctx, "name");

        if (isNull(nickname)) {
            Http11Channel.noContent(ctx);
        } else if (nonNull(email) && nonNull(name)) {
            service.createClient(ctx, email, name, nickname);
        } else {
            Http11Channel.badRequestJson(ctx, new ResponseMessage("Missed mandatory parameter"));
        }
    }

    public void handleDelete(RoutingContext ctx) {
        val nickname = getNicknameParam(ctx);

        if (nonNull(nickname)) {
            service.removeClient(ctx, nickname);
        } else {
            Http11Channel.noContent(ctx);
        }
    }

    private String getNicknameParam(RoutingContext ctx) {
        return VertxRequestUtil.pathParam(ctx, "nickname");
    }
}