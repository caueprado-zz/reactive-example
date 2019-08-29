package com.reactiveexample.repository;

import com.reactiveexample.model.ItemDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ItemRepository extends ReactiveMongoRepository<ItemDocument,String> {
}
