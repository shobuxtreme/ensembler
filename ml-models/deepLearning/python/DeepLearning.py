# Imports
import numpy as np
import pandas as pd
import joblib
from sklearn import metrics
from keras.models import Sequential
from keras.layers import Dense
import time
import click


@click.group()
def cli():
    pass


@click.command()
@click.option('--epochs', default=150, help='Number of epochs.', required=True)
@click.option('--batchsize', default=10, help='Batch size.', required=True)
@click.option('--lossfn', default='binary_crossentropy', type=str, help='Loss function to minimize.', required=True)
@click.option('--x', '-x', required=True, multiple=True)
@click.option('--y', '-y', required=True)
def train(epochs, batchsize, lossfn, x, y):
    startTime = time.time()

    # 1. Load dataset
    dataset = loadDataset('train.csv')

    # 2. Slect Features
    features = np.asarray(x)
    X = pd.get_dummies(dataset[features])
    y = dataset[y]

    # 3. Create ML Algorithm
    model = Sequential()
    model.add(Dense(12, input_dim=8, activation='relu'))
    model.add(Dense(8, activation='relu'))
    model.add(Dense(1, activation='sigmoid'))
    #model = loadModel();
    # compile the keras model
    model.compile(loss=lossfn,
                  optimizer='adam', metrics=['accuracy'])

    # 4. Train the model
    model.fit(X, y, epochs=epochs, batch_size=batchsize, verbose=0)

    # 5. Save Model
    saveModel(model)
    model.save("latest.model.h5")

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
    y_Predict = (model.predict(X, verbose=0) > 0.5).astype(int)

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
    y_Predict = (model.predict(X) > 0.5).astype(int)

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
