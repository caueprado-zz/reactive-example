package com.reactiveexample.reactiveexample;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RunWith(SpringRunner.class)
public class ColdAndHotPublisherTest {

    @Test
    public void coldPublisherTest() throws Exception {
        Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E", "F")
                .delayElements(Duration.ofSeconds(1));

        stringFlux.subscribe(s -> System.out.println("sub 1 " + s));

        Thread.sleep(2000);

        stringFlux.subscribe(s -> System.out.println("sub 2 " + s));

        Thread.sleep(4000);
    }

    @Test
    public void hotPublisherTest() throws Exception {
        Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E", "F")
                .delayElements(Duration.ofSeconds(1));

        ConnectableFlux<String> flux = stringFlux.publish();
        flux.connect();
        flux.subscribe(s -> System.out.println("sub 1 " + s));
        Thread.sleep(2000);
        flux.subscribe(s -> System.out.println("sub 2 " + s));
        Thread.sleep(4000);
    }
}
