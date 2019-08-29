package com.reactiveexample.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class HandlerFunction {

    public Mono<ServerResponse> flux(ServerRequest serverRequest) {
        return ServerResponse.accepted()
                .body(
                        Flux.just(1, 2, 3, 4).log(),
                        Integer.class);
    }
}
