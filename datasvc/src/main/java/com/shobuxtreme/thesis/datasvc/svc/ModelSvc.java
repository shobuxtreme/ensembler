package com.shobuxtreme.thesis.datasvc.svc;

import com.shobuxtreme.thesis.core.ModelMetaRequest;
import com.shobuxtreme.thesis.datasvc.mongo.model.ModelStore;
import com.shobuxtreme.thesis.datasvc.mongo.repository.ModelRepository;
import org.bson.BsonMaximumSizeExceededException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ModelSvc {

    private final ModelRepository modelRepository;

    public ModelSvc(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    public ModelMetaRequest findByModelId(final String modelId) {
        return convertToBo(modelRepository.findByModelId(modelId));
    }

    public List<ModelMetaRequest> findByJobId(final String jobId) {
        return convertToBo(modelRepository.findByJobId(jobId));
    }

    public List<ModelMetaRequest> findModelsInLevelByJobId(final String jobId, final int level) {

        List<ModelStore> models = modelRepository.findByJobId(jobId).stream()
                .filter(modelStore -> modelStore.getLevel() == level).toList();

        return convertToBo(models);
    }
    public ModelStore storeModel(final ModelMetaRequest request) {
        System.out.println(request);
        final ModelStore requestModel = new ModelStore(request);

        final ModelStore existingModel = modelRepository.findByModelId(requestModel.getModelId());
        if(existingModel==null){
            try{
                //MongoDB BSON Document max size 16 MB
                modelRepository.save(requestModel);
            }catch (BsonMaximumSizeExceededException e){
                requestModel.setTrainBin(null);
            }

            return requestModel;
        }

        existingModel.update(requestModel);
        try{
            //MongoDB BSON Document max size 16 MB
            modelRepository.save(existingModel);
        }catch (BsonMaximumSizeExceededException e){
            existingModel.setTrainBin(null);
        }
        return existingModel;
    }

    private List<ModelMetaRequest> convertToBo(final List<ModelStore> list) {
        List<ModelMetaRequest> response = new ArrayList<>();
        if(!list.isEmpty()){
            list.forEach(modelStore -> response.add(convertToBo(modelStore)));
        }
        return response;
    }

    private ModelMetaRequest convertToBo(final ModelStore modelStore) {

        if(modelStore==null){
            return null;
        }

        ModelMetaRequest model = new ModelMetaRequest(modelStore.getJobId(), modelStore.getModelId(), modelStore.getLevel());
        if(modelStore.getModelBin()!=null)
            model.setModel(modelStore.getModelBin().getData());
        if(modelStore.getTrainBin()!=null)
            model.setTrainInput(modelStore.getTrainBin().getData());
        if(modelStore.getValidateBin()!=null)
            model.setValidateInput(modelStore.getValidateBin().getData());
        if(modelStore.getValidateResultBin()!=null)
            model.setValidateResult(modelStore.getValidateResultBin().getData());
        if(modelStore.getPredictBin()!=null)
            model.setPredictInput(modelStore.getPredictBin().getData());
        if(modelStore.getPredictResultBin()!=null)
            model.setPredictResult(modelStore.getPredictResultBin().getData());
        return model;
    }

}
