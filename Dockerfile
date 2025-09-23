# ---- Stage 1: Build ----
FROM gradle:8.10.2-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle clean bootJar --no-daemon

# ---- Stage 2: Runtime ----
FROM eclipse-temurin:17-jdk
WORKDIR /code
COPY --from=build /app/build/libs/flowmaster-0.0.1.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]



#WORKDIR /code/
#COPY build/libs/flowmaster-0.0.1.jar app.jar
#EXPOSE 8080
#CMD ["java", "-jar", "app.jar"]