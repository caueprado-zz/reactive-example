package com.reactiveexample.controller;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;

@RunWith(SpringRunner.class)
@WebFluxTest
public class FluxAndMonoControllerTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void webFluxClientTest() {
        Flux<Integer> flux = webTestClient.get()
                .uri("/flux")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(flux.log())
                .expectSubscription()
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .verifyComplete();
    }

    @Test
    public void webFluxClientTest2() {
        webTestClient.get()
                .uri("/flux")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(APPLICATION_JSON_UTF8)
                .expectBodyList(Integer.class)
                .contains(1, 2, 3, 4)
                .hasSize(4);
    }

    @Test
    public void webFluxStreamTest() {

        Flux<Integer> integers = webTestClient
                .get()
                .uri("/fluxStream")
                .accept(APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(integers.log())
                .expectSubscription()
                .expectNext(1, 2, 3, 4)
//                .thenAwait(Duration.ofSeconds(1))
                .thenCancel()
                .verify();
    }

    @Test
    public void webFluxStreamTest2() {

        webTestClient.get()
                .uri("/fluxStream")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Integer.class)
                .hasSize(150);
    }

    @Test
    public void webMonoTest() {

        Integer expected = 1;

        webTestClient.get()
                .uri("/mono")
                .accept(APPLICATION_JSON_UTF8)
                .exchange()
                .returnResult(Integer.class)
                .consumeWith((response) -> {
                    Assertions.assertThat(expected).isEqualTo(response);
                });
    }
}
