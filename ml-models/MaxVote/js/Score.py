# Imports
import numpy as np
import pandas as pd
import joblib
import time
import click
from sklearn.ensemble import AdaBoostClassifier
from sklearn import metrics


@click.group()
def cli():
    pass


@click.command()
@click.option('--y', '-y', required=True)
def score(y):
    startTime = time.time()

    # 1. Load Test Datasets
    datasetRef = loadDataset('validate.csv')
    yRef = datasetRef[y]

    dataset = loadDataset('validate.results.csv')
    yCheck = dataset['0']

    # 2. Measure Accuracy
    accuracy = metrics.accuracy_score(yRef, yCheck)
    confusion = metrics.confusion_matrix(yRef, yCheck)
    accuracy = metrics.accuracy_score(yRef, yCheck)
    precision = metrics.precision_score(yRef, yCheck)
    recall = metrics.recall_score(yRef, yCheck)
    f1score = metrics.f1_score(yRef, yCheck)

    endTime = time.time()
    print("Confusion Matrix:\n", confusion)
    print("Accuracy: ", accuracy)
    print("Precision: ", precision)
    print("Recall: ", recall)
    print("F1 Score: ", f1score)
    print('Scoring Finished in %.2fs' % (endTime-startTime))
    return accuracy


def loadDataset(name):
    # load the dataset
    return pd.read_csv(name)


cli.add_command(score)

if __name__ == '__main__':
    cli()
