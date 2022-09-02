git pull
docker build -t payment-backend .
docker tag payment-backend:latest 811288377093.dkr.ecr.$AWS_REGION.amazonaws.com/payment-backend:latest
aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin 811288377093.dkr.ecr.us-west-2.amazonaws.com/
docker push 811288377093.dkr.ecr.us-west-2.amazonaws.com/payment-backend:latest
kubectl delete -f manifests/payment-deployment.yaml
kubectl apply -f manifests/payment-deployment.yaml
kubectl get pod
kubectl apply -f manifests/payment-service.yaml
kubectl get service
