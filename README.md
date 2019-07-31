# Deploy Rundeck PRO 

This example deploy a Rundeck PRO cluster with Mysql DB and Minio as logstorage

## Create Secrets 

* Create Storage converter secret

Create a master password for the storage converter

```
echo -n 'masterpassword123.' > ./masterpassword
kubectl create secret generic rundeckpro-storage-converter --from-file=./masterpassword

```

* Create Storage Access Key

Create the AWS access key/secret to access the log storage (S3 or any similar storage based on S3, like minio)
```

echo -n 'minio123' > ./awssecret
kubectl create secret generic rundeckpro-log-storage --from-file=./awskey --from-file=./awssecret
```

* Create Mysql database password

```
echo -n 'rundeck123.' > ./password
kubectl create secret generic mysql-rundeckuser --from-file=./password
```

* Create LDAP password

```
echo -n 'AdminPass123' > ./password
kubectl create secret generic ldap-password --from-file=./password
```


## Create config files as secret

* Create License Key Secret

Add the Rundeck PRO license key as secret.

```
kubectl create secret generic rundeckpro-license --from-file=./data/rundeckpro-license.key
```

* Add custom ACL from secrets

Custom acls

```
kubectl create secret generic rundeckpro-admin-acl --from-file=./data/admin-role.aclpolicy
```

* Add custom logback.groovy and log4j.properties from secrets


```
kubectl create secret generic logback --from-file=./data/logback.groovy
kubectl create secret generic log4j --from-file=./data/log4j.properties

```

* Create LDAP init file

Add the Rundeck PRO license key as secret.

```
kubectl create secret generic bootstrap.ldif --from-file=./ldap/50-bootstrap.ldif
```


## Deploy database and storage (Optional)

A common database and log-storage is needed on a rundeckpro Cluster.
This is "optional" in case you want to use a db/storage located on k8s.

```
kubectl apply -f persistent-volumes.yaml
kubectl apply -f minio-deployment.yaml
kubectl apply -f mysql-deployment.yaml
kubectl apply -f ldap-deployment.yaml

```

## Deploy Rundeck 

### Ingress Controller

We use on this example an Nginx ingress controller, which allows us to set the sticky sessions configuration
(that are needed on a Rundeck cluster environment).

We are using this ingress controller: https://kubernetes.github.io/ingress-nginx/ 
You will need to install it in order to make this example works (see https://kubernetes.github.io/ingress-nginx/deploy/):


* The following Mandatory Command is required.


```
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/master/deploy/static/mandatory.yaml

```

* Provider Specific Steps for nginx ingress (see https://kubernetes.github.io/ingress-nginx/deploy/)

For example for a local docker mac environment: 
```
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/master/deploy/static/provider/cloud-generic.yaml

```
* create rundeckpro deployment


```
kubectl apply -f rundeckpro-deployment.yaml

```


## Uninstall 

```

kubectl delete deployment,service rundeckpro
kubectl delete ingress rudeckpro-nginx
kubectl delete deployment,service mysql 
kubectl delete deployment,service minio 
kubectl delete job minio-create-bucket 



```