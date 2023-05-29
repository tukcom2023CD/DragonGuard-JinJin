# !/bin/sh

kubectl apply -f ./k8s
kubectl apply -f ./k8s/ingress
kubectl apply -f ./k8s/database
kubectl apply -f ./k8s/zookeeper
kubectl apply -f ./k8s/kafka
kubectl apply -f ./k8s/kafdrop
kubectl apply -f ./k8s/redis
kubectl apply -f ./k8s/backend
kubectl apply -f ./k8s/scraping
kubectl apply -f ./k8s/elk
