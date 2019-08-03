package io.cat.ai.model;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor @Getter @ToString
public class WebsocketMessage {

    private String method;

    private User msg;
}