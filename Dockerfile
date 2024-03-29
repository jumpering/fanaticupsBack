FROM maven:3-eclipse-temurin-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk
COPY --from=build /target/fanaticupsBack-0.0.1-SNAPSHOT.jar fanaticupsBack.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","fanaticupsBack.jar"]