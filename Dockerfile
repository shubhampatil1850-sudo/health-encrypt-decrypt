#FROM openjdk:17
#EXPOSE 8080
#ADD target/encrypt-decrypt-json.jar encrypt-decrypt-json.jar
#ENTRYPOINT ["java","-jar","/encrypt-decrypt-json.jar"]

# 1. Use Amazon Corretto 17 on the latest Amazon Linux (AL2023)
# The "headless" version is smaller and more secure for microservices.
FROM amazoncorretto:17-al2023-headless

# 2. Set a working directory (Best practice for organization)
WORKDIR /app

# 3. Inform ECS that the container listens on port 8080
EXPOSE 8080

# 4. Copy the JAR file.
# Using COPY is preferred over ADD for local file transfers.
COPY target/encrypt-decrypt-json.jar app.jar

# 5. Run the application
# We use the full path to the jar inside our WORKDIR
ENTRYPOINT ["java", "-jar", "app.jar"]