# Build stage
FROM maven:3.9.11-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/bidding-java-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9093
ENTRYPOINT ["java","-jar","app.jar"]