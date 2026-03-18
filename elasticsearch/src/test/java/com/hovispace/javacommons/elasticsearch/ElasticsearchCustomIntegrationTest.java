package com.hovispace.javacommons.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hovispace.javacommons.elasticsearch.configuration.ElasticConfig;
import com.hovispace.javacommons.elasticsearch.model.Person;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ElasticConfig.class)
public class ElasticsearchCustomIntegrationTest {

    private static final String PEOPLE_INDEX = "people";

    @Resource
    private ElasticsearchClient _client;
    @Resource
    private ObjectMapper _objectMapper;

    @BeforeEach
    public void setUp() throws Exception {
        // create new index for test if necessary
        boolean indexExists = _client.indices().exists(e -> e.index(PEOPLE_INDEX)).value();
        if (!indexExists) {
            boolean acknowledged = _client.indices().create(c -> c.index(PEOPLE_INDEX)).acknowledged();
            if (!acknowledged) {
                throw new IllegalStateException("Could not create index: " + PEOPLE_INDEX);
            }
        }
        // clear all documents in index
        _client.deleteByQuery(d -> d
            .index(PEOPLE_INDEX)
            .query(q -> q.matchAll(m -> m)));
    }

    /**
     * https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/indexing.html
     */
    @Test
    public void test_that_json_document_is_indexed() throws Exception {
        Person person = _objectMapper.readValue("{\"age\":10,\"date_of_birth\":\"1471466076564\",\"full_name\":\"Grapes\"}", Person.class);
        IndexResponse indexResponse = _client.index(i -> i.index(PEOPLE_INDEX).document(person));

        assertThat(indexResponse.result().jsonValue()).isEqualTo("created");
        assertThat(indexResponse.version()).isEqualTo(1);
        assertThat(indexResponse.index()).isEqualTo(PEOPLE_INDEX);
    }

    @Test
    public void test_insert_person_document() throws Exception {
        Person person = new Person("Test", 10, new Date().toString());
        IndexResponse indexResponse = _client.index(i -> i.index(PEOPLE_INDEX).document(person));

        assertThat(indexResponse.result().jsonValue()).isEqualTo("created");
    }

    @Test
    public void test_querying_indexed_documents() throws Exception {
        Person person = _objectMapper.readValue("{\"age\":23,\"date_of_birth\":\"1471466076564\",\"full_name\":\"Grapes\"}", Person.class);
        _client.index(i -> i.index(PEOPLE_INDEX).document(person));

        await().untilAsserted(() -> {
            SearchResponse<Person> searchResponse = _client.search(s -> s.index(PEOPLE_INDEX), Person.class);
            List<String> names = searchResponse.hits().hits().stream()
                .map(h -> h.source() != null ? h.source().getFullName() : null)
                .toList();
            assertThat(names).contains("Grapes");
        });
    }

    @Test
    public void test_retrieve_and_delete_document() throws Exception {
        Person person = _objectMapper.readValue("{\"age\":23,\"date_of_birth\":\"1471466076564\",\"full_name\":\"Grapes\"}", Person.class);
        IndexResponse indexResponse = _client.index(i -> i.index(PEOPLE_INDEX).document(person));

        String id = indexResponse.id();

        GetResponse<Person> getResponse = _client.get(g -> g.index(PEOPLE_INDEX).id(id), Person.class);
        assertThat(getResponse.source()).isNotNull();
        assertThat(getResponse.source().getFullName()).isEqualTo("Grapes");
        assertThat(getResponse.source().getAge()).isEqualTo(23);

        DeleteResponse deleteResponse = _client.delete(d -> d.index(PEOPLE_INDEX).id(id));
        assertThat(deleteResponse.result().jsonValue()).isEqualTo("deleted");
    }

    /**
     * The QueryBuilders class provides a variety of static methods used as dynamic matchers to find specific entries in the cluster.
     */
    @Test
    public void test_search_with_QueryBuilder() throws Exception {
        Person person1 = _objectMapper.readValue("{\"age\":21,\"date_of_birth\":\"1471466076564\",\"full_name\":\"Grapes 1\"}", Person.class);
        Person person2 = _objectMapper.readValue("{\"age\":22,\"date_of_birth\":\"1471466076564\",\"full_name\":\"Banana 2\"}", Person.class);
        Person person3 = _objectMapper.readValue("{\"age\":23,\"date_of_birth\":\"1471466076564\",\"full_name\":\"Orange 3\"}", Person.class);
        _client.index(i -> i.index(PEOPLE_INDEX).document(person1));
        _client.index(i -> i.index(PEOPLE_INDEX).document(person2));
        _client.index(i -> i.index(PEOPLE_INDEX).document(person3));

        await().untilAsserted(() -> {
            // rangeQuery: matches documents where age is between 20 and 22
            SearchResponse<Person> response1 = _client.search(s -> s
                .index(PEOPLE_INDEX)
                .query(q -> q.range(r -> r.number(n -> n.field("age").gte(20.0).lte(22.0)))),
                Person.class);

            // matchQuery: matches documents where full_name matches "Banana"
            SearchResponse<Person> response2 = _client.search(s -> s
                .index(PEOPLE_INDEX)
                .query(QueryBuilders.match(m -> m.field("full_name").query("Banana"))),
                Person.class);

            assertThat(response1.hits().hits()).isNotEmpty();
            assertThat(response2.hits().hits()).isNotEmpty();
        });
    }
}
