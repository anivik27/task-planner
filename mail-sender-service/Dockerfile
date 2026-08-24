FROM eclipse-temurin:21-jdk AS build

WORKDIR /mail-sender-service

COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .

RUN chmod +x gradlew

RUN ./gradlew dependencies --no-daemon

COPY src ./src

RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine

WORKDIR /mail-sender-service

COPY --from=build /mail-sender-service/build/libs/*.jar mail-sender-service.jar

ENTRYPOINT ["java", "-jar", "mail-sender-service.jar"]