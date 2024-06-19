## Contribution

### Installation

Please install [Docker Desktop installer](https://docs.docker.com/desktop/install/windows-install/). Then:

- Install from the command line

```powershell
Start-Process 'Docker Desktop Installer.exe' -Wait -ArgumentList 'install', '--accept-license', '--installation-dir=D:\Application\Docker\installation-dir', '--hyper-v-default-data-root=D:\Application\Docker\hyper-v-default-data-root', '--windows-containers-default-data-root=D:\Application\Docker\windows-containers-default-data-root', '--wsl-default-data-root=D:\Application\Docker\wsl-default-data-root'
```

_Remark: Arguments `--installation-dir=<path>`, `--hyper-v-default-data-root=<path>`, `--windows-containers-default-data-root=<path>`, and `--wsl-default-data-root=<path>` to control Docker storage behavior._

- Enable Kubernetes
  - `Settings > Kubernetes > Enable Kubernetes`
  - Wait until completion
  - Validate installation with `kubectl version`

Please install [Helm](https://github.com/helm/helm/releases).

```bash
curl -fsSL -o get_helm.sh https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3
chmod 700 get_helm.sh
./get_helm.sh
rm ./get_helm.sh
helm version
```

Please install [Python](https://www.python.org/downloads/), [pip](https://pip.pypa.io/en/stable/installation/) and [venv](https://docs.python.org/3/library/venv.html).

```bash
sudo apt install python3 python3-pip python3-venv
```

Please install [Java 17 using Coursier](https://get-coursier.io/docs/cli-installation).

```bash
curl -fL "https://github.com/coursier/launchers/raw/master/cs-x86_64-pc-linux.gz" | gzip -d > cs
chmod +x cs
./cs setup -y --jvm 17
rm cs
source ~/.profile
java --version
```

Please install [Spark](https://spark.apache.org/downloads.html).

```bash
mkdir -p ~/apps/spark
curl -fLo ~/apps/spark/spark-3.5.1-bin-hadoop3-scala2.13.tgz https://dlcdn.apache.org/spark/spark-3.5.1/spark-3.5.1-bin-hadoop3-scala2.13.tgz
tar -xf ~/apps/spark/spark-3.5.1-bin-hadoop3-scala2.13.tgz -C ~/apps/spark
rm ~/apps/spark/spark-3.5.1-bin-hadoop3-scala2.13.tgz
echo -e '\nexport PATH="~/apps/spark/spark-3.5.1-bin-hadoop3-scala2.13/bin:$PATH"' >> ~/.bashrc
source ~/.bashrc
spark-submit --version
```

### Commands to install and manage isolated namespaces

Create namespace with quota, admin role and group.

```bash
# Create namespace with quota
kubectl create ns compordept-project-env # Create the namespace.
kubectl config set-context docker-desktop --namespace=compordept-project-env # Switch to the namespace.
echo '
---
apiVersion: v1
kind: ResourceQuota
metadata:
  name: namespace-quota
spec:
  hard:
    limits.memory: "1939Mi"
    requests.cpu: "3"
    requests.memory: "1939Mi"
    persistentvolumeclaims: "9"
    requests.ephemeral-storage: "227Gi"
    limits.ephemeral-storage: "227Gi"
    pods: "27"
    services: "9"
' | kubectl apply -f -

# Create namespace admin role and group
echo '
import subprocess, json
kubectl_get_raw_as_dict = lambda path: json.loads(subprocess.check_output(f"kubectl get --raw {path}", shell=True, text=True, stderr=subprocess.PIPE))
print_as_yaml_array = lambda list_of_strings: print(json.dumps(list_of_strings))

paths = kubectl_get_raw_as_dict("/")["paths"]
output_resources = []
for path in paths:
  try:
    resources = kubectl_get_raw_as_dict(path)["resources"]
    resources = [resource["name"] for resource in resources if resource["namespaced"] == True and (resource["name"] not in ["resourcequotas", "roles"])]
    output_resources.extend(resources)
  except:
    pass
print_as_yaml_array(output_resources)
' | python3 | xargs -I {} echo '
---
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  name: namespace-admin
rules:
  - apiGroups: ["*"]
    resources: {}
    verbs: ["*"]
  - apiGroups: ["*"]
    resources: ["resourcequotas"]
    verbs: ["get", "list", "watch"]
  - apiGroups: ["*"]
    resources: ["roles"]
    verbs: ["get", "list", "watch"]

---
apiVersion: rbac.authorization.k8s.io/v1
kind: RoleBinding
metadata:
  name: namespace-admin
subjects:
  - kind: Group
    name: namespace-admin
    apiGroup: rbac.authorization.k8s.io
roleRef:
  kind: Role
  name: namespace-admin
  apiGroup: rbac.authorization.k8s.io
' | kubectl apply -f -
```

Create K8S certificate for a user of the group namespace-admin.

- [CSR, CA and CRT](https://kubernetes.io/docs/reference/access-authn-authz/certificate-signing-requests/#normal-user)
- [CSR, CA and CRT in practice](https://youtu.be/dnKVZR4eK7Q)

```bash
# Generate CSR (on user side)
openssl genrsa -out compordept-project-env-admin.key 2048
openssl req -new -key compordept-project-env-admin.key -out compordept-project-env-admin.csr -subj "/CN=namespace-admin/O=namespace-admin"

# Validate CSR & Generate CRT (on K8S admin side)
cat compordept-project-env-admin.csr | base64 | tr -d "\n" | xargs -I {} echo '
---
apiVersion: certificates.k8s.io/v1
kind: CertificateSigningRequest
metadata:
  name: compordept-project-env-admin
spec:
  request: {}
  signerName: kubernetes.io/kube-apiserver-client
  expirationSeconds: 7898368 # 3 months
  usages:
    - client auth
' | kubectl apply -f -
kubectl certificate approve compordept-project-env-admin
kubectl get csr compordept-project-env-admin -o jsonpath='{.status.certificate}'| base64 -d > compordept-project-env-admin.crt # Certificate to give to your user

# Set CRT and use created user (on user side)
kubectl config set-credentials compordept-project-env-admin --client-key=compordept-project-env-admin.key --client-certificate=compordept-project-env-admin.crt --embed-certs=true
kubectl config set-context docker-desktop --user=compordept-project-env-admin
rm compordept-project-env-admin.* # Purge certificates from disk
```

Purge namespace.

```bash
kubectl config set-context docker-desktop --user=docker-desktop # Set back to the K8s admin user
kubectl config set-context docker-desktop --namespace=default # Set back to the default namespace
kubectl delete csr compordept-project-env-admin
kubectl delete ns compordept-project-env
```

### Commands for Spark

To install Spark service account on a given namespace.

```bash
helm install devops-spark . -n compordept-project-env
helm list
```

To submit a Spark application.

```bash
spark-submit --master k8s://https://kubernetes.docker.internal:6443 --deploy-mode cluster --name spark-app --class org.apache.spark.examples.SparkPi --conf spark.kubernetes.driver.request.cores=50m --conf spark.kubernetes.executor.request.cores=200m --conf spark.driver.memory=512m --conf spark.executor.memory=512m --conf spark.executor.instances=1 --conf spark.kubernetes.container.image=spark:3.5.1-scala2.12-java17-ubuntu --conf spark.kubernetes.namespace=compordept-project-env --conf spark.kubernetes.authenticate.driver.serviceAccountName=spark-sa local:///opt/spark/examples/jars/spark-examples_2.12-3.5.1.jar 1
```

To kill all Spark applications.

```bash
spark-submit --kill compordept-project-env:spark-app* --master k8s://https://kubernetes.docker.internal:6443
kubectl get pods
```

To uninstall Spark service account.

```bash
helm uninstall devops-spark -n compordept-project-env
helm list
```
