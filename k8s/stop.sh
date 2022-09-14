#!/bin/sh
# Author : Shubhankar Joshi
# enseMbLer Master Thesis Project

# Delete Factorizer
kubectl delete deployment.apps/factorizer-dep service/factorizer-svc

# Delete Core Services
kubectl delete deployment.apps/factorizer-dep service/factorizer-svc
kubectl delete deployment.apps/executor-dep service/executor-svc
kubectl delete deployment.apps/datasvc-dep service/datasvc-svc
kubectl delete deployment.apps/mongo-dep service/mongo-svc
kubectl delete deployment.apps/rabbitmq-dep service/rabbitmq-svc
kubectl delete deployment.apps/gui-dep service/gui-svc

# Delete workers
kubectl delete deployment.apps/worker-adab-dep                               
kubectl delete deployment.apps/worker-deepl-dep                              
kubectl delete deployment.apps/worker-deeplj-dep                             
kubectl delete deployment.apps/worker-deepljs-dep                            
kubectl delete deployment.apps/worker-extratrees-dep                         
kubectl delete deployment.apps/worker-gboost-dep                             
kubectl delete deployment.apps/worker-rforest-dep 

# Delete l1 workers
kubectl delete deployment.apps/worker-maxvotejs-dep
kubectl delete deployment.apps/worker-maxvote-dep
kubectl delete deployment.apps/worker-l1stackxgb-dep
