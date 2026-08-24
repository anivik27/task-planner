FROM eclipse-temurin:21-jdk AS build

WORKDIR /planner-service

COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .

RUN chmod +x gradlew

RUN ./gradlew dependencies --no-daemon

COPY src ./src

RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine

WORKDIR /planner-service

COPY --from=build /planner-service/build/libs/*.jar planner-service.jar

ENTRYPOINT ["java", "-jar", "planner-service.jar"]