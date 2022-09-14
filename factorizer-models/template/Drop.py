# Commands
import pandas as pd
from sklearn.model_selection import train_test_split
import click
import time


@click.group()
def cli():
    pass


@click.command()
def drop():
    startTime = time.time()

    # 1. Load Data
    dataset = loadDataset("data-51200k.csv")

    # 2. Factorize

    # 3. Save dataset
    saveDataset('data-51200k.csv', dataset)

    endTime = time.time()
    print('Data saved to dislk.')
    print('Factorization Finished in %.2fs' % (endTime-startTime))


def loadDataset(name):
    # load the dataset
    return pd.read_csv(name)


def saveDataset(name, data):
    # save the dataset
    return pd.DataFrame(data).to_csv(name, index=False, columns=["Pregnancies", "Glucose", "BloodPressure", "SkinThickness", "Insulin", "BMI", "DiabetesPedigreeFunction", "Age", "Outcome"])


cli.add_command(drop)

if __name__ == '__main__':
    cli()
