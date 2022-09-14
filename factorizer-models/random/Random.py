# Commands
# python3 Random.py factorize
import pandas as pd
from sklearn.model_selection import train_test_split
import click
import time


@click.group()
def cli():
    pass


@click.command()
@click.option('--test_size', default=.2)
@click.option('--random_state', default=-1)
def factorize(test_size, random_state):
    startTime = time.time()

    # 1. Load Data
    data = loadDataset('data.csv')

    # 2. Factorize
    if(random_state == -1):
        train, test, = train_test_split(
            data, test_size=test_size)
    else:
        train, test, = train_test_split(
            data, test_size=test_size, random_state=random_state)

    # 3. Save dataset
    saveDataset('train.csv', train)
    saveDataset('test.csv', test)

    endTime = time.time()
    print('Data saved to dislk.')
    print('Factorization Finished in %.2fs' % (endTime-startTime))


def loadDataset(name):
    # load the dataset
    return pd.read_csv(name)


def saveDataset(name, data):
    # save the dataset
    return pd.DataFrame(data).to_csv(name)


cli.add_command(factorize)

if __name__ == '__main__':
    cli()
