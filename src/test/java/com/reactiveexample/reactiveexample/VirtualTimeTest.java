package com.reactiveexample.reactiveexample;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

@RunWith(SpringRunner.class)
public class VirtualTimeTest {

    @Test
    public void test_withouth_virtualTime() {
        Flux<Long> flux = Flux.interval(Duration.ofSeconds(1))
                .take(3);

        StepVerifier.create(flux.log())
                .expectSubscription()
                .expectNext(0L, 1L, 2L)
                .verifyComplete();
    }

    @Test
    public void test_with_virtualTime() {
        VirtualTimeScheduler.getOrSet();
        Flux<Long> flux = Flux.interval(Duration.ofSeconds(1))
                .take(3);

        StepVerifier.withVirtualTime(flux::log)
                .expectSubscription()
                .expectNext(0L, 1L, 2L)
                .verifyComplete();
    }
}
