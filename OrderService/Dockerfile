# Use Maven image to build the application
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml and install dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# --------------------
# Final image with JDK
# --------------------
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy JAR file from build stage
COPY --from=build /app/target/OrderService-0.0.1-SNAPSHOT.jar app.jar

# Expose port (match your Spring Boot server.port)
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
