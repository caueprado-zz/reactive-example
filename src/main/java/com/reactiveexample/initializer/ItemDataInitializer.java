package com.reactiveexample.initializer;

import com.reactiveexample.model.ItemDocument;
import com.reactiveexample.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class ItemDataInitializer implements CommandLineRunner {

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public void run(String... args) throws Exception {
        initialDataSetup();
    }

    private List<ItemDocument> data() {
        return Arrays.asList(ItemDocument.builder()
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
                        .build());
    }

    private void initialDataSetup() {
        itemRepository.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(itemRepository::save)
                .thenMany(itemRepository.findAll())
        .subscribe(item -> {
            System.out.println("item inserted from initializer");
        });
    }
}
