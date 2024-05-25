FROM openjdk:21-jdk-slim
VOLUME /tmp
COPY target/*.jar kanemura-project-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "kanemura-project-0.0.1-SNAPSHOT.jar"]
