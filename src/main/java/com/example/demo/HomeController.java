package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class HomeController {
    @SneakyThrows
    @GetMapping("/")
    @ResponseBody
    public Map<String, Object> showMain() {

        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("elastic", "elasticpassword"));

        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("192.168.56.112", 9200, "http"))
                        .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                                .setDefaultCredentialsProvider(credentialsProvider)));

        Request request = new Request("POST", "/_sql?format=json");
        request.setJsonEntity(
                "{" +
                        "  \"query\": \"SELECT * FROM sample1_dev___products_product_type_2___v1\"" +
                        "}");

        Response response = client.getLowLevelClient().performRequest(request);
        Map<String, Object> responseMap =
                new ObjectMapper().readValue(EntityUtils.toString(response.getEntity()), Map.class);

        return responseMap;
    }
}
