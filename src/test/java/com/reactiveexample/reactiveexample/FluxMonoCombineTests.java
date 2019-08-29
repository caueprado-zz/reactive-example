package com.reactiveexample.reactiveexample;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

@RunWith(SpringRunner.class)
public class FluxMonoCombineTests extends ReactiveExampleApplicationTests {

    @Test
    public void combineUsingMerge() {
        final Flux<String> flux1 = Flux.just("A", "B", "C");
        final Flux<String> flux2 = Flux.just("D", "E", "F");

        final Flux<String> mergedFlux = Flux.merge(flux1, flux2);

        StepVerifier.create(mergedFlux.log())
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    public void combineUsingMerge_With_delay() {
        VirtualTimeScheduler.getOrSet(  );
        final Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
        final Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));

        final Flux<String> mergedFlux = Flux.merge(flux1, flux2);

        StepVerifier.withVirtualTime(mergedFlux::log)
                .expectSubscription()
                .expectNextCount(6)
//                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    public void combineUsingConcat() {
        final Flux<String> flux1 = Flux.just("A", "B", "C");
        final Flux<String> flux2 = Flux.just("D", "E", "F");

        final Flux<String> mergedFlux = Flux.concat(flux1, flux2);

        StepVerifier.create(mergedFlux.log())
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    public void combineUsingConcat_withDelay() {
        final Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
        final Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));

        final Flux<String> mergedFlux = Flux.concat(flux1, flux2);

        StepVerifier.create(mergedFlux.log())
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    public void combineUsingZip() {
        final Flux<String> flux1 = Flux.just("A", "B", "C");
        final Flux<String> flux2 = Flux.just("D", "E", "F");

        final Flux<String> mergedFlux = Flux.zip(flux1, flux2, String::concat);

        StepVerifier.create(mergedFlux.log())
                .expectNext("AD")
                .expectNext("BE")
                .expectNext("CF")
                .verifyComplete();
    }
}
