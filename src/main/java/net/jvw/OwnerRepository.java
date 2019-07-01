package net.jvw;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OwnerRepository {

  private final ObjectMapper mapper = new ObjectMapper();

  private RestHighLevelClient client;

  public void init(String elasticsearchAddress) {
    String[] split = elasticsearchAddress.split(":");

    client = new RestHighLevelClient(
        RestClient.builder(
            new HttpHost(split[0], Integer.parseInt(split[1]), "http")));
  }

  public String findAll() {
    return jsonResult(QueryBuilders.matchAllQuery());
  }

  public String findByLastName(String lastName) {
    return jsonResult(QueryBuilders.matchQuery("lastName", lastName));
  }

  private String jsonResult(QueryBuilder query) {
    try {
      SearchRequest request = new SearchRequest("owner");

      SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
      searchSourceBuilder.query(query);
      searchSourceBuilder.size(1000);

      request.source(searchSourceBuilder);

      SearchResponse response = client.search(request, RequestOptions.DEFAULT);

      SearchHit[] hits = response.getHits().getHits();

      List<Owner> result = new ArrayList<>();
      for (SearchHit hit : hits) {
        result.add(toResult(hit));
      }

      return mapper.writeValueAsString(result);
    } catch (IOException e) {
      throw new RuntimeException(e); // don't use this 'shortcut' in production!
    }
  }


  private Owner toResult(final SearchHit searchHit) {
    try {
      return mapper.readValue(searchHit.getSourceAsString(), Owner.class);
    } catch (Exception e) {
      throw new ElasticsearchException(e);
    }
  }
}
