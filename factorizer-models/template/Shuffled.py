# Commands
import pandas as pd
from sklearn.model_selection import train_test_split
import click
import time


@click.group()
def cli():
    pass


@click.command()
@click.option('--fraction', default=.002)
@click.option('--test_size', default=.2)
@click.option('--random_state', default=-1)
@click.option('--dataset', default='data.csv')
@click.option('--y', '-y', default='Outcome', required=True)
def factorize(fraction, test_size, random_state, dataset, y):
    startTime = time.time()

    # 1. Load Data
    dataset = loadDataset(dataset)

    # 2. Factorize

    if(random_state == -1):
        data = dataset.sample(frac=fraction)
        train, test, = train_test_split(
            data, stratify=data[y], test_size=test_size)
    else:
        data = dataset.sample(frac=fraction, random_state=random_state)
        train, test, = train_test_split(
            data, stratify=data[y], test_size=test_size, random_state=random_state)

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
