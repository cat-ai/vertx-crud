package io.cat.ai.vertx.websocket.processor;

import com.typesafe.config.Config;

import io.cat.ai.app.executor.PgTaskExecutor;
import io.cat.ai.app.executor.TaskExecutorFactory;
import io.cat.ai.model.User;
import io.cat.ai.model.ResponseMessage;
import io.cat.ai.model.WebsocketMessage;
import io.cat.ai.vertx.VertxRequestUtil;
import io.cat.ai.vertx.websocket.WebSocketFrameWriter;
import io.cat.ai.vertx.websocket.cache.WebSocketChannelCache;

import io.reactiverse.pgclient.PgRowSet;

import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import lombok.val;

import java.util.Optional;

import static io.cat.ai.app.crud.CrudUtils.*;

public class WebSocketFrameProcessor {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketFrameProcessor.class);

    private static final ResponseMessage INVALID_MSG = new ResponseMessage("Invalid JSON message");
    private static final ResponseMessage INVALID_METHOD_MSG = new ResponseMessage("Invalid method");
    private static final ResponseMessage CONFLICT_MSG = new ResponseMessage("Conflict");

    private final WebSocketChannelCache<ServerWebSocket> cache;
    private final PgTaskExecutor taskExecutor;

    public WebSocketFrameProcessor(Vertx vertx, Config config, WebSocketChannelCache<ServerWebSocket> cache) {
        this.cache = cache;
        this.taskExecutor = TaskExecutorFactory.newPgTaskExecutor(vertx, config);
    }

    private void getNicknameFromRowAndWriteFrame(   ServerWebSocket websocket, AsyncResult<PgRowSet> asyncResult) {
        val it = asyncResult.result().iterator();

        if (it.hasNext()) {
            val row = it.next();
            WebSocketFrameWriter.writeFrame(new ResponseMessage(row.getString("nickname")), websocket);
        } else {
            WebSocketFrameWriter.writeFrame(CONFLICT_MSG, websocket);
        }
    }

    public void process(ServerWebSocket websocket) {
        websocket.frameHandler(frame -> {
            Optional<WebsocketMessage> wsReqMsgOpt;

            if (frame.isClose()) {
                cache.removeChannel(websocket);
                return;
            } else if (frame.isText()) {
                wsReqMsgOpt = VertxRequestUtil.parseBodyOpt(frame.textData(), WebsocketMessage.class);
            } else {
                wsReqMsgOpt = VertxRequestUtil.parseBodyOpt(frame.binaryData(), WebsocketMessage.class);
            }

            wsReqMsgOpt.ifPresent(__ -> {
                val msgBody = __.getMsg();

                switch (__.getMethod()) {

                    case "findClient":
                        taskExecutor.executeSingle(
                                SELECT_CLIENT,
                                asyncResult -> {
                                    if (asyncResult.succeeded()) {
                                        val it = asyncResult.result().iterator();

                                        if (it.hasNext()) {
                                            val row = it.next();
                                            val client = new User(row.getString("email"), row.getString("name"), row.getString("nickname"));

                                            WebSocketFrameWriter.writeFrame(new ResponseMessage(client), websocket);
                                        }
                                    } else {
                                        logger.error("User " + msgBody.getEmail() + " @findClient method error: " + asyncResult.cause().getMessage());
                                        WebSocketFrameWriter.writeFrame(CONFLICT_MSG, websocket);
                                    }
                                },
                                msgBody.getNickname(), msgBody.getEmail(), msgBody.getName()
                        );
                        return;

                    case "createClient":
                        taskExecutor.executeSingle(
                                INSERT_CLIENT,
                                asyncResult -> {
                                    if (asyncResult.succeeded()) {
                                        getNicknameFromRowAndWriteFrame(websocket, asyncResult);
                                    } else {
                                        logger.error("User " + msgBody.getEmail() + " @createClient method error: " + asyncResult.cause().getMessage());
                                        WebSocketFrameWriter.writeFrame(CONFLICT_MSG, websocket);
                                    }
                                },
                                msgBody.getEmail(), msgBody.getName(), msgBody.getNickname()
                        );
                        return;

                    case "removeClient":
                        taskExecutor.executeSingle(
                                REMOVE_CLIENT,
                                asyncResult -> {
                                    if (asyncResult.succeeded()) {
                                        getNicknameFromRowAndWriteFrame(websocket, asyncResult);
                                    } else {
                                        logger.error("User " + msgBody.getEmail() + " @removeClient method error: " + asyncResult.cause().getMessage());
                                        WebSocketFrameWriter.writeFrame(CONFLICT_MSG, websocket);
                                    }
                                },
                                msgBody.getNickname()
                        );
                        return;

                    case "updateClient":
                        taskExecutor.executeSingle(
                                UPDATE_CLIENT_SET_EMAIL,
                                asyncResult -> {
                                    if (asyncResult.succeeded()) {
                                        val it = asyncResult.result().iterator();
                                        if (it.hasNext()) {
                                            val row = it.next();
                                            WebSocketFrameWriter.writeFrame(new ResponseMessage(row.getString("email")), websocket);
                                        }
                                    } else {
                                        logger.error("User " + msgBody.getNickname() + " @updateClient method error: " + asyncResult.cause().getMessage());
                                        WebSocketFrameWriter.writeFrame(CONFLICT_MSG, websocket);
                                    }
                                },
                                msgBody.getEmail(), msgBody.getNickname()
                        );
                        return;

                    default:
                        WebSocketFrameWriter.writeFrame(INVALID_METHOD_MSG, websocket);
                }
                WebSocketFrameWriter.writeFrame(INVALID_MSG, websocket);
            });
        });
    }
}