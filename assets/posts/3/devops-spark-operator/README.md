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

To install dependencies ([Spark operator](https://github.com/kubeflow/spark-operator)).

```bash
helm dependency update
```

### Commands

To install Spark operator on a given namespace.

```bash
helm install devops-spark-operator . -n compordept-project-env --create-namespace
kubectl config set-context docker-desktop --namespace=compordept-project-env
helm list
kubectl get ns
```

To [submit a Spark application](https://github.com/kubeflow/spark-operator/blob/master/docs/user-guide.md).

```bash
echo "
apiVersion: "sparkoperator.k8s.io/v1beta2"
kind: SparkApplication
metadata:
  name: spark-app
spec:
  type: Scala
  mode: cluster
  image: "spark:3.5.1"
  imagePullPolicy: Always
  mainClass: org.apache.spark.examples.SparkPi
  mainApplicationFile: "local:///opt/spark/examples/jars/spark-examples_2.12-3.5.1.jar"
  sparkVersion: "3.5.1"
  restartPolicy:
    type: Never
  driver:
    cores: 1
    memory: "512m"
    labels:
      version: 3.5.1
    serviceAccount: spark-sa
  executor:
    cores: 1
    instances: 1
    memory: "512m"
    labels:
      version: 3.5.1
" | kubectl apply -f -
```

To kill all Spark applications.

```bash
kubectl delete sparkapp --all
```

To uninstall Spark operator.

```bash
helm uninstall devops-spark-operator -n compordept-project-env
kubectl config set-context docker-desktop --namespace=default
kubectl delete namespace compordept-project-env
kubectl delete crds scheduledsparkapplications.sparkoperator.k8s.io sparkapplications.sparkoperator.k8s.io
helm list
kubectl get ns
kubectl get crds
```
