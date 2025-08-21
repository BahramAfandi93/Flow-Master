FROM openjdk:17-jre-slim

# Set working directory
WORKDIR /app

# Copy the built JAR file
COPY build/libs/flowmaster-*.jar app.jar

# Expose the port that Spring Boot will run on
EXPOSE 8080

# Set environment variables for production
ENV SPRING_PROFILES_ACTIVE=prod

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
