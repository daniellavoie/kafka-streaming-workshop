# Kafka Streams and ksqlDB Workshop

## Workshop pre-requisites

Make sure you have the following toolset installed on your compute.

* Docker
  * [Windows 10 Pro or Enterprise](https://docs.docker.com/docker-for-windows/install/)
  * [Windows 10 Home, 8 or 7](https://github.com/docker/toolbox/releases/download/v19.03.1/DockerToolbox-19.03.1.exe)
  * [MacOS](https://docs.docker.com/docker-for-mac/install/)
* Docker Compose (installed with Docker Desktop)
* git (Installed with Docker Desktop or Docker Toolbox)
* A bash based environment (git bash is installed with Docker Desktop or Docker Toolbox)
* [Java 8 (or later)](https://adoptopenjdk.net/?variant=openjdk11&jvmVariant=hotspot)
* Your favorite Java IDE ([Eclipse](https://www.eclipse.org/downloads/packages/release/2019-12/r/eclipse-ide-enterprise-java-developers) or [IntelJ IDEA](https://www.jetbrains.com/idea/))

## Important notes for running this workshop with Windows

This workshop is compatible with Windows. Just ensure you have Docker Toolbox or Docker Desktop installed. If you are running with Docker Toolbox, follow the instructions very very carefully as you need to configure your Virtual Box Docker VM with some specific port redirection as well as enough memory to run the workshop properly.

[Docker Toolbox configuration prerequisites](doc/docker-toolbox/docker-toolbox.md)

## Skip writing code and just write the demo

This repository is intended as a hands on workshop. At any given time, you can switch the `solution` branch and run the applications without writing any code.

```
git checkout solution
```

## Workshop Instructions

* [Workshop preperation](doc/preperation/preperations.md)
* [Loading referential data with Kafka Connect](doc/connector/connector-linux.md)
* [Implementing a Stream Processor with Kafka Streams](doc/streams/streams.md)
* [Enrich transaction results with ksqlDB](doc/ksqldb/ksqldb.md)

Made with love by [Daniel Lavoie](https://github.com/daniellavoie)