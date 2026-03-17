FROM openjdk:17
EXPOSE 8080
ADD target/encrypt-decrypt-json.jar encrypt-decrypt-json.jar
ENTRYPOINT ["java","-jar","/encrypt-decrypt-json.jar"]