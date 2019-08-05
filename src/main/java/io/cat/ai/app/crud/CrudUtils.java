package io.cat.ai.app.crud;

import io.reactiverse.pgclient.Tuple;

import io.vertx.core.json.JsonArray;

import lombok.*;

import java.util.Arrays;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CrudUtils {

    public static final String SELECT_CLIENT = "select * from client where nickname=($1) and email=($2)";

    public static final String UPDATE_CLIENT_SET_EMAIL = "update client set email=($1) where nickname=($2) RETURNING email";

    public static final String REMOVE_CLIENT = "delete from client where nickname=($1) RETURNING nickname";

    public static final String INSERT_CLIENT = "insert into client (email, name, nickname) values ($1, $2 ,$3) RETURNING id, nickname";

    public static Tuple pgQueryArg(String... args) {
        switch(args.length) {
            case 1:
                return Tuple.of(args[0]);
            case 2:
                return Tuple.of(args[0], args[1]);
            case 3:
                return Tuple.of(args[0], args[1], args[2]);
            case 4:
                return Tuple.of(args[0], args[1], args[2], args[3]);
            case 5:
                return Tuple.of(args[0], args[1], args[2], args[3], args[4]);
            case 6:
                return Tuple.of(args[0], args[1], args[2], args[3], args[4], args[5]);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static JsonArray mySqlQueryArg(String... args) {
        // TODO refactor
        return new JsonArray(Arrays.asList(args));
    }
}
