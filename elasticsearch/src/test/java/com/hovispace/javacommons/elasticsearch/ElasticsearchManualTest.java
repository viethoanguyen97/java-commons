package com.hovispace.javacommons.elasticsearch;

import org.elasticsearch.action.DocWriteResponse.Result;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.action.DocWriteResponse.Result.CREATED;
import static org.elasticsearch.client.RequestOptions.DEFAULT;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticsearchManualTest {

    @Resource
    private RestHighLevelClient _client;

    @Test
    public void givenJsonString_whenJavaObject_thenIndexDocument() throws Exception{
        String jsonObject = "{\"age\":10,\"dateOfBirth\":1471466076564,"
            +"\"fullName\":\"John Doe\"}";
        IndexRequest request = new IndexRequest("people");
        request.source(jsonObject, XContentType.JSON);

        IndexResponse response = _client.index(request, RequestOptions.DEFAULT);
        String index = response.getIndex();
        long version = response.getVersion();

        assertEquals(Result.CREATED, response.getResult());
        assertEquals(1, version);
        assertEquals("people", index);
    }
    /**
     * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/master/java-rest-high-document-index.html
     * @throws Exception
     */
//    @Test
//    public void test_that_json_document_is_indexed() throws Exception {
//        String jsonObject = "{\"age\":10,\"dateOfBirth\":1471466076564,"
//            +"\"fullName\":\"John Doe\"}";
//        IndexRequest request = new IndexRequest("people");
//        request.source(jsonObject, XContentType.JSON);
//
//        //The index() function allows to store an arbitrary JSON document and make it searchable:
//        IndexResponse response = _client.index(request, DEFAULT);
//        assertThat(response.getResult()).isEqualTo(CREATED);
//        assertThat(response.getVersion()).isEqualTo(1);
//        assertThat(response.getIndex()).isEqualTo("people");
//    }
}
