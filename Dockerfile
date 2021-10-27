FROM openjdk:16
ARG TOKEN_ARG
ENV TOKEN=$TOKEN_ARG

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]