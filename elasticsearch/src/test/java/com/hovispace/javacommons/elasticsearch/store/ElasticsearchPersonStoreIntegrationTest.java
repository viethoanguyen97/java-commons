package com.hovispace.javacommons.elasticsearch.store;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.hovispace.javacommons.elasticsearch.configuration.ElasticConfig;
import com.hovispace.javacommons.elasticsearch.model.Person;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static com.hovispace.javacommons.elasticsearch.store.ElasticsearchPersonStore.PERSON_INDEX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ElasticConfig.class)
public class ElasticsearchPersonStoreIntegrationTest {

    @Resource
    private ElasticsearchPersonStore _elasticsearchPersonStore;
    @Resource
    private ElasticsearchClient _client;

    @BeforeEach
    public void setUp() throws Exception {
        // create new index for test if necessary
        boolean indexExists = _client.indices().exists(e -> e.index(PERSON_INDEX)).value();
        if (!indexExists) {
            boolean acknowledged = _client.indices().create(c -> c.index(PERSON_INDEX)).acknowledged();
            if (!acknowledged) {
                throw new IllegalStateException("Could not create index: " + PERSON_INDEX);
            }
        }

        // clear all documents in index
        _client.deleteByQuery(d -> d
            .index(PERSON_INDEX)
            .query(q -> q.matchAll(m -> m)));
    }

    @Test
    public void test_insert() throws Exception {
        Person person1 = new Person("person1", 20, new Date().toString());
        Person person2 = new Person("person2", 21, new Date().toString());
        _elasticsearchPersonStore.insert(person1);
        _elasticsearchPersonStore.insert(person2);

        Query matchAll = QueryBuilders.matchAll().build()._toQuery();
        await().untilAsserted(() -> {
            assertThat(_elasticsearchPersonStore.find(matchAll))
                .extracting(Person::getFullName)
                .containsExactlyInAnyOrder("person1", "person2");
        });
    }

    @Test
    public void test_that_find_returns_data_if_there_is_at_least_one_document_matching_query() throws Exception {
        Person person1 = new Person("person1", 20, new Date().toString());
        Person person2 = new Person("person2", 21, new Date().toString());
        _elasticsearchPersonStore.insert(person1);
        _elasticsearchPersonStore.insert(person2);

        Query query = QueryBuilders.bool(b -> b
            .must(QueryBuilders.match(m -> m.field("full_name").query("person1"))));

        await().untilAsserted(() -> {
            assertThat(_elasticsearchPersonStore.find(query))
                .extracting(Person::getAge)
                .containsExactly(20);
        });
    }
}
