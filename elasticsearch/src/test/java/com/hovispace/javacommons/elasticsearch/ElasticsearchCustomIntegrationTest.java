package com.hovispace.javacommons.elasticsearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hovispace.javacommons.elasticsearch.configuration.ElasticConfig;
import com.hovispace.javacommons.elasticsearch.model.Person;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.elasticsearch.action.DocWriteResponse.Result.*;
import static org.elasticsearch.action.search.SearchType.DFS_QUERY_THEN_FETCH;
import static org.elasticsearch.action.support.IndicesOptions.lenientExpandOpen;
import static org.elasticsearch.client.RequestOptions.DEFAULT;
import static org.elasticsearch.common.xcontent.XContentType.JSON;

@ContextConfiguration(classes = ElasticConfig.class)
public class ElasticsearchCustomIntegrationTest {

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();
    @Resource
    private RestHighLevelClient _client;
    @Resource
    private ObjectMapper _objectMapper;

    @Before
    public void setUp() throws Exception {
        //create new index for test if necessary
        //NOTE: this is a workaround for test only
        GetIndexRequest getReadIndexRequest = new GetIndexRequest("people").indicesOptions(lenientExpandOpen());
        GetIndexResponse getReadIndexResponse = _client.indices().get(getReadIndexRequest, DEFAULT);
        if (isEmpty(getReadIndexResponse.getIndices())) {
            CreateIndexResponse createIndexResponse = _client.indices().create(new CreateIndexRequest("people"), DEFAULT);
            if (!createIndexResponse.isAcknowledged()) {
                throw new IllegalStateException("Could not create index: " + "people");
            }
        }
        //clear all documents in index
        _client.deleteByQuery(new DeleteByQueryRequest("people").setQuery(QueryBuilders.matchAllQuery()), DEFAULT);
    }

    /**
     * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/master/java-rest-high-document-index.html
     */
    @Test
    public void test_that_json_document_is_indexed() throws Exception {
        String jsonObject = "{\"age\":10,\"date_of_birth\":1471466076564,\"full_name\":\"Grapes\"}";
        IndexRequest indexRequest = new IndexRequest("people");
        indexRequest.source(jsonObject, JSON);

        //The index() function allows to store an arbitrary JSON document and make it searchable:
        IndexResponse indexResponse = _client.index(indexRequest, DEFAULT);
        assertThat(indexResponse.getResult()).isEqualTo(CREATED);
        assertThat(indexResponse.getVersion()).isEqualTo(1);
        assertThat(indexResponse.getIndex()).isEqualTo("people");
    }

    /**
     * Note that it is possible to use any JSON Java library to create and process your documents.
     * If you are not familiar with any of these, you can use Elasticsearch helpers to generate your own JSON documents:
     */
    @Test
    public void test_insert_json_data_by_xContentBuilder() throws Exception {
        XContentBuilder builder = XContentFactory.jsonBuilder()
            .startObject()
            .field("full_name", "Test")
            .field("date_of_birth", new Date())
            .field("age", "10")
            .endObject();

        IndexRequest indexRequest = new IndexRequest("people");
        indexRequest.source(builder);
        IndexResponse indexResponse = _client.index(indexRequest, DEFAULT);

        assertThat(indexResponse.getResult()).isEqualTo(CREATED);
    }

