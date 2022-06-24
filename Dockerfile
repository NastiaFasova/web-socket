FROM adoptopenjdk/openjdk11:latest
ADD target/social-network-0.0.1-SNAPSHOT.jar social-network-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "social-network-0.0.1-SNAPSHOT.jar"]
