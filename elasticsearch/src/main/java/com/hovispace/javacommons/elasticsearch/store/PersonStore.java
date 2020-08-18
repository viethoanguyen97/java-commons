package com.hovispace.javacommons.elasticsearch.store;

import com.hovispace.javacommons.elasticsearch.model.Person;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.stream.Stream;

public interface PersonStore {

    Stream<Person> find(SearchSourceBuilder searchSourceBuilder) throws IOException;

    void insert(Person person) throws IOException;
}
