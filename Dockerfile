# Use a base image with Maven and JDK 17
FROM maven:3.8.6-amazoncorretto-17 as builder

ENV SPRING_DATASOURCE_URL=arn:aws:ssm:us-west-2:568558386229:parameter/databaseJDBCConnectionString
ENV SPRING_DATASOURCE_PASSWORD=arn:aws:secretsmanager:us-west-2:568558386229:secret:kbdb-CRAY6C

# Set the working directory
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY ./pom.xml ./pom.xml
RUN mvn dependency:purge-local-repository && mvn dependency:go-offline -f ./pom.xml

# Copy the source code
COPY ./src ./src

# Package the application
RUN mvn clean package -DskipTests

# Use the amazoncorretto base image for the runtime
FROM amazoncorretto:17-alpine-jdk

# Set the working directory
WORKDIR /app

# Copy the packaged jar from the builder stage
COPY --from=builder /app/target/store-spring-1.0.0.jar store-spring.jar

# Create a user and group for running the application
RUN addgroup -S spring && adduser -S spring -G spring

# Set the user to run the application
USER spring:spring

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "store-spring.jar"]