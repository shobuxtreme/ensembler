# Imports
import numpy as np
import pandas as pd
import joblib
import time
import click
import xgboost as xgb
from sklearn import metrics


@click.group()
def cli():
    pass


@click.command()
@click.option('--n_inputs', default=6, required=True)
@click.option('--n_estimators', default=2000, required=True)
@click.option('--max_depth', default=4, required=True)
@click.option('--min_child_weight', default=2, required=True)
@click.option('--gamma', default=0.9, required=True)
@click.option('--subsample', default=.8, required=True)
@click.option('--colsample_bytree', default=.8, required=True)
@click.option('--objective', default='binary:logistic', required=True)
@click.option('--scale_pos_weight', default=1, required=True)
@click.option('--y', '-y', required=True)
def train(n_inputs, n_estimators, max_depth, min_child_weight, gamma, subsample, colsample_bytree, objective, scale_pos_weight, y):
    startTime = time.time()

    # 1. Load dataset
    dataset = loadDataset('train.csv')
    y = dataset[y]

    datasets = []
    for x in range(1, n_inputs+1):
        dataset = loadDataset('train.' + str(x) + '.csv')
        datasets.append(dataset)

    X = np.column_stack(datasets)
    saveDataset('train.csv', X)

    # 3. Create ML Algorithm
    model = xgb.XGBClassifier(
        n_estimators=n_estimators, max_depth=max_depth, min_child_weight=min_child_weight,
        gamma=gamma, subsample=subsample, colsample_bytree=colsample_bytree,
        objective=objective, scale_pos_weight=scale_pos_weight
    )

    # 4. Train the model
    model.fit(X, y)

    # 5. Save Model
    saveModel(model)

    y_Predict = model.predict(X)
    saveDataset('train.results.csv', y_Predict)

    # 5. Measure Accuracy
    accuracy = metrics.accuracy_score(y, y_Predict)

    print("Accuracy: ", accuracy)

    endTime = time.time()
    timeElapsed = endTime-startTime
    print('Training Finished in %.2fs' % (timeElapsed))
    return timeElapsed


@click.command()
@click.option('--n_inputs', default=6, required=True)
@click.option('--y', '-y', required=True)
def validate(n_inputs, y):
    startTime = time.time()

    # 1. Load Model
    model = loadModel()

    # 2. Load dataset
    dataset = loadDataset('validate.csv')
    y = dataset[y]

    datasets = []
    for x in range(1, n_inputs+1):
        dataset = loadDataset('validate.' + str(x) + '.csv')
        datasets.append(dataset)

    X = np.column_stack(datasets)
    saveDataset('validate.csv', X)

    # 3. Predict Using Model
    y_Predict = model.predict(X)

    # 4. Save Result
    saveDataset('validate.results.csv', y_Predict)

    # 5. Measure Accuracy
    confusion = metrics.confusion_matrix(y, y_Predict)
    accuracy = metrics.accuracy_score(y, y_Predict)
    precision = metrics.precision_score(y, y_Predict)
    recall = metrics.recall_score(y, y_Predict)
    f1score = metrics.f1_score(y, y_Predict)

    endTime = time.time()
    print("Confusion Matrix:\n", confusion)
    print("Accuracy: ", accuracy)
    print("Precision: ", precision)
    print("Recall: ", recall)
    print("F1 Score: ", f1score)
    print('Validation Finished in %.2fs' % (endTime-startTime))
    return accuracy


@click.command()
@click.option('--n_inputs', default=6, required=True)
@click.option('--y', '-y', required=True)
def predict(n_inputs, y):
    startTime = time.time()

    # 1. Load Model
    model = loadModel()

    # 2. Load Test Dataset
    dataset = loadDataset('predict.csv')
    y = dataset[y]

    datasets = []
    for x in range(1, n_inputs+1):
        dataset = loadDataset('predict.' + str(x) + '.csv')
        datasets.append(dataset)

    X = np.column_stack(datasets)
    saveDataset('predict.csv', X)

    # 3. Predict Using Model
    y_Predict = model.predict(X)

    # 4. Save Result
    saveDataset('predict.results.csv', y_Predict)

    endTime = time.time()
    print('Prediction Finished in %.2fs' % (endTime-startTime))


def loadDataset(name):
    # load the dataset
    return pd.read_csv(name)


def saveDataset(name, data):
    # save the dataset
    return pd.DataFrame(data).to_csv(name)


def saveModel(model):
    fileName = 'latest.model.joblib'
    joblib.dump(model, fileName)
    print("Saved model to disk")


def loadModel():
    fileName = 'latest.model.joblib'
    return joblib.load(fileName)


cli.add_command(train)
cli.add_command(validate)
cli.add_command(predict)

if __name__ == '__main__':
    cli()

# python3 L1-XGBoost.py train --n_inputs=6 --y=Outcome
# python3 L1-XGBoost.py validate - -n_inputs = 6 - -y = Outcome
# python3 L1-XGBoost.py predict --n_inputs=6 --y=Outcome
