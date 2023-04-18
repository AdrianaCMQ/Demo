FROM openjdk:16

WORKDIR /app

COPY /build/libs/O-quiz-final-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]