    @Test
    public void test_querying_indexed_documents() throws Exception {
        String jsonObject = "{\"age\":23,\"date_of_birth\":1471466076564,\"full_name\":\"Grapes\"}";
        IndexRequest indexRequest = new IndexRequest("people");
        indexRequest.source(jsonObject, JSON);
        IndexResponse indexResponse = _client.index(indexRequest, DEFAULT);

        await().untilAsserted(() -> {
            SearchRequest searchRequest = new SearchRequest().indices("people");
            SearchResponse searchResponse = _client.search(searchRequest, DEFAULT);
            // The results returned by the search() method are called Hits, each Hit refers to a JSON document matching a search request.
            SearchHit[] searchHits = searchResponse.getHits().getHits();

            List<Person> results = Arrays.stream(searchHits)
                .map(hit -> {
                    try {
                        return _objectMapper.readValue(hit.getSourceAsString(), Person.class);
                    } catch (JsonProcessingException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
            assertThat(results).extracting(Person::getFullName).contains("Grapes");
        });
    }

    @Test
    public void test_retrieve_and_delete_document() throws Exception {
        String jsonObject = "{\"age\":23,\"date_of_birth\":1471466076564,\"full_name\":\"Grapes\"}";
        IndexRequest indexRequest = new IndexRequest("people");
        indexRequest.source(jsonObject, JSON);
        IndexResponse indexResponse = _client.index(indexRequest, DEFAULT);

        String id = indexResponse.getId();

        GetRequest getRequest = new GetRequest("people").id(id);
        GetResponse getResponse = _client.get(getRequest, DEFAULT);
        assertThat(getResponse.getSourceAsString()).contains("\"age\":23", "\"date_of_birth\":1471466076564","\"full_name\":\"Grapes\"");

        DeleteRequest deleteRequest = new DeleteRequest("people").id(id);
        DeleteResponse deleteResponse = _client.delete(deleteRequest, DEFAULT);
        assertThat(deleteResponse.getResult()).isEqualTo(DELETED);
    }

    /**
     * The QueryBuilders class provides a variety of static methods used as dynamic matchers to find specific entries in the cluster.
     * While using the search() method to look for specific JSON documents in the cluster, we can use query builders to customize the search results.
     */
    @Test
    public void test_search_with_QueryBuilder() throws Exception {
        String jsonObject1 = "{\"age\":21,\"date_of_birth\":1471466076564,\"full_name\":\"Grapes 1\"}";
        String jsonObject2 = "{\"age\":22,\"date_of_birth\":1471466076564,\"full_name\":\"Banana 2\"}";
        String jsonObject3 = "{\"age\":23,\"date_of_birth\":1471466076564,\"full_name\":\"Orange 3\"}";
        _client.index(new IndexRequest("people").source(jsonObject1, JSON), DEFAULT);
        _client.index(new IndexRequest("people").source(jsonObject2, JSON), DEFAULT);
        _client.index(new IndexRequest("people").source(jsonObject3, JSON), DEFAULT);

        // The rangeQuery() matches documents where a field's value is within a certain range:
        SearchSourceBuilder builder1 = new SearchSourceBuilder().postFilter(QueryBuilders.rangeQuery("age").from(20).to(22));
        // we can use the Lucene's Query Parser syntax to build simple, yet powerful queries.
        // Here're some basic operators that can be used alongside the AND/OR/NOT operators to build search queries:
        //The required operator (+): requires that a specific piece of text exists somewhere in fields of a document.
        //The prohibit operator (–): excludes all documents that contain a keyword declared after the (–) symbol.
        SearchSourceBuilder builder2 = new SearchSourceBuilder().postFilter(QueryBuilders.simpleQueryStringQuery("+Grapes -Banana OR Orange"));
        // The matchQuery() method matches all document with these exact field's value:
        SearchSourceBuilder builder3 = new SearchSourceBuilder().postFilter(QueryBuilders.matchQuery("full_name", "Banana"));

        SearchRequest searchRequest1 = new SearchRequest().indices("people").searchType(DFS_QUERY_THEN_FETCH).source(builder1);
        SearchRequest searchRequest2 = new SearchRequest().indices("people").searchType(DFS_QUERY_THEN_FETCH).source(builder2);
        SearchRequest searchRequest3 = new SearchRequest().indices("people").searchType(DFS_QUERY_THEN_FETCH).source(builder3);

        await().untilAsserted(() -> {
            SearchResponse response1 = _client.search(searchRequest1, DEFAULT);
            SearchResponse response2 = _client.search(searchRequest2, DEFAULT);
            SearchResponse response3 = _client.search(searchRequest3, DEFAULT);

            assertThat(response1.getHits()).isNotEmpty();
            assertThat(response2.getHits()).isNotEmpty();
            assertThat(response3.getHits()).isNotEmpty();
        });
    }
}
