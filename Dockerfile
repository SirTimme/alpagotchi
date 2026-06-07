FROM maven:3.9 AS build

WORKDIR /home/app

COPY pom.xml .
RUN mvn verify

COPY src src
RUN mvn package

FROM eclipse-temurin:21-jre-alpine

WORKDIR /home/app

COPY --from=build /home/app/target/Alpagotchi-jar-with-dependencies.jar alpagotchi.jar

ENTRYPOINT [ "java", "-jar", "alpagotchi.jar" ]