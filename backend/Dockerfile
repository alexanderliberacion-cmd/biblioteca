FROM eclipse-temurin:26-jdk AS build
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline || ./mvnw dependency:resolve-plugins
COPY src ./src
RUN ./mvnw clean package -DskipTests
FROM eclipse-temurin:26-jre
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/app.jar"]
