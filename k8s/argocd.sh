#!/bin/bash

kubectl create namespace argocd
kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/v2.4.7/manifests/install.yaml

if kubectl patch svc argocd-server -n argocd -p '{"spec": {"type": "LoadBalancer"}}'; then
  echo "Service patched successfully. Waiting for gitrank-argocd LoadBalancer to become active."

  while [[ $(kubectl get svc argocd-server -n argocd -o 'jsonpath={..status.loadBalancer.ingress[0].hostname}') == "" ]]; do 
    echo "Waiting for gitrank-argocd LoadBalancer to become active."
    sleep 10;
  done

  echo "gitrank-argocd LoadBalancer is active."
  export ARGOCD_SERVER=`kubectl get svc argocd-server -n argocd -o json | jq --raw-output '.status.loadBalancer.ingress[0].hostname'`
  export ARGO_PWD=`kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 -d`
  argocd login $ARGOCD_SERVER --username admin --password $ARGO_PWD --insecure
else
  echo "Failed to patch the service."
fi
