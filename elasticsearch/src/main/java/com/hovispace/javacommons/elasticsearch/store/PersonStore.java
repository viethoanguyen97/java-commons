package com.hovispace.javacommons.elasticsearch.store;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.hovispace.javacommons.elasticsearch.model.Person;

import java.io.IOException;
import java.util.stream.Stream;

public interface PersonStore {

    Stream<Person> find(Query query) throws IOException;

    void insert(Person person) throws IOException;
}
