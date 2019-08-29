package com.reactiveexample.repository;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemSubscriber<T> implements Subscriber<T> {

    //    private Long bucketSize = 500L;
    private Long bucketSize = 1L;
    private List<T> bucketData;

    public ItemSubscriber() {
        this.bucketData = new ArrayList<>();
    }

    @Override
    public void onSubscribe(Subscription s) {
        s.request(bucketSize);
    }

    @Override
    public void onNext(T item) {
        bucketData.add(item);
    }

    @Override
    public void onError(Throwable t) {
        System.out.println(" -> " + t.getMessage());
    }

    @Override
    public void onComplete() {
        System.out.println("complete");
    }
}
