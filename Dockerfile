#
FROM maven:4.0.0-openjdk-21 AS build
COPY . .
RUN mvn clean install

#
Package stage
#
FROM eclipse-temurin:21-jdk
COPY --from=build /target/kanemura-project-0.0.1-SNAPSHOT.jar kanemura-project.jar
ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","kanemura-project.jar"]