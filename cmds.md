# cmds

## Docker Commands

BUILD Images from Dockerfile

```
docker build -t shobuxtreme/ensembler.worker:deepl .
docker push shobuxtreme/ensembler.worker:deepl
```

CREATE Network for ensembler

```
docker network create ensembler
```

RUN main containers

```
docker run -d --name rabbitMQ -p 15672:15672 -p 5672:5672 --network=ensembler rabbitmq:3-management

docker run -d --name=mongo -p 27017:27017 --network=ensembler mongo

docker run -d -p 9002:9002 --name=ensembler.datasvc --network=ensembler shobuxtreme/ensembler.datasvc --spring.data.mongodb.host=mongo

docker run -d -p 9000:9000 --name=ensembler.executor --network=ensembler shobuxtreme/ensembler.executor --spring.rabbitmq.host=rabbitMQ

docker run -d -p 9001:9001 --name=ensembler.factorizer --network=ensembler shobuxtreme/ensembler.factorizer:kstone --factorize.cmd="python3 KStone.py factorize"
```

Run worker containers

```
docker run -d -p 9011:9011 --network=ensembler shobuxtreme/ensembler.worker:adab --server.port=9011 --worker.model.name="AdaBoost" --spring.rabbitmq.host=rabbitMQ --ensembler.datasvc.url=ensembler.datasvc:9002/

docker run -d -p 9012:9012 --network=ensembler shobuxtreme/ensembler.worker:deepl --server.port=9012 --worker.model.name="DeepL" --spring.rabbitmq.host=rabbitMQ --ensembler.datasvc.url=ensembler.datasvc:9002/

docker run -d -p 9013:9013 --network=ensembler shobuxtreme/ensembler.worker:deeplj --server.port=9013 --worker.model.name="DeepLJ" --spring.rabbitmq.host=rabbitMQ --ensembler.datasvc.url=ensembler.datasvc:9002/

docker run -d -p 9014:9014 --network=ensembler shobuxtreme/ensembler.worker:deepljs --server.port=9014 --worker.model.name="DeepLJS" --spring.rabbitmq.host=rabbitMQ --ensembler.datasvc.url=ensembler.datasvc:9002/

docker run -d -p 9015:9015 --network=ensembler shobuxtreme/ensembler.worker:extratrees --server.port=9015 --worker.model.name="ExtraT" --spring.rabbitmq.host=rabbitMQ --ensembler.datasvc.url=ensembler.datasvc:9002/

docker run -d -p 9016:9016 --network=ensembler shobuxtreme/ensembler.worker:gboost --server.port=9016 --worker.model.name="GBoost" --spring.rabbitmq.host=rabbitMQ --ensembler.datasvc.url=ensembler.datasvc:9002/

docker run -d -p 9017:9017 --network=ensembler shobuxtreme/ensembler.worker:rforest --server.port=9017 --worker.model.name="RForest" --spring.rabbitmq.host=rabbitMQ --ensembler.datasvc.url=ensembler.datasvc:9002/

docker run -d -p 9018:9018 --network=ensembler shobuxtreme/ensembler.worker:maxvotejs --server.port=9018 --worker.model.name="MaxVoteJS" --spring.rabbitmq.host=rabbitMQ --ensembler.datasvc.url=ensembler.datasvc:9002/

docker run -d -p 9019:9019 --network=ensembler shobuxtreme/ensembler.worker:maxvote --server.port=9019 --worker.model.name="MaxVote" --spring.rabbitmq.host=rabbitMQ --ensembler.datasvc.url=ensembler.datasvc:9002/

docker run -d -p 9020:9020 --network=ensembler shobuxtreme/ensembler.worker:l1stackxgb --server.port=9020 --worker.model.name="L1-XGBoost" --spring.rabbitmq.host=rabbitMQ --ensembler.datasvc.url=ensembler.datasvc:9002/

docker run -d -p 3000:3000 --name=ensembler.gui --network=ensembler -e API_EXECUTOR="http://ensembler.executor:9000" -e API_DATA="" shobuxtreme/ensembler.gui
```

## Java Service Commands

Data Service

```
java -jar datasvc-0.0.1.jar
```

Executor Service

```
java -jar executor-0.0.1.jar
```

Factorizer Service

```
java -jar factorizer-0.0.1.jar --factorize.cmd="python3 Stratified.py factorize"

java -jar factorizer-0.0.1.jar --factorize.cmd="python3 KStone.py factorize"

java -jar factorizer-0.0.1.jar --factorize.cmd="python3 Random.py factorize"
```

