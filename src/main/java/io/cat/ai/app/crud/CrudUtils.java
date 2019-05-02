package io.cat.ai.app.crud;

import io.reactiverse.pgclient.Tuple;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CrudUtils {

    public static final String SELECT_IF_EXISTS_OR_CREATE_IF_NOT =
            "with sel as (\n" +
                    "    select id, \"email\", \"name\", \"nickname\"\n" +
                    "    from client\n" +
                    "    where email=$1 and nickname=$3\n" +
                    "), ins as (\n" +
                    "    insert into client (\"email\", \"name\", \"nickname\")\n" +
                    "    select $1, $2, $3\n" +
                    "    where not exists (select 1 from sel)\n" +
                    "    returning id, \"email\", \"name\",  \"nickname\"\n" +
                    ")\n" +
                    "select id, \"email\", \"name\", \"nickname\"\n" +
                    "from ins\n" +
                    "union all\n" +
                    "select id, \"email\", \"name\", \"nickname\"\n" +
                    "from sel";

    public static final String UPDATE_CLIENT_SET_EMAIL = "update client set email=$1 where nickname=$2";

    public static final String REMOVE_CLIENT = "delete from client where nickname=$1";

    public static final String INSERT_CLIENT = "insert into client (email, name, nickname) values ($1, $2 ,$3) RETURNING id, nickname";

    public static Tuple pgQueryArg(String... args) {
        // TODO refactor
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
                return Tuple.of(args); // fix
        }
    }
}
