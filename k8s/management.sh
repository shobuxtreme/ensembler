#!/bin/sh
# Author : Shubhankar Joshi
# enseMbLer Master Thesis Project

# SSH Into pod
kubectl exec --stdin --tty shell-demo -- /bin/bash

# Scale replicas
kubectl scale deployment.apps/factorizer-dep --replicas 7


