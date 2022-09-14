package com.shobuxtreme.thesis.datasvc.mongo.repository;

import com.shobuxtreme.thesis.core.JobRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JobRepository extends MongoRepository<JobRequest, String> {
    JobRequest findJobByJobName(String jobName);
}
