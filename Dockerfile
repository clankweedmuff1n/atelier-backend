FROM openjdk:21-jdk

WORKDIR /app

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} spring-boot-docker.jar

CMD ["java", "-jar", "/app/spring-boot-docker.jar"]

EXPOSE 8000