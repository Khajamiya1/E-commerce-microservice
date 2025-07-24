# Use a Maven image to build the app
FROM maven:3.9.6-eclipse-temurin-17 as builder

# Set working directory inside container
WORKDIR /app

# Copy source code
COPY . .

# Build the project
RUN mvn clean package -DskipTests

# Use a lightweight JDK image to run the app
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the jar from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose port (adjust if your gateway uses a different port)
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
