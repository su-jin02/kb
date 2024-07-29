# Use an official Maven image with OpenJDK 20
FROM maven:3.8.6-openjdk-20 AS builder

# Copy the pom.xml and download the dependencies
COPY ./pom.xml ./pom.xml
RUN mvn dependency:go-offline -f ./pom.xml

# Copy the source code
COPY src ./src/

# Package the application
RUN mvn clean package && mv target/store-spring-1.0.0-exec.jar store-spring.jar
RUN rm -rf ~/.m2/repository

# Use an official OpenJDK 20 runtime image
FROM openjdk:20-jdk-slim

# Install shadow-utils (for groupadd and adduser commands)
RUN apt-get update && apt-get install -y shadow-utils && apt-get clean

# Copy the packaged jar file from the builder stage
COPY --from=builder store-spring.jar store-spring.jar

# Create a non-root user and group
RUN groupadd --system spring -g 1000
RUN adduser --system spring -u 1000 -g 1000

# Change to non-root user
USER 1000:1000

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "-Dserver.port=8080", "/store-spring.jar"]
