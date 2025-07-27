# Stage 1: Build the application using Maven
FROM maven:3.9.6-eclipse-temurin-17 as builder

WORKDIR /app

# Copy all project files
COPY . .

# Build the jar
RUN mvn clean package -DskipTests

# Stage 2: Run the built application
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy only the final jar from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
