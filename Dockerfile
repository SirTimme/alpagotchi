FROM maven:3.9.3 AS build
ENV HOME=/home/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD pom.xml $HOME
RUN mvn verify
ADD src $HOME/src
RUN mvn package

FROM openjdk:17
ENV HOME=/home/app
WORKDIR $HOME
ARG TOKEN=""
ENV TOKEN=$TOKEN
ARG POSTGRES_USER=""
ENV POSTGRES_USER=$POSTGRES_USER
ARG POSTGRES_PASSWORD=""
ENV POSTGRES_PASSWORD=$POSTGRES_PASSWORD
ARG POSTGRES_URL=""
ENV POSTGRES_URL=$POSTGRES_URL
ENV DEV_ID=483012399893577729
COPY --from=build $HOME/target/Alpagotchi-jar-with-dependencies.jar app.jar
CMD java -jar app.jar