FROM openjdk:17-jdk-alpine

COPY db-api.jar app.jar
COPY application.properties application.properties

ENTRYPOINT ["java", "-jar", "app.jar"]