#FROM openjdk:17
#EXPOSE 8080
#ADD target/encrypt-decrypt-json.jar encrypt-decrypt-json.jar
#ENTRYPOINT ["java","-jar","/encrypt-decrypt-json.jar"]

# Stage 1: Build the application
FROM maven:3.9.6-amazoncorretto-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM amazoncorretto:17-alpine-jdk
EXPOSE 8080
# Copy the jar from the 'build' stage.
# NOTE: Ensure 'ecsfargate.jar' matches the <finalName> in your pom.xml
COPY --from=build /target/*.jar encrypt-decrypt-json.jar
ENTRYPOINT ["java", "-jar", "/encrypt-decrypt-json.jar"]