Worker

```
java -jar worker-0.0.1.jar --server.port=9011 --worker.model.name="AdaBoost" --worker.model.img="adaB"

java -jar worker-0.0.1.jar --server.port=9012 --worker.model.name="DeepLJ" --worker.model.img="deepLJ"

java -jar worker-0.0.1.jar --server.port=9013 --worker.model.name="DeepLJS" --worker.model.img="deepLJS"

java -jar worker-0.0.1.jar --server.port=9014 --worker.model.name="DeepL" --worker.model.img="deepL"

java -jar worker-0.0.1.jar --server.port=9015 --worker.model.name="ExtraT" --worker.model.img="xTrees"

java -jar worker-0.0.1.jar --server.port=9016 --worker.model.name="GBoost" --worker.model.img="gBoost"

java -jar worker-0.0.1.jar --server.port=9017 --worker.model.name="RForest" --worker.model.img="rForest"

java -jar worker-0.0.1.jar --server.port=9018 --worker.model.name="MaxVoteJS" --worker.model.img="maxVoteJS"

java -jar worker-0.0.1.jar --server.port=9019 --worker.model.name="MaxVote" --worker.model.img="maxVote"

java -jar worker-0.0.1.jar --server.port=9020 --worker.model.name="L1-XGBoost" --worker.model.img="xgbStack"

```

Worker as Docker containers

```
docker run -d -p 9011:9011 test:2 --server.port=9011 --worker.model.name="AdaBoost" --worker.model.img="adaB" --spring.rabbitmq.host=172.17.0.2 --ensembler.datasvc.url=host.docker.internal:9002/

docker run -d -p 9012:9012 test:2 --server.port=9012 --worker.model.name="DeepLJ" --worker.model.img="deepLJ" --spring.rabbitmq.host=172.17.0.2 --ensembler.datasvc.url=host.docker.internal:9002/

docker run -d -p 9013:9013 test:2 --server.port=9013 --worker.model.name="DeepLJS" --worker.model.img="deepLJS" --spring.rabbitmq.host=172.17.0.2 --ensembler.datasvc.url=host.docker.internal:9002/

docker run -d -p 9014:9014 test:2 --server.port=9014 --worker.model.name="DeepL" --worker.model.img="deepL" --spring.rabbitmq.host=172.17.0.2 --ensembler.datasvc.url=host.docker.internal:9002/

docker run -d -p 9015:9015 test:2 --server.port=9015 --worker.model.name="ExtraT" --worker.model.img="xTrees" --spring.rabbitmq.host=172.17.0.2 --ensembler.datasvc.url=host.docker.internal:9002/

docker run -d -p 9016:9016 test:2 --server.port=9016 --worker.model.name="GBoost" --worker.model.img="gBoost" --spring.rabbitmq.host=172.17.0.2 --ensembler.datasvc.url=host.docker.internal:9002/

docker run -d -p 9017:9017 test:2 --server.port=9017 --worker.model.name="RForest" --worker.model.img="rForest" --spring.rabbitmq.host=172.17.0.2 --ensembler.datasvc.url=host.docker.internal:9002/

docker run -d -p 9018:9018 test:2 --server.port=9018 --worker.model.name="MaxVoteJS" --worker.model.img="maxVoteJS" --spring.rabbitmq.host=172.17.0.2 --ensembler.datasvc.url=host.docker.internal:9002/

docker run -d -p 9019:9019 test:2 --server.port=9019 --worker.model.name="MaxVote" --worker.model.img="maxVote" --spring.rabbitmq.host=172.17.0.2 --ensembler.datasvc.url=host.docker.internal:9002/

docker run -d -p 9020:9020 test:2 --server.port=9020 --worker.model.name="L1-XGBoost" --worker.model.img="xgbStack" --spring.rabbitmq.host=172.17.0.2 --ensembler.datasvc.url=host.docker.internal:9002/
```

## Machine Learning Model Commands

All Commands

