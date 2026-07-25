# ---------- Stage 1: Build ----------
# Uses a Maven + JDK 21 image just for compiling — this layer is discarded
# from the final image, so its size doesn't matter.
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy only the pom.xml first and download dependencies.
# Docker caches this layer, so dependencies are only re-downloaded
# when pom.xml actually changes — not on every code edit.
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Now copy the actual source and build the jar.
COPY src ./src
RUN mvn clean package -DskipTests -B

# ---------- Stage 2: Run ----------
# A minimal JRE-only image (no JDK, no Maven) — this is what actually
# ships. Nobody running this container needs Java installed on their
# own machine at all.
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy only the built jar from the build stage.
COPY --from=build /app/target/*.jar app.jar

# Render (and most platforms) inject $PORT at runtime — default to
# 8080 for local `docker run` where no PORT is set.
ENV PORT=8080
EXPOSE 8080

# Respect $PORT if the platform sets one, otherwise fall back to 8080.
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT:-8080}"]
