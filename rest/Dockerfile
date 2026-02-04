FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml ./
COPY messaging ./messaging
COPY calculator ./calculator
COPY rest ./rest

RUN mvn -q -DskipTests -pl rest -am package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/rest/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]