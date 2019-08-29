package com.reactiveexample.controller;

import com.reactiveexample.model.ItemDocument;
import com.reactiveexample.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("v1")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    public Flux<ItemDocument> getAll() {
        return itemRepository.findAll();
    }
}
