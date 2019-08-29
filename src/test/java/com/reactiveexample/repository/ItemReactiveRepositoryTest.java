package com.reactiveexample.repository;

import com.reactiveexample.model.ItemDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reactivestreams.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@DataMongoTest
@RunWith(SpringRunner.class)
public class ItemReactiveRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    private List<ItemDocument> items = null;

    @Before
    public void setUp() {
        items = Arrays.asList(ItemDocument.builder()
                        .name("test")
                        .id("1")
                        .description("testing")
                        .value(BigDecimal.valueOf(10))
                        .build(),
                ItemDocument.builder()
                        .id("2")
                        .name("test2")
                        .description("testing2")
                        .value(BigDecimal.valueOf(20))
                        .build(),
                ItemDocument.builder()
                        .name("test3")
                        .description("testing3")
                        .value(BigDecimal.valueOf(30))
                        .build(),
                ItemDocument.builder()
                        .name("test4")
                        .description("testing4")
                        .value(BigDecimal.valueOf(40))
                        .build());

//        itemRepository.deleteAll()
//                .thenMany(Flux.fromIterable(items))
//                .flatMap(itemRepository::save)
//                .doOnNext(item ->
//                        System.out.println("item inserted : " + item))
//                .blockLast();
        Flux.just(items)
                .flatMap(items -> {
                    items.forEach(item -> itemRepository.save(item));
                    return null;
                }).doOnNext(item ->
                System.out.println("item inserted : " + item));
    }

    private Long bucket = 500L;

    @Test
    public void getAllItems() {

        Subscriber<ItemDocument> subscriber = new ItemSubscriber<>();

        final Flux<ItemDocument> flux = itemRepository.findAll()
                .log()
                .limitRate(1);

        StepVerifier
                .create(flux)
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();

        flux.subscribe(subscriber);
    }


    @Test
    public void getItemById() {
        StepVerifier
                .create(itemRepository.findById("2"))
                .expectSubscription()
                .expectNextMatches(item ->
                        item.getName().equals("test2"))
                .verifyComplete();
    }

    @Test
    public void saveItem() {
        final ItemDocument item = ItemDocument.builder().id("4")
                .name("glass")
                .description("night glass")
                .value(BigDecimal.valueOf(250))
                .build();

        Mono<ItemDocument> monoItem = itemRepository.save(item);

        StepVerifier.create(monoItem.log())
                .expectSubscription()
                .expectNextMatches(glass -> glass.getName().equals("glass"))
                .verifyComplete();
    }

    @Test
    public void updateItem() {
        final BigDecimal price = BigDecimal.valueOf(25);

        Mono<ItemDocument> document = itemRepository.findById("1").log()
                .map(item -> {
                    item.setValue(price);
                    return item;
                })
                .flatMap(itemRepository::save);

        StepVerifier.create(document)
                .expectSubscription()
                .expectNextMatches(item ->
                        item.getName().equals("test")
                                && item.getValue().equals(BigDecimal.valueOf(25)))
                .verifyComplete();
    }

    @Test
    public void deleteItemById() {
        String idToDelete = "1";

        final Mono<Void> deletedFlux = itemRepository.findById(idToDelete)
                .map(ItemDocument::getId)
                .flatMap(itemRepository::deleteById);

        StepVerifier.create(deletedFlux.log())
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(itemRepository.findAll().log())
                .expectSubscription()
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void testWriteWithFlux() {
        ItemTest itemA = ItemTest.builder().name("Teste item")
                .description("testando o item")
                .date(LocalDate.now())
                .dateTime(LocalDateTime.now())
                .value(BigDecimal.valueOf(100D))
                .build();

        List<ItemTest> items = new ArrayList<>();

        for (int i = 0; i < 5_000_000; i++) {
            items.add(itemA);
        }

        Flux<ItemTest> fluxItems = Flux.fromStream(items.stream());

        try (final PrintWriter writer = new PrintWriter(new BufferedOutputStream(new FileOutputStream("file.csv")), true)) {
            System.out.println("inicio -" +LocalDateTime.now());

            writer.append(this.getHeader());

            fluxItems
                    .subscribeOn(Schedulers.immediate())
                    .map(this::map)
                    .doOnNext(writer::printf)
                    .doOnComplete(writer::flush)
                    .blockLast();

            StepVerifier.create(fluxItems.log())
                    .expectSubscription();

        } catch (Exception e) {
            System.out.println("error");
            System.out.println("e = " + e);
        } finally {
            System.out.println("fim -" + LocalDateTime.now());
        }
    }

    public String map(ItemTest item) {
        return item.toString();
    }

    public String getHeader() {
        return "name,description,date,dateTime,value\n";
    }
}
