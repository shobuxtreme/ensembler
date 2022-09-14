#!/bin/sh
# Author : Shubhankar Joshi
# enseMbLer Master Thesis Project

# Deploy enseMbLer core services
kubectl apply -f mongo-secret.yaml
kubectl apply -f mongo.yaml
kubectl apply -f rabbit.yaml
kubectl apply -f datasvc.yaml
kubectl apply -f executor.yaml
kubectl apply -f gui.yaml

# Customize Factorizer
kubectl apply -f factorizer.benchmark.yaml

# Deploy worker
kubectl apply -f worker.adab.yaml
kubectl apply -f worker.deepl.yaml
kubectl apply -f worker.deeplj.yaml
kubectl apply -f worker.deepljs.yaml
kubectl apply -f worker.extratrees.yaml
kubectl apply -f worker.gboost.yaml
kubectl apply -f worker.rforest.yaml

# Deploy l1 workers
kubectl apply -f worker.l1stackxgb.yaml
kubectl apply -f worker.maxvote.yaml