```
## Factorizers

python3 Stratified.py factorize

python3 KStone.py factorize

python3 Random.py factorize

## Models

python3 AdaBoost.py train -x Pregnancies -x Glucose -x BloodPressure -x SkinThickness -x Insulin -x BMI -x DiabetesPedigreeFunction -x Age -y Outcome --n_estimators 400 --learning_rate 0.65

python3 AdaBoost.py validate -x Pregnancies -x Glucose -x BloodPressure -x SkinThickness -x Insulin -x BMI -x DiabetesPedigreeFunction -x Age -y Outcome

python3 AdaBoost.py predict -x Pregnancies -x Glucose -x BloodPressure -x SkinThickness -x Insulin -x BMI -x DiabetesPedigreeFunction -x Age -y Outcome

##

python3 DeepLearning.py train -x Pregnancies -x Glucose -x BloodPressure -x SkinThickness -x Insulin -x BMI -x DiabetesPedigreeFunction -x Age -y Outcome --epochs 150 --batchsize 10 --lossfn binary_crossentropy

python3 DeepLearning.py validate -x Pregnancies -x Glucose -x BloodPressure -x SkinThickness -x Insulin -x BMI -x DiabetesPedigreeFunction -x Age -y Outcome

python3 DeepLearning.py predict -x Pregnancies -x Glucose -x BloodPressure -x SkinThickness -x Insulin -x BMI -x DiabetesPedigreeFunction -x Age -y Outcome

##

node DeepLearning.js train --epochs 150 --batchsize 10 --lossfn BinaryCrossentropy --y Outcome

node DeepLearning.js validate --batchsize 10 --y Outcome

node DeepLearning.js predict --batchsize 10 --y Outcome

##

java -jar deepl.jar --train

java -jar deepl.jar --validate

java -jar deepl.jar --predict

##
python3 ExtraTrees.py train -x Pregnancies -x Glucose -x BloodPressure -x SkinThickness -x Insulin -x BMI -x DiabetesPedigreeFunction -x Age -y Outcome --n_estimators 400 --max_depth 5 --min_samples_leaf 2 --n_jobs 1

python3 ExtraTrees.py validate -x Pregnancies -x Glucose -x BloodPressure -x SkinThickness -x Insulin -x BMI -x DiabetesPedigreeFunction -x Age -y Outcome

python3 ExtraTrees.py predict -x Pregnancies -x Glucose -x BloodPressure -x SkinThickness -x Insulin -x BMI -x DiabetesPedigreeFunction -x Age -y Outcome

##

python3 GBoost.py train -x Pregnancies -x Glucose -x BloodPressure -x SkinThickness -x Insulin -x BMI -x DiabetesPedigreeFunction -x Age -y Outcome --n_estimators 400 --max_depth 6

python3 GBoost.py validate -x Pregnancies -x Glucose -x BloodPressure -x SkinThickness -x Insulin -x BMI -x DiabetesPedigreeFunction -x Age -y Outcome

python3 GBoost.py predict -x Pregnancies -x Glucose -x BloodPressure -x SkinThickness -x Insulin -x BMI -x DiabetesPedigreeFunction -x Age -y Outcome

##

python3 RandomForest.py train -x Pregnancies -x Glucose -x BloodPressure -x SkinThickness -x Insulin -x BMI -x DiabetesPedigreeFunction -x Age -y Outcome --n_estimators 400 --max_depth 5 --min_samples_leaf 3 --max_features sqrt

python3 RandomForest.py validate -x Pregnancies -x Glucose -x BloodPressure -x SkinThickness -x Insulin -x BMI -x DiabetesPedigreeFunction -x Age -y Outcome

python3 RandomForest.py predict -x Pregnancies -x Glucose -x BloodPressure -x SkinThickness -x Insulin -x BMI -x DiabetesPedigreeFunction -x Age -y Outcome

##

python3 L1-XGBoost.py train --n_inputs 5 --n_estimators 2000 --min_child_weight 2 --gamma 0.9 --subsample 0.8 --colsample_bytree 0.8 --objective  binary:logistic --scale_pos_weight 1 --y Outcome

python3 L1-XGBoost.py validate --n_inputs 5 --y Outcome

python3 L1-XGBoost.py predict --n_inputs 5 --y Outcome

##

node MaxVote.js vote --inputs 5

python3 MaxVote.py vote --n_inputs 5

## Toos

python3 CheckAccuracy.py score --y Outcome

```

Python Standard Train Command

```
python3 Template.py train -x Pregnancies -x Glucose -x BloodPressure -x SkinThickness -x Insulin -x BMI -x DiabetesPedigreeFunction -x Age -y Outcome
```

JS Node Standard Train Command

```
node DeepLearning.js train --epochs 150 --batchsize 10 --lossfn BinaryCrossentropy --x Pregnancies --x Glucose --x BloodPressure --x SkinThickness --x Insulin --x BMI --x DiabetesPedigreeFunction --x Age  --y Outcome
```
