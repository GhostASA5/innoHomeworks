FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/InnopolisHomework-1.0-SNAPSHOT.jar app.jar

EXPOSE 8090

CMD ["java", "-jar", "app.jar"]