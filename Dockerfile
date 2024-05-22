FROM jelastic/maven:3.9.5-openjdk-21 AS build
COPY . .
RUN mvn clean package   -DskipTests

FROM openjdk:21-slim
COPY --from=build /target/kanemuraproject-3.2.5.jar kanemuraproject.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","kanemuraproject.jar"]
