# Stage 1: Build the JAR using Maven Wrapper
FROM eclipse-temurin:22-jdk AS builder

# Set the working directory
WORKDIR /usr/src/app

# Copy the entire project, excluding files specified in .dockerignore
COPY . .

# Ensure the Maven wrapper is executable
RUN chmod +x mvnw

# Copy 'frontend' into 'src/main/frontend' if required by your project structure
RUN mkdir -p src/main && mv frontend src/main/frontend

# Build the application JAR with the production profile
RUN ./mvnw clean install -Pproduction -DskipTests -B

# Stage 2: Runtime
FROM eclipse-temurin:22-jdk-alpine

# Set the working directory
WORKDIR /usr/src/app

# Copy the JAR file from the builder stage
COPY --from=builder /usr/src/app/target/*.jar ./app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
