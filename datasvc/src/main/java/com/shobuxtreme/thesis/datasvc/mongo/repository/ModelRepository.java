package com.shobuxtreme.thesis.datasvc.mongo.repository;

import com.shobuxtreme.thesis.datasvc.mongo.model.ModelStore;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ModelRepository extends MongoRepository<ModelStore, String> {

    List<ModelStore> findByJobId(String jobId);

    ModelStore findByModelId(String modelId);
}
