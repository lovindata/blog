---
title: "Why I Dumped Kubeflow Spark Operator (formerly Google's Spark Operator)!"
date: 2024-07-06
categories:
  - Kubernetes
  - Data Engineering
  - DevOps
---

Heyoooo [Spark ⚡](https://spark.apache.org/) developers! 🌟 I am a Data Engineer and it's been more than 3 years that I've been developing Spark-based applications. After implementing your big data pipeline locally using Spark, the next phase is to run it on a cluster. I remember when I was a junior and started several years ago running my pipeline on a cluster; it was always the not-so-fun part to do. 😅 I say no fun because it's all about configuration on the infrastructure side and Spark side. Plus, I dunno why, but at the time, it was always harder to do than developing the pipeline. Maybe it's because it introduces several technologies to learn and orchestrate together at the same time. But yep, I am writing today to show you how you can set up a development [K8s 🐳](https://kubernetes.io/) cluster locally and how to run a Spark application on it using basic [Spark CLI 💻](https://spark.apache.org/docs/latest/running-on-kubernetes.html) or [Kubeflow 🔧 Spark Operator](https://github.com/kubeflow/spark-operator)! 🚀

<!-- more -->

![Spark on K8s](image.png)

## 🤔 K8s, Spark and Kubeflow?

What are **K8s**, **Spark**, and **Kubeflow Spark Operator**? Quickly:

- 🐳 [**K8s**](https://kubernetes.io/): Kubernetes (K8s) is an open-source container orchestration platform designed to automate the deployment, scaling, and management of containerized applications, often used to orchestrate complex frameworks like [Apache Spark](https://spark.apache.org/) for efficient data processing at scale.
- ⚡ [**Spark**](https://spark.apache.org/): Apache Spark is an open-source distributed computing framework that enables fast data processing and analytics, widely recognized and supported by the company [Databricks](https://www.databricks.com/) in big data environments.
- 🔧 [**Kubeflow Spark Operator**](https://github.com/kubeflow/spark-operator): The Kubeflow Spark Operator facilitates seamless integration of Apache Spark with Kubernetes. Originally developed by [Google Cloud Platform](https://cloud.google.com/?hl=en), it has recently been donated to the [Kubeflow community](https://www.kubeflow.org/).

At the end of this guide, you should also be able to launch a Spark application on a Kubernetes cluster and understand when to use basic Spark CLI or the Kubeflow Spark Operator. Let's get started!

## 👨‍💻 Local K8s for development

## 💻 Spark on K8s via CLI

### Submitting a Spark job

### Multi-tenant K8s

## 🔧 Spark on K8s via Kubeflow

### Submitting a Spark job

### Multi-tenant K8s

## 🔚 Conclusion

I write monthly on the [LovinData Blog](https://lovindata.github.io/blog/) and on [Medium](https://medium.com/@jamesjg), and like to give back the knowledge I've learned. So don't hesitate to reach out; I'm always available to chat about nerdy stuff 🤗! Here are my socials: [LinkedIn](https://www.linkedin.com/in/james-jiang-87306b155/), [Twitter](https://twitter.com/jamesjg_) and [Reddit](https://www.reddit.com/user/lovindata/). Otherwise, let's learn together in the next story 🫡! Bye ❤️.
