# ── Stage 1: Build ────────────────────────────────────────────
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# Copy Maven wrapper & config first for layer caching
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw

# Download dependencies (cached unless pom.xml changes)
RUN ./mvnw dependency:resolve -B || true

# Copy all source files (Java, resources, XSD, proto)
COPY src/ src/

# Build the fat JAR (compiles proto, XSD, and Spring Boot application)
RUN ./mvnw clean package -DskipTests -B

# ── Stage 2: Runtime ──────────────────────────────────────────
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Install curl/wget for health checks
RUN apt-get update && apt-get install -y --no-install-recommends curl wget && rm -rf /var/lib/apt/lists/*

# Run as non-root user
RUN groupadd -r gymtrack && useradd -r -g gymtrack gymtrack

# Copy JAR from build stage
COPY --from=build /app/target/*.jar app.jar
RUN chown -R gymtrack:gymtrack /app

USER gymtrack

ENV PORT=8081
EXPOSE 8081 9090

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -Dserver.port=${PORT} -jar app.jar"]
