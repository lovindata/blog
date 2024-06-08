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

### Commands

To install Spark service account on a given namespace.

```bash
helm install devops-spark . -n devops-spark --create-namespace
kubectl config set-context docker-desktop --namespace=devops-spark
helm list
kubectl get ns
```

To submit a Spark application.

```bash
spark-submit --master k8s://https://kubernetes.docker.internal:6443 --deploy-mode cluster --name spark-app --class org.apache.spark.examples.SparkPi --conf spark.kubernetes.driver.request.cores=50m --conf spark.kubernetes.executor.request.cores=200m --conf spark.driver.memory=512m --conf spark.executor.memory=512m --conf spark.executor.instances=2 --conf spark.kubernetes.container.image=spark:3.5.1-scala2.12-java17-ubuntu --conf spark.kubernetes.namespace=devops-spark --conf spark.kubernetes.authenticate.driver.serviceAccountName=spark-sa local:///opt/spark/examples/jars/spark-examples_2.12-3.5.1.jar 1
```

To kill all Spark applications.

```bash
spark-submit --kill devops-spark:spark-app* --master k8s://https://kubernetes.docker.internal:6443 --conf spark.kubernetes.namespace=devops-spark --conf spark.kubernetes.authenticate.driver.serviceAccountName=spark-sa*
kubectl get pods
```

To uninstall Spark service account.

```bash
helm uninstall devops-spark -n devops-spark
kubectl config set-context docker-desktop --namespace=default
kubectl delete namespace devops-spark
helm list
kubectl get ns
```
