package com.shobuxtreme.thesis.datasvc.svc;

import com.shobuxtreme.thesis.core.LevelDataStore;
import com.shobuxtreme.thesis.datasvc.mongo.model.ModelStore;
import com.shobuxtreme.thesis.datasvc.mongo.repository.ModelRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LevelDataSvc {

    private final ModelRepository modelRepository;

    public LevelDataSvc(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    public LevelDataStore getValidateResultsInLevelByJobId(final String jobId, final int level) {

        List<byte[]> levelData = new ArrayList<>();

        List<ModelStore> models = modelRepository.findByJobId(jobId);
        models.forEach(model -> {
            if(model.getLevel()==level){
                if(model.getValidateResultBin()!=null)
                    levelData.add(model.getValidateResultBin().getData());
            }
        });

        return new LevelDataStore(jobId, level, levelData);
    }

    public LevelDataStore getPredictResultsInLevelByJobId(final String jobId, final int level) {

        List<byte[]> levelData = new ArrayList<>();

        List<ModelStore> models = modelRepository.findByJobId(jobId);
        models.forEach(model -> {
            if(model.getLevel()==level){
                if(model.getPredictResultBin()!=null)
                    levelData.add(model.getPredictResultBin().getData());
            }
        });

        return new LevelDataStore(jobId, level, levelData);
    }

}
