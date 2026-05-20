# ==============================================================================
# STAGE 1: Build the application using Maven
# ==============================================================================
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app

# Copy the pom.xml to download dependencies first (improves build caching)
COPY pom.xml .

# Download dependencies offline to speed up subsequent builds
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src ./src

# Package the application (skipping unit tests for deployment build speed)
RUN mvn clean package -DskipTests

# ==============================================================================
# STAGE 2: Create the production runtime image
# ==============================================================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Add a non-privileged system user for security best practices
RUN addgroup -S spring && adduser -S spring -G spring

# Copy the packaged jar from the builder stage
COPY --from=builder /app/target/clothing-store-api-0.0.1-SNAPSHOT.jar app.jar

# Ensure the app jar is owned by the spring user
RUN chown -R spring:spring /app

# Switch to the non-root user
USER spring:spring

# Expose the default application port (8080)
EXPOSE 8080

# Environment variables defaults (can be overridden during runtime)
ENV PORT=8080 \
    SPRING_PROFILES_ACTIVE=prod

# Command to execute the application
ENTRYPOINT ["java", "-jar", "app.jar"]