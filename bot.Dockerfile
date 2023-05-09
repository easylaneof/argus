FROM amazoncorretto:17.0.7-alpine

COPY bot/target/bot-*.jar bot.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/bot.jar"]
