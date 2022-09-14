package com.shobuxtreme.thesis.executor.svc;

import com.shobuxtreme.thesis.core.*;
import com.shobuxtreme.thesis.executor.amqp.AMQPConfig;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service("executeService")
public class ExecuteService {

    @Qualifier("rabbitTemplate")
    private final RabbitTemplate template;

    @Qualifier("rabbitAdmin")
    private final AmqpAdmin amqpAdmin;

    private final AMQPConfig amqpConfig;

    private int currentLevel;

    private int toBeProcessed;

    private final Object mutex = new Object();

    public ExecuteService(RabbitTemplate template, AmqpAdmin amqpAdmin, AMQPConfig amqpConfig) {
        this.template = template;
        this.amqpAdmin = amqpAdmin;
        this.amqpConfig = amqpConfig;
        this.currentLevel = 0;
        this.toBeProcessed = 0;
    }

    @RabbitListener(queues = {AMQPConfig.QUEUE_ALL_RESULTS})
    public void consumeResult(ExecuteRequest response) throws InterruptedException {
        synchronized(mutex){
            System.out.println("Consumed: "+response.getId());
            --toBeProcessed;
            if(toBeProcessed == 0){
                System.out.println("Switch Level");
                Thread.sleep(500);
                ++currentLevel;
                mutex.notifyAll();
            }
        }
    }
    public void clear() {
        synchronized(mutex){
            toBeProcessed = 0;
            currentLevel = 0;
            mutex.notifyAll();
        }
        System.out.println("Job Session Cleared");
    }

    public void schedule(final JobRequest request){
        // Sort acc to levels
        final Set<ModelSet> sortedModelSets = request.getModelSet().values().stream()
                .sorted(Comparator.comparing(ModelSet::getLevel))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        for ( final ModelSet set: sortedModelSets ) {
            final Model baseModel = set.getModel();
            final int level = set.getLevel();

            if(baseModel.getLevel() ==-1 || baseModel.getLevel() != level){
                baseModel.setLevel(level);
            }

            // Declare Specific Queues
            final String queueName = "jobs."+baseModel.getModelName();
            Queue queue = new Queue(queueName);
            amqpAdmin.declareQueue(queue);

            final String bindingKey = "jobs."+baseModel.getModelName();
            Binding binding = BindingBuilder
                    .bind(queue)
                    .to(amqpConfig.exchange())
                    .with(bindingKey);

            amqpAdmin.declareBinding(binding);

            // Check parameter tuning
            if (set.isTuneParameters()){
                // Shuffle numeric Parameters
            }

            // Await Level
            synchronized(mutex){
                while (currentLevel != level ) {
                    try {
                        mutex.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            // Send Requests
            for (int i = 0; i < set.getReplicas() ; ++i) {
                final Model model = new Model(baseModel);
                final ExecuteRequest reqMsg = new ExecuteRequest(
                        request.getJobId(), request.getJobKind(), model,
                        request.getTrainDataSource(), request.getValidateDataSource(),
                        request.getPredictDataSource(), request.getFactorizeCmd()
                );

                System.out.println("Produced: "+reqMsg.getId());
                template.convertAndSend(AMQPConfig.EXCHANGE_JOBS,
                        bindingKey, reqMsg);
                ++toBeProcessed;
            }
        }

        clear();
    }

    private String generateParameters(final Model model){
        StringBuilder params = new StringBuilder();
        for ( final ModelParameter param : model.getParameters() ){
            final String paramKeyValue =  param.getName()+"="+param.getStringValue();
            params.append("--");
            params.append(paramKeyValue);
            params.append(" ");
        }
        return params.toString();
    }
}
