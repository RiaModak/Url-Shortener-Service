# Dockerfile

# =========================================================================
# STAGE 1: The "Builder" Stage (The Workshop)
# =========================================================================
# We start with an official Maven image that includes OpenJDK 17.
# This image is our fully-equipped workshop.
# We give this stage a name, "builder", so we can refer to it later.

FROM maven:3.8.5-openjdk-17 AS builder

# Set the working directory inside the container.
WORKDIR /app

# Docker Caching Optimization: Copy the pom.xml file first.
# As long as pom.xml doesn't change, Docker can reuse the cached layer
# from the next step, making builds much faster.
COPY pom.xml .

# Download all project dependencies. This step is cached as long as
# pom.xml remains unchanged. This is the most time-consuming part,
# so caching it is a huge win.
RUN mvn dependency:go-offline

# Now, copy the rest of our application's source code. If only our source
# code changes (and not the dependencies), the build will start from here.
COPY src ./src

# Build the application, creating the executable JAR file.
# -DskipTests is a best practice for Docker builds, as tests should
# be run in a separate CI/CD pipeline step.
RUN mvn package -DskipTests

# =========================================================================
# STAGE 2: The "Runner" Stage (The Showroom)
# =========================================================================
# We start the final stage fresh with a minimal, production-ready JRE image.
# The 'slim' variant is significantly smaller and more secure than the full JDK image.
# This is our clean, empty showroom.
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory for our running application.
WORKDIR /app

# The magic of multi-stage builds!
# Copy ONLY the final JAR file from the 'builder' stage into our new stage.
# We find the JAR in the 'target' directory (where Maven places it) and
# rename it to a consistent 'app.jar' for simplicity.
COPY --from=builder /app/target/*.jar app.jar

# This instruction informs Docker that the container listens on the specified
# network port at runtime. It's primarily for documentation and doesn't
# actually publish the port to the host.
EXPOSE 8080

# This is the command that will be executed when the container starts.
# It tells Java to run our Spring Boot application.
# The exec form (in brackets) is preferred because it allows the application
# to properly receive signals from the host OS (like a shutdown signal).
ENTRYPOINT ["java", "-jar", "app.jar"]