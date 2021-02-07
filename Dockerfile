FROM openjdk:8-jdk-alpine

WORKDIR /usr/src/app
COPY . .
RUN /usr/src/app/mvnw package
RUN find ./target -name "*.jar" -exec cp {} app.jar \;
