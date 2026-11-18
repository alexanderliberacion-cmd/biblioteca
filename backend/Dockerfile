FROM eclipse-temurin:26-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests
FROM eclipse-temurin:26-jre
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar
EXPOSE 8080
CMD ["java",  "-XX:+UnlockDiagnosticVMOptions", "-XX:-UseBASE64Intrinsics", "-jar", "/app/app.jar"]
