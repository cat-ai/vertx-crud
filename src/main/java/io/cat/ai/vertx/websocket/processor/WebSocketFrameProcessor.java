package io.cat.ai.vertx.websocket.processor;

import com.typesafe.config.Config;

import io.cat.ai.app.executor.PgTaskExecutor;
import io.cat.ai.app.executor.TaskExecutorFactory;
import io.cat.ai.model.Client;
import io.cat.ai.model.ResponseMessage;
import io.cat.ai.model.WebsocketMessage;
import io.cat.ai.vertx.websocket.cache.WsChannelCache;

import io.vertx.core.Vertx;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import lombok.val;

import java.util.Optional;

import static io.cat.ai.app.crud.CrudUtils.*;
import static io.cat.ai.vertx.VertxRequestUtil.*;
import static io.cat.ai.vertx.websocket.WebSocketChannel.*;

public class WebSocketFrameProcessor {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketFrameProcessor.class);

    private static final ResponseMessage incorrectMsg = new ResponseMessage("Incorrect JSON message");
    private static final ResponseMessage incorrectMethodMsg = new ResponseMessage("Incorrect method");
    private static final ResponseMessage internalErrorMsg = new ResponseMessage("Something get wrong");

    private final WsChannelCache cache;
    private final PgTaskExecutor taskExecutor;

    public WebSocketFrameProcessor(Vertx vertx, Config config, WsChannelCache cache) {
        this.cache = cache;
        this.taskExecutor = TaskExecutorFactory.newPgTaskExecutor(vertx, config);
    }

    public void process(ServerWebSocket websocket) {
        websocket.frameHandler(frame -> {
            Optional<WebsocketMessage> wsReqMsgOpt;

            if (frame.isClose()) {
                cache.removeChannel(websocket);
                return;
            }
            else if (frame.isText()) wsReqMsgOpt = parseBodyOpt(frame.textData(), WebsocketMessage.class);
            else wsReqMsgOpt = parseBodyOpt(frame.binaryData(), WebsocketMessage.class);

            wsReqMsgOpt.ifPresent(__ -> {
                val msgBody = __.getMsg();

                switch (__.getMethod()) {

                    case "findOrCreate":
                        taskExecutor.executeSingle(
                                SELECT_IF_EXISTS_OR_CREATE_IF_NOT,
                                asyncResult -> {

                                    if (asyncResult.succeeded()) {
                                        val it = asyncResult.result().iterator();

                                        if (it.hasNext()) {
                                            val column = it.next();

                                            val client = new Client(column.getString(1), column.getString(2), column.getString(3));

                                            writeFrame(new ResponseMessage(client), websocket);
                                        }
                                    } else {
                                        logger.error("Client " + msgBody.getEmail() + " @findOrCreate method error: " + asyncResult.cause().getMessage());
                                        writeFrame(internalErrorMsg, websocket);
                                    }
                                },
                                msgBody.getNickname(), msgBody.getEmail(), msgBody.getName()
                        );
                        return;

                    case "addNew":
                        taskExecutor.executeSingle(
                                INSERT_CLIENT,
                                asyncResult -> {

                                    if (asyncResult.succeeded()) {
                                        val it = asyncResult.result().iterator();

                                        if (it.hasNext()) {
                                            val row = it.next();
                                            writeFrame(new ResponseMessage("Created new client: " + row.getString(1)), websocket);
                                        }
                                    }
                                    else {
                                        logger.error("Client " + msgBody.getEmail() + " @addNew method error: " + asyncResult.cause().getMessage());
                                        writeFrame(internalErrorMsg, websocket);
                                    }
                                },
                                msgBody.getEmail(), msgBody.getName(), msgBody.getNickname()
                        );
                        return;

                    case "remove":
                        taskExecutor.executeSingle(
                                REMOVE_CLIENT,
                                asyncResult -> {
                                    if (asyncResult.succeeded())
                                        writeFrame(new ResponseMessage("Removed client: " + msgBody.getNickname()), websocket);

                                    else {
                                        logger.error("Client " + msgBody.getEmail() + " @remove method error: " + asyncResult.cause().getMessage());
                                        writeFrame(internalErrorMsg, websocket);
                                    }
                                },
                                msgBody.getEmail()
                        );
                        return;

                    case "updateClient":
                        taskExecutor.executeSingle(
                                UPDATE_CLIENT_SET_EMAIL,
                                asyncResult -> {

                                    if (asyncResult.succeeded())
                                        writeFrame(new ResponseMessage("Email changed to " + msgBody.getEmail()), websocket);
                                    else {
                                        logger.error("Client " + msgBody.getNickname() + " @updateClient method error: " + asyncResult.cause().getMessage());
                                        writeFrame(internalErrorMsg, websocket);
                                    }
                                },
                                msgBody.getEmail()
                        );
                        return;

                    default:
                        writeFrame(incorrectMethodMsg, websocket);
                }
                writeFrame(incorrectMsg, websocket);
            });
        });
    }
}