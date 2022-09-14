package com.shobuxtreme.thesis.datasvc.svc;

import com.shobuxtreme.thesis.core.ExecuteRequest;
import com.shobuxtreme.thesis.datasvc.mongo.repository.ExecuteResponseRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ExecuteSvc {

    private final ExecuteResponseRepository executeRepository;

    public ExecuteSvc(ExecuteResponseRepository executeRepository) {
        this.executeRepository = executeRepository;
    }

    public ExecuteRequest storeResponse(final ExecuteRequest request) {
        this.executeRepository.save(request);
        return request;
    }

    public ExecuteRequest getResponse(final String id) {
        final Optional<ExecuteRequest> response = executeRepository.findById(id);
        return response.orElse(null);
    }

    public List<ExecuteRequest> getAllResponsesForJobId(final String jobId){
        return executeRepository.findAllByJobId(jobId);
    }

    public List<ExecuteRequest> getAllResponsesForModelId(final String modelId){
        return executeRepository.findAllByModelId(modelId);
    }
}
