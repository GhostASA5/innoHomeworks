FROM openjdk:21-oracle

WORKDIR /app

COPY target/InnopolisHomework-1.0-SNAPSHOT.jar app.jar
COPY src/main/resources/figures.txt /app/resources/figures.txt

CMD ["java", "-jar", "app.jar"]