package com.reactiveexample.reactiveexample;

import com.reactiveexample.exception.CustomException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
public class FluxAndMonoOnErrorTests extends ReactiveExampleApplicationTests {

    @Test
    public void fluxErrorHandling_withEmpty_return() {

        Flux<String> flux = Flux.just("test", "test1", "test2")
                .concatWith(Flux.error(new RuntimeException()))
                .concatWith(Flux.just("test3", "test4"))
                .onErrorResume(err -> {
                    System.out.println("Error");
                    return Flux.empty();
                })
                .log();

        StepVerifier.create(flux)
                .expectSubscription()
                .expectNext("test", "test1", "test2")
                .expectNext()
                .verifyComplete();
    }

    @Test
    public void fluxErrorHandling_OnErrorMap() {

        Flux<String> flux = Flux.just("test", "test1", "test2")
                .concatWith(Flux.error(new RuntimeException()))
                .concatWith(Flux.just("test3", "test4"))
                .onErrorMap(CustomException::new)
                .log();

        StepVerifier.create(flux)
                .expectSubscription()
                .expectNext("test", "test1", "test2")
                .expectError(CustomException.class)
                .verify();
    }

    @Test
    public void fluxErrorHandling_OnErrorMap_withRetry() {

        Flux<String> flux = Flux.just("test", "test1", "test2")
                .concatWith(Flux.error(new RuntimeException()))
                .concatWith(Flux.just("test3", "test4"))
                .onErrorMap(CustomException::new)
                .retry(2)
                .log();

        StepVerifier.create(flux)
                .expectSubscription()
                .expectNext("test", "test1", "test2")
                .expectNext("test", "test1", "test2")
                .expectNext("test", "test1", "test2")
                .expectError(CustomException.class)
                .verify();
    }
}
