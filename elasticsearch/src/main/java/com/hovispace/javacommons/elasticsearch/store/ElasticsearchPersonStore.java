package com.hovispace.javacommons.elasticsearch.store;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import com.hovispace.javacommons.elasticsearch.model.Person;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Spliterator.ORDERED;
import static java.util.Spliterators.spliterator;
import static org.elasticsearch.client.RequestOptions.DEFAULT;
import static org.elasticsearch.common.xcontent.XContentType.JSON;
import static org.elasticsearch.rest.RestStatus.*;

public class ElasticsearchPersonStore implements PersonStore {

    /**
     * For this tutorial I'll skip setup the alias, in real production, instead of directly querying a specific index,
     * we should setup aliases for reading and writing data in order to scale up our indexes when your data growing up.
     */
    @VisibleForTesting
    static final String PERSON_INDEX = "person";

    /**
     * because RestHighLevelClient methods are final we can not using mockito to mock it,
     * so in order to write unit tests in production, consider writing a facade wrapping the RestHighLevelClient and mock that object.
     */
    private final RestHighLevelClient _elasticClient;
    private final ObjectMapper _objectMapper;

    @Autowired
    public ElasticsearchPersonStore(RestHighLevelClient elasticClient, ObjectMapper objectMapper) {
        _elasticClient = elasticClient;
        _objectMapper = objectMapper;
    }

    @Override
    public Stream<Person> find(SearchSourceBuilder searchSourceBuilder) throws IOException {
        //if number of search hit exceed limit, use ScrollAPI instead, for this tutorial, I'll skip it
        SearchRequest searchRequest = new SearchRequest(PERSON_INDEX);
        searchRequest.source(searchSourceBuilder);
        SearchHits searchHits = _elasticClient.search(searchRequest, DEFAULT).getHits();
        return StreamSupport.stream(spliterator(searchHits.iterator(), searchHits.getTotalHits().value, ORDERED), false)
            .map(this::toPerson)
            .filter(Objects::nonNull);
    }

    @Override
    public void insert(Person person) throws IOException {
        IndexRequest indexRequest = new IndexRequest(PERSON_INDEX);
        String jsonString = _objectMapper.writeValueAsString(person);
        IndexResponse indexResponse = _elasticClient.index(indexRequest.source(jsonString, JSON), DEFAULT);
        if (indexResponse.status() != OK && indexResponse.status() != CREATED) {
            throw new RuntimeException("Failed to insert json for " + person.toString() + ". Status was " + indexResponse.status() + " and response was " + indexResponse);
        }
    }

    @Nullable
    private Person toPerson(SearchHit searchHit) {
        try {
            String jsonString = searchHit.getSourceAsString();
            Person person = _objectMapper.readValue(jsonString, Person.class);
            return person;
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

}
