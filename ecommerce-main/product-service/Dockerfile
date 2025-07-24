# Use OpenJDK base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the built jar to the container
COPY target/product-service-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the app runs on
EXPOSE 8082

# Command to run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
