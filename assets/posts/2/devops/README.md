# DevOps

## Installation

Please install [Docker Desktop](https://www.docker.com/products/docker-desktop/).

Please install VSCode extension(s):

- Docker

## Local build

To build local docker image:

```bash
docker build -t lovindata/tarp:local -f "prod-build/Dockerfile" ..
```

To investigate the content of the docker image:

```bash
docker run -it --entrypoint /bin/sh lovindata/tarp:local
```

To clean docker build cache:

```bash
docker builder prune -af
```

To clean docker build cache & dandling images:

```bash
docker system prune -f
```

## Remote multi-platform build

To list, create, use and delete the multi-platform builder:

```bash
docker buildx ls
```

```bash
docker buildx create --use --name multi-platform-builder
```

```bash
docker buildx use multi-platform-builder
```

```bash
docker buildx rm multi-platform-builder
```

To build and push multi-platform docker images:

```bash
docker buildx build -t lovindata/tarp:0.0.0 -t lovindata/tarp:latest --platform linux/amd64,linux/arm64 --push -f "prod-build/Dockerfile" ..
```

To clean docker buildx cache:

```bash
docker buildx prune -af
```
