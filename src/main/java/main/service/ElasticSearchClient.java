package main.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ElasticSearchClient {

    public ElasticSearchClient() {
        // Create the low-level client
        RestClient restClient = RestClient.builder(new HttpHost("https://elasticsearch.akogare.de/", 9200)).build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

        // And create the API client
        ElasticsearchClient client = new ElasticsearchClient(transport);
    }
}
