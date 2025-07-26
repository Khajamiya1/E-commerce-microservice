# Step 1: Use Maven to build the app
FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Use OpenJDK to run the app
FROM openjdk:17
WORKDIR /app
COPY --from=builder /app/target/*.jar eureka-server.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "eureka-server.jar"]
