package com.hovispace.javacommons.elasticsearch.store;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.google.common.annotations.VisibleForTesting;
import com.hovispace.javacommons.elasticsearch.model.Person;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;

public class ElasticsearchPersonStore implements PersonStore {

    /**
     * For this tutorial I'll skip setup the alias, in real production, instead of directly querying a specific index,
     * we should setup aliases for reading and writing data in order to scale up our indexes when your data growing up.
     */
    @VisibleForTesting
    static final String PERSON_INDEX = "person";

    private final ElasticsearchClient _elasticClient;

    @Autowired
    public ElasticsearchPersonStore(ElasticsearchClient elasticClient) {
        _elasticClient = elasticClient;
    }

    @Override
    public Stream<Person> find(Query query) throws IOException {
        SearchResponse<Person> searchResponse = _elasticClient.search(s -> s
            .index(PERSON_INDEX)
            .query(query),
            Person.class
        );
        return searchResponse.hits().hits().stream()
            .map(Hit::source)
            .filter(Objects::nonNull);
    }

    @Override
    public void insert(Person person) throws IOException {
        IndexResponse indexResponse = _elasticClient.index(i -> i
            .index(PERSON_INDEX)
            .document(person)
        );
        if (indexResponse.result() != Result.Created && indexResponse.result() != Result.Updated) {
            throw new RuntimeException("Failed to insert document for " + person.toString() + ". Result was " + indexResponse.result());
        }
    }

}
