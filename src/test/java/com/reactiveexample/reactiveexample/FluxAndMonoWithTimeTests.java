package com.reactiveexample.reactiveexample;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

@RunWith(SpringRunner.class)
public class FluxAndMonoWithTimeTests {

    @Test
    public void infiniteSequence() throws Exception{
        final Flux<Long> flux1 = Flux.interval(Duration.ofMillis(100)).log();

        flux1.subscribe(e -> System.out.print("Value : " + e));

        Thread.sleep(3000);
    }

    @Test
    public void infiniteSequence_with_takeLimitedElements() throws Exception{
        final Flux<Long> flux1 = Flux.interval(Duration.ofMillis(100))
                .delayElements(Duration.ofSeconds(1))
                .take(3)
                .log();

        StepVerifier
                .create(flux1)
                .expectSubscription()
                .expectNext(0L, 1L, 2L)
                .verifyComplete();
    }

}
