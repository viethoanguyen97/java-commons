package com.hovispace.javacommons.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.hovispace.javacommons.elasticsearch.configuration.ElasticConfig;
import com.hovispace.javacommons.elasticsearch.model.Person;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import jakarta.annotation.Resource;
import java.io.StringReader;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@ContextConfiguration(classes = ElasticConfig.class)
public class ElasticsearchCustomIntegrationTest {

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();
    @Resource
    private ElasticsearchClient _client;

    @Before
    public void setUp() throws Exception {
        //create new index for test if necessary
        try {
            _client.indices().get(g -> g.index("people"));
        } catch (Exception e) {
            _client.indices().create(c -> c.index("people"));
        }
        //clear all documents in index
        _client.deleteByQuery(d -> d
            .index("people")
            .query(q -> q.matchAll(m -> m))
        );
    }

    /**
     * The index() function allows to store an arbitrary JSON document and make it searchable.
     */
    @Test
    public void test_that_json_document_is_indexed() throws Exception {
        String jsonObject = "{\"age\":10,\"date_of_birth\":\"1471466076564\",\"full_name\":\"Grapes\"}";
        IndexResponse indexResponse = _client.index(i -> i
            .index("people")
            .withJson(new StringReader(jsonObject))
        );

        assertThat(indexResponse.result().jsonValue()).isIn("created", "Created");
        assertThat(indexResponse.version()).isEqualTo(1);
        assertThat(indexResponse.index()).isEqualTo("people");
    }

    @Test
    public void test_querying_indexed_documents() throws Exception {
        String jsonObject = "{\"age\":23,\"date_of_birth\":\"1471466076564\",\"full_name\":\"Grapes\"}";
        _client.index(i -> i
            .index("people")
            .withJson(new StringReader(jsonObject))
        );

        await().untilAsserted(() -> {
            SearchResponse<Person> searchResponse = _client.search(s -> s
                .index("people"),
                Person.class
            );

            List<Person> results = searchResponse.hits().hits().stream()
                .map(Hit::source)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
            assertThat(results).extracting(Person::getFullName).contains("Grapes");
        });
    }

    @Test
    public void test_retrieve_and_delete_document() throws Exception {
        String jsonObject = "{\"age\":23,\"date_of_birth\":\"1471466076564\",\"full_name\":\"Grapes\"}";
        IndexResponse indexResponse = _client.index(i -> i
            .index("people")
            .withJson(new StringReader(jsonObject))
        );

        String id = indexResponse.id();

        GetResponse<Person> getResponse = _client.get(g -> g
            .index("people")
            .id(id),
            Person.class
        );
        assertThat(getResponse.source()).isNotNull();
        assertThat(getResponse.source().getFullName()).isEqualTo("Grapes");
        assertThat(getResponse.source().getAge()).isEqualTo(23);

        DeleteResponse deleteResponse = _client.delete(d -> d
            .index("people")
            .id(id)
        );
        assertThat(deleteResponse.result().jsonValue()).isIn("deleted", "Deleted");
    }

    /**
     * The query builders provide a variety of methods used as dynamic matchers to find specific entries in the cluster.
     */
    @Test
    public void test_search_with_QueryBuilder() throws Exception {
        String jsonObject1 = "{\"age\":21,\"date_of_birth\":\"1471466076564\",\"full_name\":\"Grapes 1\"}";
        String jsonObject2 = "{\"age\":22,\"date_of_birth\":\"1471466076564\",\"full_name\":\"Banana 2\"}";
        String jsonObject3 = "{\"age\":23,\"date_of_birth\":\"1471466076564\",\"full_name\":\"Orange 3\"}";
        _client.index(i -> i.index("people").withJson(new StringReader(jsonObject1)));
        _client.index(i -> i.index("people").withJson(new StringReader(jsonObject2)));
        _client.index(i -> i.index("people").withJson(new StringReader(jsonObject3)));

        await().untilAsserted(() -> {
            // Range query: matches documents where a field's value is within a certain range
            SearchResponse<Person> response1 = _client.search(s -> s
                .index("people")
                .query(q -> q.range(r -> r.number(n -> n.field("age").gte(20.0).lte(22.0)))),
                Person.class
            );

            // Simple query string: supports AND/OR/NOT operators
            SearchResponse<Person> response2 = _client.search(s -> s
                .index("people")
                .query(q -> q.simpleQueryString(sq -> sq.query("+Grapes -Banana OR Orange"))),
                Person.class
            );

            // Match query: matches all documents with the exact field's value
            SearchResponse<Person> response3 = _client.search(s -> s
                .index("people")
                .query(q -> q.match(m -> m.field("full_name").query("Banana"))),
                Person.class
            );

            assertThat(response1.hits().hits()).isNotEmpty();
            assertThat(response2.hits().hits()).isNotEmpty();
            assertThat(response3.hits().hits()).isNotEmpty();
        });
    }
}
