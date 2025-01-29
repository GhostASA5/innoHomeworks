FROM openjdk:21-oracle

WORKDIR /app

COPY target/InnopolisHomework-1.0-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]