package com.kalbob.reactive.play.rest;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;

public interface ItemReactiveCappedRepository extends ReactiveMongoRepository<ItemCapped,String> {


    @Tailable
    Flux<ItemCapped> findItemsBy();
}
