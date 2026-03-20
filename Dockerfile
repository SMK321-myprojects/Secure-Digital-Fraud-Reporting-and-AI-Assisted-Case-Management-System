# Use Maven with JDK 17 for building
FROM maven:3.9.9-eclipse-temurin-17 AS build

# Set working directory in container
WORKDIR /app

# Copy entire project into container
COPY . .

# Move to the fraud module where pom.xml exists
WORKDIR /app/fraud

# Build the project, skip tests
RUN mvn clean package -DskipTests

# ---- Runtime stage ----
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app/fraud

# Copy the built jar from the build stage
COPY --from=build /app/fraud/target/*.jar ./app.jar

# Run the jar
CMD ["java", "-jar", "app.jar"]
