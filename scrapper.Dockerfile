FROM amazoncorretto:17.0.7-alpine

COPY scrapper/target/scrapper-*.jar scrapper.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/scrapper.jar"]
