# Use a base image with Maven 3.9.8 and JDK 17
FROM public.ecr.aws/docker/library/maven:3.8.1-amazoncorretto-17 as builder

COPY ./pom.xml ./pom.xml

# Force update of dependencies
RUN mvn dependency:go-offline -U -f ./pom.xml

COPY src ./src/
RUN mvn clean package && mv target/store-spring-1.0.0-exec.jar store-spring.jar
RUN rm -rf ~/.m2/repository

FROM public.ecr.aws/docker/library/amazoncorretto:17-al2023
RUN yum install -y shadow-utils

COPY --from=builder store-spring.jar store-spring.jar

RUN groupadd --system spring -g 1000
RUN adduser spring -u 1000 -g 1000

USER 1000:1000

EXPOSE 8080
ENTRYPOINT ["java","-jar","-Dserver.port=8080","/store-spring.jar"]
