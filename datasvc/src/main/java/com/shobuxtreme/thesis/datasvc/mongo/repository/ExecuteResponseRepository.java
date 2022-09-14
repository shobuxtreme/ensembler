package com.shobuxtreme.thesis.datasvc.mongo.repository;

import com.shobuxtreme.thesis.core.ExecuteRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ExecuteResponseRepository extends MongoRepository<ExecuteRequest, String> {

        List<ExecuteRequest> findAllByJobId(String jobId);

        List<ExecuteRequest> findAllByModelId(String modelId);

}
