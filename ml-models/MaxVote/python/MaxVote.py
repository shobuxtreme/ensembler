# Imports
import numpy as np
import pandas as pd
import time
import click


@click.group()
def cli():
    pass


@click.command()
def train():
    startTime = time.time()

    with open('latest.model.joblib', 'w') as fp:
        pass
        fp.write("Max Vote Algorithm")

    endTime = time.time()
    timeElapsed = endTime-startTime
    print('Max Vote Ready in %.2fs' % (timeElapsed))
    return timeElapsed


@click.command()
def validate():
    startTime = time.time()

    with open('validate.results.csv', 'w') as fp:
        pass

    endTime = time.time()
    timeElapsed = endTime-startTime
    print('Max Vote Ready in %.2fs' % (timeElapsed))
    return timeElapsed


@click.command()
@click.option('--n_inputs', default=5, required=True)
def vote(n_inputs):
    startTime = time.time()

    # 1. Load dataset
    datasets = []
    for x in range(1, n_inputs+1):
        # Pandas DataFrame
        dataset = loadDataset('predict.' + str(x) + '.csv')
        datasets.append(dataset.iloc[:, 1])

    X = np.column_stack(datasets)

    # 2. Prepare Max Count
    result = []
    for x in range(0, len(X)):
        max = findMajority(X[x], len(X[x]))
        if (max == -1):
            max = X[x][0]
        result.append(max)

    # 3. Store Result
    resultDf = pd.DataFrame(result)
    saveDataset('predict.results.csv', resultDf)

    endTime = time.time()
    timeElapsed = endTime-startTime
    print('Voting Finished in %.2fs' % (timeElapsed))
    return timeElapsed

# Boyer-Moore


def findMajority(arr, n):
    candidate = -1
    votes = 0

    # Finding majority candidate
    for i in range(n):
        if (votes == 0):
            candidate = arr[i]
            votes = 1
        else:
            if (arr[i] == candidate):
                votes += 1
            else:
                votes -= 1
    count = 0

    # Checking if majority candidate occurs more than n/2
    # times
    for i in range(n):
        if (arr[i] == candidate):
            count += 1

    if (count > n // 2):
        return candidate
    else:
        return -1


def loadDataset(name):
    # load the dataset
    return pd.read_csv(name)


def saveDataset(name, data):
    # save the dataset
    return pd.DataFrame(data).to_csv(name)


cli.add_command(train)
cli.add_command(validate)
cli.add_command(vote)

if __name__ == '__main__':
    cli()
