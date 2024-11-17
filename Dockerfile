# Stage 1: Build the JAR using Maven Wrapper
FROM eclipse-temurin:22-jdk AS builder

# Set the working directory
WORKDIR /usr/src/app

# Copy Maven wrapper and pom.xml and package.json to download dependencies
COPY .mvn .mvn
COPY package.json ./
COPY mvnw pom.xml ./

# Ensure the Maven wrapper is executable
RUN chmod +x mvnw

# Download dependencies (cacheable layer)
RUN ./mvnw dependency:go-offline -B

# Copy the rest of the source code
COPY src ./src

# Copy 'frontend' into 'src/main/frontend' if required by your project structure
COPY frontend ./src/main/frontend

# Build the application JAR with the production profile
RUN ./mvnw clean install -Pproduction -DskipTests -B

# Stage 2: Runtime
FROM eclipse-temurin:22-jre-alpine

# Set the working directory
WORKDIR /usr/src/app

# Copy the JAR file from the builder stage
COPY --from=builder /usr/src/app/target/*.jar ./app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
