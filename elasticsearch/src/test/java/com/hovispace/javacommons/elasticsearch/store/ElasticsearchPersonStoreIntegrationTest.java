package com.hovispace.javacommons.elasticsearch.store;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hovispace.javacommons.elasticsearch.configuration.ElasticConfig;
import com.hovispace.javacommons.elasticsearch.model.Person;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.hovispace.javacommons.elasticsearch.store.ElasticsearchPersonStore.PERSON_INDEX;
import static java.util.Spliterator.ORDERED;
import static java.util.Spliterators.spliterator;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.elasticsearch.action.support.IndicesOptions.lenientExpandOpen;
import static org.elasticsearch.client.RequestOptions.DEFAULT;

@ContextConfiguration(classes = ElasticConfig.class)
public class ElasticsearchPersonStoreIntegrationTest {

    @ClassRule
    public static SpringClassRule c_springClassRule = new SpringClassRule();
    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();
    @Resource
    private ElasticsearchPersonStore _elasticsearchPersonStore;
    @Resource
    private RestHighLevelClient _client;
    @Resource
    private ObjectMapper _objectMapper;

    @Before
    public void setUp() throws Exception {
        //create new index for test if necessary
        GetIndexRequest getReadIndexRequest = new GetIndexRequest(PERSON_INDEX).indicesOptions(lenientExpandOpen());
        GetIndexResponse getReadIndexResponse = _client.indices().get(getReadIndexRequest, DEFAULT);
        if (isEmpty(getReadIndexResponse.getIndices())) {
            CreateIndexResponse createIndexResponse = _client.indices().create(new CreateIndexRequest(PERSON_INDEX), DEFAULT);
            if (!createIndexResponse.isAcknowledged()) {
                throw new IllegalStateException("Could not create index: " + PERSON_INDEX);
            }
        }

        //clear all documents in index
        _client.deleteByQuery(new DeleteByQueryRequest(PERSON_INDEX).setQuery(QueryBuilders.matchAllQuery()), DEFAULT);
    }

    @Test
    public void test_insert() throws Exception {
        Person person1 = new Person("person1", 20, new Date().toString());
        Person person2 = new Person("person2", 21, new Date().toString());
        _elasticsearchPersonStore.insert(person1);
        _elasticsearchPersonStore.insert(person2);

        SearchRequest searchRequest = new SearchRequest().indices(PERSON_INDEX);
        SearchResponse searchResponse = _client.search(searchRequest, DEFAULT);
        SearchHits searchHits = searchResponse.getHits();

        await().untilAsserted(() -> {
            Stream<Person> people = StreamSupport.stream(spliterator(searchHits.iterator(), searchHits.getTotalHits().value, ORDERED), false)
                .map(this::toPerson)
                .filter(Objects::nonNull);
            assertThat(people).extracting(Person::getFullName).containsExactlyInAnyOrder("person1", "person2");
        });
    }

    @Test
    public void test_that_find_returns_data_if_there_is_at_least_one_document_matching_query() throws Exception {
        Person person1 = new Person("person1", 20, new Date().toString());
        Person person2 = new Person("person2", 21, new Date().toString());
        _elasticsearchPersonStore.insert(person1);
        _elasticsearchPersonStore.insert(person2);

        QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("full_name", "person1"));
        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource().query(queryBuilder);

        await().untilAsserted(() -> {
            Stream<Person> people = _elasticsearchPersonStore.find(searchSourceBuilder);
            assertThat(people).extracting(Person::getAge).containsExactly(20);
        });
    }

    @Nullable
    private Person toPerson(SearchHit searchHit) {
        try {
            String jsonString = searchHit.getSourceAsString();
            Person person = _objectMapper.readValue(jsonString, Person.class);
            return person;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
