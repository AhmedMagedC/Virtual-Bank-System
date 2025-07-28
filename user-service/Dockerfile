# ---------- Stage 1: Build the application ----------
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom.xml and download dependencies (cached if pom.xml doesn't change)
COPY pom.xml .
COPY .mvn/ .mvn
COPY mvnw .
RUN ./mvnw dependency:go-offline

# Copy the rest of the project files
COPY . .

# Build the application (skip tests for faster build)
RUN ./mvnw clean package -DskipTests

# ---------- Stage 2: Create minimal runtime image ----------
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy only the jar from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
