package com.reactiveexample.reactiveexample;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.reactivestreams.Subscription;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
public class MonoAndFluxBackPressureTests {

    @Test
    public void backPressureTest() {
        Flux<Integer> finiteFlux = Flux.range(1, 10)
                .log();

        StepVerifier
                .create(finiteFlux)
                .expectSubscription()
                .thenRequest(1L)
                .expectNext(1)
                .thenRequest(1L)
                .expectNext(2)
                .thenCancel()
                .verify();
    }

    @Test
    public void backPressure() {
        Flux<Integer> finiteFlux = Flux.range(1, 10)
                .log();

        finiteFlux.subscribe(element -> System.out.print("element : " + element),
                err -> System.err.print("err : " + err),
                () -> System.out.print("Done"),
                subscription -> subscription.request(2));
    }

    @Test
    public void backPressure_onCancel() {
        Flux<Integer> finiteFlux = Flux.range(1, 10)
                .log();

        finiteFlux.subscribe(element -> System.out.print("element : " + element),
                err -> System.err.print("err : " + err),
                () -> System.out.print("Done"),
                Subscription::cancel);
    }

    @Test
    public void backPressure_custom() {
        Flux<Integer> finiteFlux = Flux.range(1, 10)
                .log();

        finiteFlux.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnNext(Integer value) {
                super.hookOnNext(value);
                System.out.println(value);
                if (value == 9) {
                    cancel();
                }
            }
        });
    }
}
