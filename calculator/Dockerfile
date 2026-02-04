FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml ./
COPY messaging ./messaging
COPY calculator ./calculator
COPY rest ./rest

RUN mvn -q -DskipTests -pl calculator -am package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/calculator/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]