# Kafka Streams and ksqlDB Workshop

## Workshop pre-requisites

Make sure you have the following toolset installed on your compute.

* [git](https://git-scm.com/)
* A bash based environment (eg: [cygwin](https://www.cygwin.com/) or [gitbash for Windows]((https://git-scm.com/)))
* Docker ([linux](https://docs.docker.com/install/), [macos](https://docs.docker.com/docker-for-mac/install/) or [Windows](https://docs.docker.com/docker-for-windows/install/))
* Docker Compose (installed with Docker Desktop)
* Java 8 (or later)
* Your favorite Java IDE

## Note for running this workshop with Windows

This workshop is compatible with Windows. Just ensure you have git bash or cygwin installed on your machine. Docker Desktop for Windows is also a requirement.

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