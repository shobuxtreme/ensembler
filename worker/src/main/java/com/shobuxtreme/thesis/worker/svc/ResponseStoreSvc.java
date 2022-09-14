package com.shobuxtreme.thesis.worker.svc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shobuxtreme.thesis.core.ExecuteRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ResponseStoreSvc {

    private final WebClient client;

    public ResponseStoreSvc(WebClient.Builder builder) {
        this.client = builder.build();
    }

    public ExecuteRequest saveResponse(final String baseUrl, final ExecuteRequest request) throws IOException {
        final String url = baseUrl + "responses";
        Mono<ExecuteRequest> response = client.post()
                .uri(url)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), ExecuteRequest.class)
                .retrieve()
                .bodyToMono(ExecuteRequest.class);

        return response.block();
    }

    public ExecuteRequest getResponse(final String urlBase, final String id) {
        final String url = urlBase+"responses/"+id;
        Mono<ExecuteRequest> response = client.get()
                .uri(url)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ExecuteRequest.class);

        return response.block();
    }

    @SuppressWarnings("DuplicatedCode")
    public List<ExecuteRequest> getResponsesForJob(final String urlBase, final String jobId) {
        final String url = urlBase+"responses/job/"+jobId;
        Mono<Object[]> response = client.get()
                .uri(url)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Object[].class);

        Object[] objects = response.block();

        if(objects==null){
            return new ArrayList<>();
        }

        ObjectMapper mapper = new ObjectMapper();

        return Arrays.stream(objects)
                .map(object -> mapper.convertValue(object, ExecuteRequest.class))
                .collect(Collectors.toList());
    }

    @SuppressWarnings("DuplicatedCode")
    public List<ExecuteRequest> getResponsesForModel(final String urlBase, final String modelId) {
        final String url = urlBase+"responses/model/"+modelId;
        Mono<Object[]> response = client.get()
                .uri(url)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Object[].class);

        Object[] objects = response.block();

        if(objects==null){
            return new ArrayList<>();
        }

        ObjectMapper mapper = new ObjectMapper();

        return Arrays.stream(objects)
                .map(object -> mapper.convertValue(object, ExecuteRequest.class))
                .collect(Collectors.toList());
    }

}
