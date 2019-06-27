package net.jvw;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OwnerRepository {

  private final ObjectMapper mapper = new ObjectMapper();

  private RestHighLevelClient client;

  public void init(String address) throws IOException {

    String[] split = address.split(":");

    client = new RestHighLevelClient(
        RestClient.builder(
            new HttpHost(split[0], Integer.parseInt(split[1]), "http")));
  }

  public List<String> findAll() throws IOException {
    MatchAllQueryBuilder query = QueryBuilders.matchAllQuery();
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

    return result.stream().map(owner -> {
      try {
        return mapper.writeValueAsString(owner);
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
      return "";
    }).collect(Collectors.toList());
  }


  protected Owner toResult(final SearchHit searchHit) {
    try {
      return mapper.readValue(searchHit.getSourceAsString(), Owner.class);
    } catch (Exception e) {
      throw new ElasticsearchException(e);
    }
  }
}
