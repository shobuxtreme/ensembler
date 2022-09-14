#!/bin/bash
python3 Stratified.py factorize --test_size .002
mv test.csv data-100k.csv

python3 Stratified.py factorize --test_size .004
mv test.csv data-200k.csv

python3 Stratified.py factorize --test_size .008
mv test.csv data-400k.csv

python3 Stratified.py factorize --test_size .016
mv test.csv data-800k.csv

python3 Stratified.py factorize --test_size .032
mv test.csv data-1600k.csv

python3 Stratified.py factorize --test_size .064
mv test.csv data-3200k.csv

python3 Stratified.py factorize --test_size .128
mv test.csv data-6400k.csv

python3 Stratified.py factorize --test_size .256
mv test.csv data-12800k.csv

python3 Stratified.py factorize --test_size .512
mv test.csv data-25600k.csv

python3 Stratified.py factorize --test_size .99
mv test.csv data-51200k.csv