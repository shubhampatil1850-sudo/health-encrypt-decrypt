#FROM openjdk:17
#EXPOSE 8080
#ADD target/encrypt-decrypt-json.jar encrypt-decrypt-json.jar
#ENTRYPOINT ["java","-jar","/encrypt-decrypt-json.jar"]

# Use Amazon Corretto 17 as the base image
FROM amazoncorretto:17-alpine

# Copy the jar file from your target folder
# Using COPY is generally preferred over ADD for local files
COPY target/encrypt-decrypt-json.jar encrypt-decrypt-json.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "encrypt-decrypt-json.jar"]