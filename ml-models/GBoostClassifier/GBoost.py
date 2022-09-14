# Imports
import numpy as np
import pandas as pd
import joblib
import time
import click
from sklearn.ensemble import GradientBoostingClassifier
from sklearn import metrics


@click.group()
def cli():
    pass


@click.command()
@click.option('--n_estimators', default=400, required=True)
@click.option('--max_depth', default=6, required=True)
@click.option('--x', '-x', required=True, multiple=True)
@click.option('--y', '-y', required=True)
def train(n_estimators, max_depth, x, y):
    startTime = time.time()

    # 1. Load dataset
    dataset = loadDataset('train.csv')

    # 2. Slect Features
    features = np.asarray(x)
    X = pd.get_dummies(dataset[features])
    y = dataset[y]

    # 3. Create ML Algorithm
    model = GradientBoostingClassifier(
        n_estimators=n_estimators, max_depth=max_depth)

    # 4. Train the model
    model.fit(X, y)

    # 5. Save Model
    saveModel(model)

    endTime = time.time()
    timeElapsed = endTime-startTime
    print('Training Finished in %.2fs' % (timeElapsed))
    return timeElapsed


@click.command()
@click.option('--x', '-x', required=True, multiple=True)
@click.option('--y', '-y', required=True)
def validate(x, y):
    startTime = time.time()

    # 1. Load Model
    model = loadModel()

    # 2. Load Test Dataset
    dataset = loadDataset('validate.csv')
    features = np.asarray(x)
    X = pd.get_dummies(dataset[features])
    y = dataset[y]

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
@click.option('--x', '-x', required=True, multiple=True)
@click.option('--y', '-y', required=True)
def predict(x, y):
    startTime = time.time()

    # 1. Load Model
    model = loadModel()

    # 2. Load Test Dataset
    dataset = loadDataset('predict.csv')
    features = np.asarray(x)
    X = pd.get_dummies(dataset[features])
    y = dataset[y]

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
