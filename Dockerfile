# ---- Stage 1: Build the app ----
FROM gradle:8.9-jdk17 AS builder
WORKDIR /home/gradle/app
COPY --chown=gradle:gradle . .
RUN gradle clean bootJar --no-daemon

# ---- Stage 2: Run the app ----
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /home/gradle/app/build/libs/*.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
