package com.hovispace.javacommons.elasticsearch.store;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.hovispace.javacommons.elasticsearch.configuration.ElasticConfig;
import com.hovispace.javacommons.elasticsearch.model.Person;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import jakarta.annotation.Resource;
import java.util.Date;
import java.util.stream.Stream;

import static com.hovispace.javacommons.elasticsearch.store.ElasticsearchPersonStore.PERSON_INDEX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@ContextConfiguration(classes = ElasticConfig.class)
public class ElasticsearchPersonStoreIntegrationTest {

    @ClassRule
    public static SpringClassRule c_springClassRule = new SpringClassRule();
    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();
    @Resource
    private ElasticsearchPersonStore _elasticsearchPersonStore;
    @Resource
    private ElasticsearchClient _client;

    @Before
    public void setUp() throws Exception {
        //create new index for test if necessary
        try {
            _client.indices().get(g -> g.index(PERSON_INDEX));
        } catch (Exception e) {
            _client.indices().create(c -> c.index(PERSON_INDEX));
        }

        //clear all documents in index
        _client.deleteByQuery(d -> d
            .index(PERSON_INDEX)
            .query(q -> q.matchAll(m -> m))
        );
    }

    @Test
    public void test_insert() throws Exception {
        Person person1 = new Person("person1", 20, new Date().toString());
        Person person2 = new Person("person2", 21, new Date().toString());
        _elasticsearchPersonStore.insert(person1);
        _elasticsearchPersonStore.insert(person2);

        await().untilAsserted(() -> {
            Query matchAllQuery = Query.of(q -> q.matchAll(m -> m));
            Stream<Person> people = _elasticsearchPersonStore.find(matchAllQuery);
            assertThat(people).extracting(Person::getFullName).containsExactlyInAnyOrder("person1", "person2");
        });
    }

    @Test
    public void test_that_find_returns_data_if_there_is_at_least_one_document_matching_query() throws Exception {
        Person person1 = new Person("person1", 20, new Date().toString());
        Person person2 = new Person("person2", 21, new Date().toString());
        _elasticsearchPersonStore.insert(person1);
        _elasticsearchPersonStore.insert(person2);

        Query query = Query.of(q -> q
            .bool(b -> b
                .must(m -> m.match(mt -> mt.field("full_name").query("person1")))
            )
        );

        await().untilAsserted(() -> {
            Stream<Person> people = _elasticsearchPersonStore.find(query);
            assertThat(people).extracting(Person::getAge).containsExactly(20);
        });
    }
}
