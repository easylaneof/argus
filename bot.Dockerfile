FROM amazoncorretto:17.0.7-alpine

COPY bot/target/bot-*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
