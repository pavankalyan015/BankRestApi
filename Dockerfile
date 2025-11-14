FROM azul/zulu-openjdk-alpine:24

WORKDIR /app

COPY target/bank-app.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
