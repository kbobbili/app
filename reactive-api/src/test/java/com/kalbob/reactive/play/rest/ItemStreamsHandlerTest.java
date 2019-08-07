package com.kalbob.reactive.play.rest;

import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
@DirtiesContext
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class ItemStreamsHandlerTest {

    @Autowired
    ItemReactiveCappedRepository itemReactiveCappedRepository;

    @Autowired
    MongoOperations mongoOperations;

    @Autowired
    WebTestClient webTestClient;

    @BeforeEach
    public void setUp() {

        mongoOperations.dropCollection(ItemCapped.class);
        mongoOperations.createCollection(ItemCapped.class, CollectionOptions.empty().maxDocuments(20).size(50000).capped());

        Flux<ItemCapped> itemCappedFlux = Flux.interval(Duration.ofMillis(100))
                .map(i -> new ItemCapped(null, "Random Item " + i, (100.00 + i)))
                .take(5);

        itemReactiveCappedRepository
                .insert(itemCappedFlux)
                .doOnNext((itemCapped -> {
                    System.out.println("Inserted Item in setUp " + itemCapped);
                }))
                .blockLast();


    }

    @Test
    public void testStreamAllItems(){

        Flux<ItemCapped> itemCappedFlux = webTestClient.get().uri(ItemConstants.ITEM_STREAM_FUNCTIONAL_END_POINT_V1)
                .exchange()
                .expectStatus().isOk()
                .returnResult((ItemCapped.class))
                .getResponseBody()
                .take(5);

        StepVerifier.create(itemCappedFlux)
                .expectNextCount(5)
                .thenCancel()
                .verify();

    }
}
