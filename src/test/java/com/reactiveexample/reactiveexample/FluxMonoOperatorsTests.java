package com.reactiveexample.reactiveexample;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@RunWith(SpringRunner.class)
public class FluxMonoOperatorsTests extends ReactiveExampleApplicationTests {

    @Test
    public void monoFromString() {

        Mono<String> mono = Mono.just("test");

        StepVerifier.create(mono)
                .expectNext("test")
                .verifyComplete();
    }

    @Test
    public void monoFromSupplier() {
        Supplier<String> supplier = (() -> "Caue");

        Mono<String> monoTest = Mono.fromSupplier(supplier);

        StepVerifier.create(monoTest.log())
                .expectNext("Caue")
                .verifyComplete();
    }

    @Test
    public void fluxFromRange() {
        System.out.println(LocalDateTime.now());
        Flux<Integer> range = Flux
                .range(1, 50000)
                .filter(number -> number > 2);

        StepVerifier.create(range.log())
                .expectNext(1);
        System.out.println(LocalDateTime.now());
        System.out.println(LocalDateTime.now());

        List<Integer> ints = new ArrayList<>();
        for (int i = 0; i < 50000; i++) {
            ints.add(i);
            System.out.println(i);
        }


        List<Integer> ints1 = new ArrayList<>(50000);
        for (int i = 0; i < 50000; i++) {
            ints.add(i);
        }
        System.out.println(LocalDateTime.now());

        ints1.forEach(i -> System.out.println(i));

        Flux<Integer> range1 = Flux
                .range(1, 50000)
                .filter(number -> number > 2);

        StepVerifier.create(range1.log())
                .expectNext(1);
        System.out.println(LocalDateTime.now());
    }

    @Test
    public void fluxFromIterable() {

        Flux<String> brands = Flux.fromIterable(list)
                .filter(brand -> brand.startsWith("n"))
                .map(String::toUpperCase);

        List<String> list = new ArrayList<>();
        StepVerifier.create(brands.log())
                .expectNext("NIKE", "NOBALANCE")
                .expectNextSequence(list)
                .verifyComplete();
    }

    @Test
    public void transformUsingFlatMap() {

        final Flux<String> profiles = Flux.fromIterable(names)
                .flatMap(s -> Flux.fromIterable(mapModels(s)))
                .log();

//        final List<String> firstExpect = Arrays.asList("Caue,28,Dev");
//        final List<String> secExpect = Arrays.asList("Snow,20,Warrior");

        StepVerifier.create(profiles.log())
                .expectNextCount(18)
                .verifyComplete();
    }

    @Test
    public void transformUsingFlatMapParallel() {

        final Flux<String> profiles = Flux.fromIterable(names)
                .window(2)
                .flatMap(s -> s.map(this::mapModels).subscribeOn(Schedulers.parallel()))
                .flatMap(Flux::fromIterable)
                .log();

        StepVerifier.create(profiles.log())
                .expectNextCount(18)
                .verifyComplete();
    }

    @Test
    public void transformUsingFlatMapParallel_with_same_order() {

        final Flux<String> profiles = Flux.fromIterable(names)
                .window(2)
                .flatMapSequential(s -> s.map(this::mapModels).subscribeOn(Schedulers.parallel()))
                .flatMap(Flux::fromIterable)
                .log();

        StepVerifier.create(profiles.log())
                .expectNextCount(18)
                .verifyComplete();
    }
}
