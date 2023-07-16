FROM maven:3.9.3 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:17
WORKDIR /home/app
ARG TOKEN=""
ENV TOKEN=$TOKEN
ENV DEV_ID=483012399893577729
COPY --from=build /home/app/target/Alpagotchi-jar-with-dependencies.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]