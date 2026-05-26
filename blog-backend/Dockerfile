# Build stage
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests -B

# Run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Install curl for health check
RUN apk add --no-cache curl

COPY --from=build /app/target/*.jar app.jar

# Environment variables with defaults
ENV SERVER_PORT=8080 \
    JAVA_OPTS="-Xms256m -Xmx512m"

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=30s \
  CMD curl -f http://localhost:8080/api/front/article/list || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
