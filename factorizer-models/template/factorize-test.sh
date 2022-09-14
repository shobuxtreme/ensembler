#!/bin/bash
# 51200k
mv data-51200k.csv data.csv
echo "## 51200k ##"
echo "Stratified"
python3 Stratified.py factorize --test_size .002
python3 Stratified.py factorize --test_size .002
python3 Stratified.py factorize --test_size .002
python3 Stratified.py factorize --test_size .002
python3 Stratified.py factorize --test_size .002
python3 Stratified.py factorize --test_size .002
python3 Stratified.py factorize --test_size .002
python3 Stratified.py factorize --test_size .002
python3 Stratified.py factorize --test_size .002
python3 Stratified.py factorize --test_size .002
echo "Random"
python3 Random.py factorize --test_size .002
python3 Random.py factorize --test_size .002
python3 Random.py factorize --test_size .002
python3 Random.py factorize --test_size .002
python3 Random.py factorize --test_size .002
python3 Random.py factorize --test_size .002
python3 Random.py factorize --test_size .002
python3 Random.py factorize --test_size .002
python3 Random.py factorize --test_size .002
python3 Random.py factorize --test_size .002