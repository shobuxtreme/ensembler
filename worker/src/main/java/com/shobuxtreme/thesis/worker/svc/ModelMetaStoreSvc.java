package com.shobuxtreme.thesis.worker.svc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shobuxtreme.thesis.core.LevelDataStore;
import com.shobuxtreme.thesis.core.ModelMetaRequest;
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
public class ModelMetaStoreSvc {

    private final WebClient client;

    public ModelMetaStoreSvc(WebClient.Builder builder) {
        this.client = builder.build();
    }

    public void saveModelMeta(final String url, final ModelMetaRequest request) throws IOException {
        Mono<ModelMetaRequest> response = client.post()
                .uri(url)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), ModelMetaRequest.class)
                .retrieve()
                .bodyToMono(ModelMetaRequest.class);

        response.block();
    }

    public ModelMetaRequest getModelMeta(final String urlBase, final String modelId) {
        final String url = urlBase+"models/"+modelId;
        Mono<ModelMetaRequest> response = client.get()
                .uri(url)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ModelMetaRequest.class);

        return response.block();
    }

    @SuppressWarnings("DuplicatedCode")
    public List<ModelMetaRequest> getModelsForLevelByJob(final String urlBase, final String jobId, final int level) {
        final String url = urlBase+"models/job/"+jobId+"/level/"+(level-1);
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
                .map(object -> mapper.convertValue(object, ModelMetaRequest.class))
                .collect(Collectors.toList());
    }

    public LevelDataStore getLevelValidateResultsByJob(final String urlBase, final String jobId, final int level) {
        final String url = urlBase + "models/job/" + jobId + "/level/" + (level) + "/validateResults";
        Mono<LevelDataStore> response = client.get()
                .uri(url)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(LevelDataStore.class);

        return response.block();
    }

    public LevelDataStore getLevelPredictResultsByJob(final String urlBase, final String jobId, final int level) {
        final String url = urlBase + "models/job/" + jobId + "/level/" + (level) + "/predictResults";
        Mono<LevelDataStore> response = client.get()
                .uri(url)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(LevelDataStore.class);

        return response.block();
    }
}
