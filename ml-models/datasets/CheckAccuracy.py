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
    datasetRef = loadDataset('predict.csv')
    yRef = datasetRef[y]

    dataset = loadDataset('predict.results.csv')
    yCheck = dataset['0']

    # 2. Measure Accuracy
    accuracy = metrics.accuracy_score(yRef, yCheck)

    endTime = time.time()
    print("Accuracy: ", accuracy)
    print('Scoring Finished in %.2fs' % (endTime-startTime))
    return accuracy


def loadDataset(name):
    # load the dataset
    return pd.read_csv(name)


cli.add_command(score)

if __name__ == '__main__':
    cli()
