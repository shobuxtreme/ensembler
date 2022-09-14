package com.shobuxtreme.thesis.worker.svc;

import com.shobuxtreme.thesis.core.DataRequest;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;

@Component
public class DataFetchService {

    private final WebClient client;

    public DataFetchService(WebClient.Builder builder) {
        this.client = builder.build();
    }

    public DataRequest fetchDataset(final String url, final DataRequest request, final String saveAsName) throws IOException {
        Mono<DataRequest> response = client.post()
                .uri(url)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), DataRequest.class)
                .retrieve()
                .bodyToMono(DataRequest.class);

        final DataRequest data = response.block();
        if(data==null || data.getResult()!=0){
            request.setResult(-1);
            request.setResultMsg("Data Fetch from Client failed");
            return request;
        }
        FileUtils.writeByteArrayToFile(new File(saveAsName), data.getData());
        return data;
    }

}
