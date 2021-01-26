FROM openjdk:8-jdk-alpine

WORKDIR /usr/src/app

COPY . /usr/src/app

RUN /usr/src/app/mvnw package