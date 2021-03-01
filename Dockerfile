FROM openjdk:8-jre-alpine
ADD target/rjz.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]